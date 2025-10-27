package rs.raf.pds.v4.z5;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import rs.raf.pds.v4.z5.messages.*;


public class ChatServer implements Runnable{

	private volatile Thread thread = null;
	
	volatile boolean running = false;
	final Server server;
	final int portNumber;
	ConcurrentMap<String, Connection> userConnectionMap = new ConcurrentHashMap<String, Connection>();
	ConcurrentMap<Connection, String> connectionUserMap = new ConcurrentHashMap<Connection, String>();
	ConcurrentMap<String, ChatRoomHistory> chatRooms = new ConcurrentHashMap<>();

	public ChatServer(int portNumber) {
		this.server = new Server();
		
		this.portNumber = portNumber;
		KryoUtil.registerKryoClasses(server.getKryo());
		registerListener();
	}
	private void registerListener() {
		server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof Login) {
					Login login = (Login)object;
					newUserLogged(login, connection);
					connection.sendTCP(new InfoMessage("Hello "+login.getUserName()));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
				if (object instanceof ChatMessage) {
					ChatMessage chatMessage = (ChatMessage)object;
					System.out.println(chatMessage.getUser()+":"+chatMessage.getTxt());
					broadcastChatMessage(chatMessage, connection); 
					return;
				}

				if (object instanceof WhoRequest) {
					ListUsers listUsers = new ListUsers(getAllUsers());
					connection.sendTCP(listUsers);
					return;
				}

				if (object instanceof PrivateMessage) {
					PrivateMessage pm = (PrivateMessage) object;
					Connection toConn = userConnectionMap.get(pm.getToUser());
					if (toConn != null && toConn.isConnected()) {
						toConn.sendTCP(new ChatMessage("(private) " + pm.getFromUser(), pm.getTxt()));
					} else {
						connection.sendTCP(new InfoMessage("User " + pm.getToUser() + " not found."));
					}
					return;
				}

				if (object instanceof MultiMessage) {
					MultiMessage mm = (MultiMessage) object;
					for (String toUser : mm.getToUsers()) {
						Connection connTo = userConnectionMap.get(toUser);
						if (connTo != null && connTo.isConnected()) {
							connTo.sendTCP(new ChatMessage("(multi) " + mm.getFromUser(), mm.getTxt()));
						}
					}
					return;
				}

				if (object instanceof RPCCreateRoomReq) {
					RPCCreateRoomReq req = (RPCCreateRoomReq) object;
					chatRooms.putIfAbsent(req.getRoomName(), new ChatRoomHistory(req.getRoomName()));
					connection.sendTCP(new InfoMessage("Room '" + req.getRoomName() + "' created."));
					return;
				}

				if (object instanceof RPCListRoomsReq) {
					String[] rooms = chatRooms.keySet().toArray(new String[0]);
					connection.sendTCP(new RPCListRoomsRes(rooms));
					return;
				}

				if (object instanceof RPCJoinRoomReq) {
					RPCJoinRoomReq req = (RPCJoinRoomReq) object;
					ChatRoomHistory room = chatRooms.get(req.getRoomName());
					if (room != null) {
						room.addUser(req.getUserName());
						connection.sendTCP(new RPCRoomMessagesRes(room.getLastMessages()));
					} else {
						connection.sendTCP(new InfoMessage("Room '" + req.getRoomName() + "' does not exist."));
					}
					return;
				}

				if (object instanceof RPCRoomChatMessage) {
					RPCRoomChatMessage rcm = (RPCRoomChatMessage) object;
					ChatRoomHistory room = chatRooms.get(rcm.getRoomName());
					if (room != null) {
						room.addMessage(rcm);
						for (String user : room.getUsers()) {
							Connection conn = userConnectionMap.get(user);
							if (conn != null && conn.isConnected()) {
								conn.sendTCP(rcm);
							}
						}
					} else {
						connection.sendTCP(new InfoMessage("Room '" + rcm.getRoomName() + "' does not exist."));
					}
					return;
				}

				if (object instanceof RPCGetLastMessagesReq) {
					RPCGetLastMessagesReq req = (RPCGetLastMessagesReq) object;
					ChatRoomHistory room = chatRooms.get(req.getRoomName());
					if (room != null) {
						connection.sendTCP(new RPCRoomMessagesRes(room.getLastMessages()));
					} else {
						connection.sendTCP(new InfoMessage("Room '" + req.getRoomName() + "' does not exist."));
					}
					return;
				}

				if (object instanceof RPCEditMessage) {
					RPCEditMessage edit = (RPCEditMessage) object;
					ChatRoomHistory room = chatRooms.get(edit.getRoomName());
					if (room != null) {
						RPCRoomChatMessage msg = room.getMessageById(edit.getMessageId());
						if (msg != null && msg.getFromUser().equals(edit.getEditor())) {
							msg.setTxt(edit.getNewText());
							msg.setEdited(true);

							for (String user : room.getUsers()) {
								Connection userConn = userConnectionMap.get(user);
								if (userConn != null) userConn.sendTCP(msg);
							}

							System.out.println("Message (#" + msg.getId() + ") edited by " + edit.getEditor());
						}
						else {
							connection.sendTCP(new InfoMessage("You can only edit your own messages."));
						}
					}
					else {
						connection.sendTCP(new InfoMessage("Room '" + room + "' does not exist."));
					}
				}
			}
			
			public void disconnected(Connection connection) {
				String user = connectionUserMap.get(connection);
				connectionUserMap.remove(connection);
				userConnectionMap.remove(user);
				showTextToAll(user+" has disconnected!", connection);
			}
		});
	}
	
	String[] getAllUsers() {
		String[] users = new String[userConnectionMap.size()];
		int i=0;
		for (String user: userConnectionMap.keySet()) {
			users[i] = user;
			i++;
		}
		
		return users;
	}
	void newUserLogged(Login loginMessage, Connection conn) {
		userConnectionMap.put(loginMessage.getUserName(), conn);
		connectionUserMap.put(conn, loginMessage.getUserName());
		showTextToAll("User "+loginMessage.getUserName()+" has connected!", conn);
	}
	private void broadcastChatMessage(ChatMessage message, Connection exception) {
		for (Connection conn: userConnectionMap.values()) {
			if (conn.isConnected() && conn != exception)
				conn.sendTCP(message);
		}
	}
	private void showTextToAll(String txt, Connection exception) {
		System.out.println(txt);
		for (Connection conn: userConnectionMap.values()) {
			if (conn.isConnected() && conn != exception)
				conn.sendTCP(new InfoMessage(txt));
		}
	}
	public void start() throws IOException {
		server.start();
		server.bind(portNumber);
		
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	public void stop() {
		Thread stopThread = thread;
		thread = null;
		running = false;
		if (stopThread != null)
			stopThread.interrupt();
	}
	@Override
	public void run() {
		running = true;
		
		while(running) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) {
		
		if (args.length != 1) {
	        System.err.println("Usage: java -jar chatServer.jar <port number>");
	        System.out.println("Recommended port number is 54555");
	        System.exit(1);
	   }
	    
	   int portNumber = Integer.parseInt(args[0]);
	   try { 
		   ChatServer chatServer = new ChatServer(portNumber);
	   	   chatServer.start();
	   
			chatServer.thread.join();
	   } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	}
	
   
   
}
