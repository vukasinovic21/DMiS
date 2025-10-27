package rs.raf.pds.v4.z5.messages;

public class RPCRoomChatMessage {
    String roomName;
    String fromUser;
    String txt;

    protected RPCRoomChatMessage() {}

    public RPCRoomChatMessage(String roomName, String fromUser, String txt) {
        this.roomName = roomName;
        this.fromUser = fromUser;
        this.txt = txt;
    }

    public String getRoomName() { return roomName; }
    public String getFromUser() { return fromUser; }
    public String getTxt() { return txt; }
}