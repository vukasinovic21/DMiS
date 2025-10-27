package rs.raf.pds.v4.z5.messages;

public class RPCGetLastMessagesReq {
    String roomName;

    protected RPCGetLastMessagesReq() {}
    public RPCGetLastMessagesReq(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomName() { return roomName; }
}