package rs.raf.pds.v4.z5.messages;

import java.util.List;

public class RPCRoomMessagesRes {
    List<RPCRoomChatMessage> lastMessages;
    protected RPCRoomMessagesRes() {}
    public RPCRoomMessagesRes(List<RPCRoomChatMessage> lastMessages) { this.lastMessages = lastMessages; }
    public List<RPCRoomChatMessage> getLastMessages() { return lastMessages; }
}