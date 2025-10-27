package rs.raf.pds.v4.z5.messages;

import java.util.LinkedList;
import java.util.List;

public class ChatRoomHistory {
    private String roomName;
    private List<String> users; // username lista
    private LinkedList<ChatMessage> messages; // poslednjih 10 poruka

    protected ChatRoomHistory() {}

    public ChatRoomHistory(String roomName) {
        this.roomName = roomName;
        this.users = new LinkedList<>();
        this.messages = new LinkedList<>();
    }

    public String getRoomName() { return roomName; }

    public List<String> getUsers() { return users; }

    public void addUser(String userName) { users.add(userName); }

    public void removeUser(String userName) { users.remove(userName); }

    public void addMessage(ChatMessage msg) {
        messages.add(msg);
        if (messages.size() > 10) messages.removeFirst();
    }

    public List<ChatMessage> getLastMessages() { return messages; }
}