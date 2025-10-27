package rs.raf.pds.v4.z5.messages;

public class RPCListRoomsRes {
    String[] roomNames;
    protected RPCListRoomsRes() {}
    public RPCListRoomsRes(String[] roomNames) { this.roomNames = roomNames; }
    public String[] getRoomNames() { return roomNames; }
}