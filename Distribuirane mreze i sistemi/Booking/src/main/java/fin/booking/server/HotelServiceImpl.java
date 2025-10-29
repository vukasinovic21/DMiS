package fin.booking.server;

import fin.booking.grpc.HotelInfo;
import fin.booking.grpc.HotelList;
import fin.booking.grpc.HotelRequest;
import fin.booking.grpc.HotelServiceGrpc;
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
        hotels.add(new Hotel("Hotel3 Ibis", 3, "Budapest", 2.5, 50.0, 10));
        hotels.add(new Hotel("Hotel4 Sheraton", 4, "Budapest", 1.0, 60.0, 4));
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
}
