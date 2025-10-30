package fin.booking.client;

import fin.booking.grpc.HotelList;
import fin.booking.grpc.HotelRequest;
import fin.booking.grpc.HotelServiceGrpc;
import fin.booking.grpc.ReservationRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.Socket;
import java.util.Random;

public class HotelClient {
    public static void main(String[] args) {

        String clientName;
        String city;
        int minStars;
        double maxDistance;
        int numberOfDays;

        if (args.length >= 5) {
            clientName = args[0];
            city = args[1];
            minStars = Integer.parseInt(args[2]);
            maxDistance = Double.parseDouble(args[3]);
            numberOfDays = Integer.parseInt(args[4]);
        } else {
            clientName = "Client-" + new Random().nextInt(1000);
            city = "Budapest";
            minStars = 3;
            maxDistance = 3.0;
            numberOfDays = 1 + new Random().nextInt(3);
        }

        System.out.println("Starting client: " + clientName);

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
            .setCity(city)
            .setMinStars(minStars)
            .setMaxDistance(maxDistance)
            .build();

        HotelList hotels = stub.getAvailableHotels(request);

        if (hotels.getHotelsList().isEmpty()) {
            System.out.println("No available hotels found for your criteria.");
        } else {
            hotels.getHotelsList().forEach(h ->
                System.out.println("Available: " + h.getName() + " " + h.getStars() + "* " + h.getDistance() + "km " + h.getPrice() + "$ " + h.getFreeRooms()));
        }

        if (!hotels.getHotelsList().isEmpty()) {
            String hotelName = hotels.getHotelsList().getFirst().getName();

            var reservationRequest = ReservationRequest.newBuilder()
                    .setHotelName(hotelName)
                    .setClientName(clientName)
                    .setStartDate("2025-29-10")
                    .setNights(numberOfDays)
                    .build();

            var reservationResponse = stub.makeReservation(reservationRequest);

            System.out.println("Reservation result: " + reservationResponse.getMessage());
        }

        channel.shutdown();
    }
}