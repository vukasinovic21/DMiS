package fin.booking.client;

import fin.booking.grpc.HotelList;
import fin.booking.grpc.HotelRequest;
import fin.booking.grpc.HotelServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.Socket;

public class HotelClient {
    public static void main(String[] args) {

        new Thread(() -> {
            try (Socket socket = new Socket("localhost", 6000);
                 var in = new java.util.Scanner(socket.getInputStream())) {
                while (in.hasNextLine()) {
                    System.out.println("INFO: " + in.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build();

        HotelServiceGrpc.HotelServiceBlockingStub stub = HotelServiceGrpc.newBlockingStub(channel);

        HotelRequest request = HotelRequest.newBuilder()
            .setCity("Budapest")
            .setMinStars(3)
            .setMaxDistance(3.0)
            .build();

        HotelList hotels = stub.getAvailableHotels(request);

        if (hotels.getHotelsList().isEmpty()) {
            System.out.println("No available hotels found for your criteria.");
        } else {
            hotels.getHotelsList().forEach(h ->
                System.out.println(h.getName() + " " + h.getStars() + "* " + h.getDistance() + "km " + h.getPrice() + "$ " + h.getFreeRooms()));
        }

        channel.shutdown();
    }
}