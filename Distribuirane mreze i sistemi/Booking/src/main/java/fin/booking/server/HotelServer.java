package fin.booking.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import fin.booking.notification.NotificationServer;
import java.io.IOException;

public class HotelServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        NotificationServer notifier = new NotificationServer(6000);
        notifier.start();

        Server server = ServerBuilder.forPort(50051)
            .addService(new HotelServiceImpl(notifier))
            .build();

        System.out.println("Hotel server started on port 50051...");
        server.start();
        server.awaitTermination();
    }
}
