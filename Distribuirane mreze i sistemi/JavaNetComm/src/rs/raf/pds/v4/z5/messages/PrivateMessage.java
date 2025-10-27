package rs.raf.pds.v4.z5.messages;

public class PrivateMessage {
    String fromUser;
    String toUser;
    String txt;

    protected PrivateMessage() {}

    public PrivateMessage(String fromUser, String toUser, String txt) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.txt = txt;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public String getTxt() {
        return txt;
    }
}