package fin.booking.notification;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NotificationServer {

    private final Set<PrintWriter> clients = Collections.synchronizedSet(new HashSet<>());
    private final int port;

    public NotificationServer(int port) {
        this.port = port;
    }

    public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Notification server started on port " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    clients.add(out);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void publish(String message) {
        synchronized (clients) {
            for (PrintWriter out : clients) {
                out.println(message);
            }
        }
    }
}