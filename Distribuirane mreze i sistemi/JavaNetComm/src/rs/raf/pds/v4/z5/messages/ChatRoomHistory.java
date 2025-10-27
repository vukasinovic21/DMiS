package rs.raf.pds.v4.z5.messages;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoomHistory {
    private String roomName;
    private List<String> users;
    private LinkedList<RPCRoomChatMessage> messages;
    private AtomicInteger nextMessageId;

    protected ChatRoomHistory() {}

    public ChatRoomHistory(String roomName) {
        this.roomName = roomName;
        this.users = new LinkedList<>();
        this.messages = new LinkedList<>();
        this.nextMessageId = new AtomicInteger(1);
    }

    public String getRoomName() { return roomName; }

    public List<String> getUsers() { return users; }

    public void addUser(String userName) { users.add(userName); }

    public void removeUser(String userName) { users.remove(userName); }

    public synchronized void addMessage(RPCRoomChatMessage msg) {
        msg.setId(nextMessageId.getAndIncrement());
        messages.add(msg);
    }

    public synchronized List<RPCRoomChatMessage> getLastMessages() {
        return new LinkedList<>(messages);
    }

    public synchronized RPCRoomChatMessage getMessageById(int id) {
        for (RPCRoomChatMessage msg : messages) {
            if (msg.getId() == id) return msg;
        }
        return null;
    }
}