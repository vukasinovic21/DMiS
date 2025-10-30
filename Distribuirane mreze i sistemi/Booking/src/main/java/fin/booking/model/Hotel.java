package fin.booking.model;

public class Hotel {

    private String name;
    private int stars;
    private String city;
    private double distanceFromCenter;
    private double price;
    private int freeRooms;
    private double basePrice;
    private boolean lowerPrice = false;
    private boolean higherPrice = false;
    private double profit = 0.0;

    public Hotel() {
    }

    public Hotel(String name, int stars, String city, double distanceFromCenter, double price, int freeRooms) {
        this.name = name;
        this.stars = stars;
        this.city = city;
        this.distanceFromCenter = distanceFromCenter;
        this.price = price;
        this.freeRooms = freeRooms;
        this.basePrice = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getDistanceFromCenter() {
        return distanceFromCenter;
    }

    public void setDistanceFromCenter(double distanceFromCenter) {
        this.distanceFromCenter = distanceFromCenter;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getFreeRooms() {
        return freeRooms;
    }

    public void setFreeRooms(int freeRooms) {
        this.freeRooms = freeRooms;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(boolean lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public boolean isHigherPrice() {
        return higherPrice;
    }

    public void setHigherPrice(boolean higherPrice) {
        this.higherPrice = higherPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
