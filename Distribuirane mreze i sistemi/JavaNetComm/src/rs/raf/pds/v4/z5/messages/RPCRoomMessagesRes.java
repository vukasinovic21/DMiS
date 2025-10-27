package rs.raf.pds.v4.z5.messages;

import java.util.List;

public class RPCRoomMessagesRes {
    List<ChatMessage> lastMessages;
    protected RPCRoomMessagesRes() {}
    public RPCRoomMessagesRes(List<ChatMessage> lastMessages) { this.lastMessages = lastMessages; }
    public List<ChatMessage> getLastMessages() { return lastMessages; }
}