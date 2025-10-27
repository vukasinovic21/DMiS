package rs.raf.pds.v4.z5.messages;

public class MultiMessage {
    String fromUser;
    String[] toUsers;
    String txt;

    protected MultiMessage() {}

    public MultiMessage(String fromUser, String[] toUsers, String txt) {
        this.fromUser = fromUser;
        this.toUsers = toUsers;
        this.txt = txt;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String[] getToUsers() {
        return toUsers;
    }

    public String getTxt() {
        return txt;
    }
}