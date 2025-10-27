package rs.raf.pds.v4.z5.messages;

public class RPCRoomChatMessage {
    String roomName;
    String fromUser;
    String txt;
    int id;
    int replyTo;

    protected RPCRoomChatMessage() {}

    public RPCRoomChatMessage(String roomName, String fromUser, String txt) {
        this.roomName = roomName;
        this.fromUser = fromUser;
        this.txt = txt;
        this.replyTo = -1;
    }

    public RPCRoomChatMessage(String roomName, String fromUser, String txt, int replyTo) {
        this.roomName = roomName;
        this.fromUser = fromUser;
        this.txt = txt;
        this.replyTo = replyTo;
    }

    public String getRoomName() { return roomName; }
    public String getFromUser() { return fromUser; }
    public String getTxt() { return txt; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getReplyTo() { return replyTo; }
    public void setReplyTo(int replyTo) { this.replyTo = replyTo; }
}