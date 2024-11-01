package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public sealed class Car extends Vehicle permits Caravan{

    private static final long pricePerSeat = 5;
    private FuelType fuelType;
    private int numberOfSeats;
    private double pricePerWeek;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model);

        if (numberOfSeats < 1 || numberOfSeats > 7) {
            throw new IllegalArgumentException("Invalid number of seats!");
        }

        setPricePerDay(pricePerDay);
        setPricePerHour(pricePerHour);

        checkPrice(pricePerWeek, "week");
        this.pricePerWeek = pricePerWeek;

        this.numberOfSeats = numberOfSeats;
        this.fuelType = fuelType;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {

        if (startOfRent.isAfter(endOfRent)) {
            throw new InvalidRentingPeriodException("Start time is after end time!");
        }

        long weeks = ChronoUnit.WEEKS.between(startOfRent, endOfRent);
        long days = ChronoUnit.DAYS.between(startOfRent, endOfRent) % 7;
        int hours = Math.abs(startOfRent.getHour() - endOfRent.getHour());
        int mins = Math.abs(startOfRent.getMinute() - endOfRent.getMinute());
        int secs = Math.abs(startOfRent.getSecond() - endOfRent.getSecond());

        if (mins > 0 || secs > 0) {
            hours += 1;
        }

        long additionalPrice = fuelType.getPricePerDay() * (weeks * 7 + days) + numberOfSeats * pricePerSeat;

        return weeks * pricePerWeek + days * getPricePerDay() + hours * getPricePerHour() + additionalPrice;
    }
}
