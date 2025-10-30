package fin.booking.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import fin.booking.notification.NotificationServer;
import java.io.IOException;

public class HotelServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        NotificationServer notifier = new NotificationServer(6000);
        notifier.start();

        HotelServiceImpl hotelService = new HotelServiceImpl(notifier);

        Server server = ServerBuilder.forPort(50051)
            .addService(hotelService)
            .build();

        System.out.println("Hotel server started on port 50051...");
        server.start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    System.out.println("\nHotel updates:");
                    hotelService.getHotels().forEach(h -> {
                        System.out.println("INFO: " + h.getName() + ", free rooms: " + h.getFreeRooms() + " price: " + h.getPrice() + "$");
                    });
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        server.awaitTermination();
    }
}
