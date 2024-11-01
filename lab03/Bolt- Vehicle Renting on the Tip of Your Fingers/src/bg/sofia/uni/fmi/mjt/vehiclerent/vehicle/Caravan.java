package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class Caravan extends Car {
    private int numberOfBeds;
    private static final long priceForBed = 10;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id, model, fuelType, numberOfSeats, pricePerWeek, pricePerDay, pricePerHour);
        this.numberOfBeds = numberOfBeds;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {

        if (startOfRent.isAfter(endOfRent)) {
            throw new InvalidRentingPeriodException("Start time is after end time!");
        }

        if (ChronoUnit.HOURS.between(startOfRent, endOfRent) < 24) {
            throw new InvalidRentingPeriodException("You can rent a caravan for at least 24 hours!");
        }

        return super.calculateRentalPrice(startOfRent, endOfRent) + numberOfBeds * priceForBed;
    }
}
