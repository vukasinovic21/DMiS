package fin.booking.client;

import fin.booking.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.Random;

public class AutoHotelClients {

    public static void main(String[] args) {
        int numberOfClients = 5;
        for (int i = 1; i <= numberOfClients; i++) {
            int clientId = i;
            new Thread(() -> {
                Random rand = new Random();
                String clientName = "AutoClient-" + clientId;
                List<String> cities = List.of("Belgrade", "Budapest", "Kragujevac");
                String city = cities.get(rand.nextInt(cities.size()));
                int minStars = 3 + rand.nextInt(3);
                double maxDistance = 1 + rand.nextDouble() * 3;
                int nights = 1 + rand.nextInt(5);

                try {
                    Thread.sleep(rand.nextInt(3000));

                    System.out.println(clientName + " starting search in " + city);

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
                        System.out.println(clientName + ": No available hotels found in " + city);
                        channel.shutdown();
                        return;
                    }

                    var chosenHotel = hotels.getHotelsList().get(rand.nextInt(hotels.getHotelsCount()));

                    System.out.println(clientName + " attempting reservation at " + chosenHotel.getName());

                    ReservationRequest reservationRequest = ReservationRequest.newBuilder()
                            .setHotelName(chosenHotel.getName())
                            .setClientName(clientName)
                            .setStartDate("2025-11-01")
                            .setNights(nights)
                            .build();

                    ReservationResponse response = stub.makeReservation(reservationRequest);
                    System.out.println(clientName + ": " + response.getMessage());

                    Thread.sleep((1 + rand.nextInt(5)) * 2000L);

                    CancelRequest cancelRequest = CancelRequest.newBuilder()
                            .setClientName(clientName)
                            .setHotelName(chosenHotel.getName())
                            .build();

                    CancelResponse cancelResponse = stub.cancelReservation(cancelRequest);
                    System.out.println(clientName + ": " + cancelResponse.getMessage());

                    channel.shutdown();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}