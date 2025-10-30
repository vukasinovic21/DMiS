package fin.booking.server;

import fin.booking.grpc.*;
import fin.booking.model.Hotel;
import fin.booking.notification.NotificationServer;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class HotelServiceImpl extends HotelServiceGrpc.HotelServiceImplBase {

    private final NotificationServer notifier;
    private final List<Hotel> hotels = new ArrayList<>();

    public HotelServiceImpl(NotificationServer notifier) {
        this.notifier = notifier;

        hotels.add(new Hotel("Hotel1 Kg", 4, "Kragujevac", 1.2, 80.0, 5));
        hotels.add(new Hotel("Hotel2 Bg", 5, "Belgrade", 0.8, 100.0, 3));
        hotels.add(new Hotel("Hotel3 Ibis", 3, "Budapest", 2.5, 50.0, 4));
        hotels.add(new Hotel("Hotel4 Sheraton", 4, "Budapest", 1.0, 60.0, 4));
        hotels.add(new Hotel("Hotel5 Stazione Centrale", 3, "Budapest", 1.6, 80.0, 2));
    }

    @Override
    public void getAvailableHotels(HotelRequest request, StreamObserver<HotelList> responseObserver) {
        List<HotelInfo> matched = new ArrayList<>();

        for (Hotel h : hotels) {
            if (h.getCity().equalsIgnoreCase(request.getCity())
                && h.getStars() >= request.getMinStars()
                && h.getDistanceFromCenter() <= request.getMaxDistance()
                && h.getFreeRooms() > 0) {

                matched.add(HotelInfo.newBuilder()
                    .setName(h.getName())
                    .setStars(h.getStars())
                    .setCity(h.getCity())
                    .setDistance(h.getDistanceFromCenter())
                    .setPrice(h.getPrice())
                    .setFreeRooms(h.getFreeRooms())
                    .build());
            }
        }

        for (HotelInfo hi : matched) {
            notifier.publish("Hotel update: " + hi.getName() + " - free rooms: " + hi.getFreeRooms() + ", price: " + hi.getPrice());
        }

        HotelList response = HotelList.newBuilder()
            .addAllHotels(matched)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void makeReservation(ReservationRequest request, StreamObserver<ReservationResponse> responseObserver) {
        Hotel hotel = hotels.stream()
            .filter(h -> h.getName().equalsIgnoreCase(request.getHotelName()))
            .findFirst()
            .orElse(null);

        ReservationResponse.Builder response = ReservationResponse.newBuilder();

        if (hotel == null) {
            response.setSuccess(false)
                .setMessage("Hotel not found");
        } else if (hotel.getFreeRooms() <= 0) {
            response.setSuccess(false)
                .setMessage("No free rooms available");
        } else {
            hotel.setFreeRooms(hotel.getFreeRooms() - 1);
            adjustPrice(hotel);
            notifier.publish("Hotel update: " + hotel.getName() + " - free rooms: " + hotel.getFreeRooms());

            response.setSuccess(true)
                .setMessage("Reservation successful for " + request.getClientName());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private void adjustPrice(Hotel h) {
        if (h.getFreeRooms() <= 3 && !h.isHigherPrice()) {
            h.setPrice(h.getBasePrice() * 1.2);
            h.setHigherPrice(true);
            notifier.publish("Price increased for " + h.getName() + ": " + h.getPrice());
        } else if (h.getFreeRooms() > 8 && !h.isLowerPrice()) {
            h.setPrice(h.getBasePrice() * 0.8);
            h.setLowerPrice(true);
            notifier.publish("Price decreased for " + h.getName() + ": " + h.getPrice());
        } else if(h.getFreeRooms() > 3 && h.getFreeRooms() <=  8 && (h.isLowerPrice() || h.isHigherPrice())){
            h.setPrice(h.getBasePrice());
            h.setLowerPrice(false);
            h.setHigherPrice(false);
        }
    }
}
