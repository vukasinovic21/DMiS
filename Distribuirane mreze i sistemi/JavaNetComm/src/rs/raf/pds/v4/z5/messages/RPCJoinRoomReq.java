package rs.raf.pds.v4.z5.messages;

public class RPCJoinRoomReq {
    String roomName;
    String userName;
    protected RPCJoinRoomReq() {}
    public RPCJoinRoomReq(String roomName, String userName) {
        this.roomName = roomName;
        this.userName = userName;
    }
    public String getRoomName() { return roomName; }
    public String getUserName() { return userName; }
}