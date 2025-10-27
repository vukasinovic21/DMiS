package rs.raf.pds.v4.z5.messages;

public class RPCCreateRoomReq {
    String roomName;
    protected RPCCreateRoomReq() {}
    public RPCCreateRoomReq(String roomName) { this.roomName = roomName; }
    public String getRoomName() { return roomName; }
}