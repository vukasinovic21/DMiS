package rs.raf.pds.v4.z5.messages;

import com.esotericsoftware.kryo.Kryo;

public class KryoUtil {
	public static void registerKryoClasses(Kryo kryo) {
		kryo.register(String.class);
		kryo.register(String[].class);
		kryo.register(Login.class);
		kryo.register(ChatMessage.class);
		kryo.register(WhoRequest.class);
		kryo.register(ListUsers.class);
		kryo.register(InfoMessage.class);
		kryo.register(PrivateMessage.class);
		kryo.register(MultiMessage.class);
		kryo.register(ChatRoomHistory.class);
		kryo.register(RPCCreateRoomReq.class);
		kryo.register(RPCJoinRoomReq.class);
		kryo.register(RPCListRoomsReq.class);
		kryo.register(RPCListRoomsRes.class);
		kryo.register(RPCRoomMessagesRes.class);
		kryo.register(java.util.LinkedList.class);
		kryo.register(java.util.ArrayList.class);
		kryo.register(RPCRoomChatMessage.class);
		kryo.register(RPCGetLastMessagesReq.class);
	}
}
