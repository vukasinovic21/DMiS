package rs.raf.pds.v4.z5.messages;

public class RPCEditMessage {
    private String roomName;
    private int messageId;
    private String newText;
    private String editor;

    protected RPCEditMessage() {}

    public RPCEditMessage(String roomName, int messageId, String newText, String editor) {
        this.roomName = roomName;
        this.messageId = messageId;
        this.newText = newText;
        this.editor = editor;
    }

    public String getRoomName() { return roomName; }
    public int getMessageId() { return messageId; }
    public String getNewText() { return newText; }
    public String getEditor() { return editor; }
}