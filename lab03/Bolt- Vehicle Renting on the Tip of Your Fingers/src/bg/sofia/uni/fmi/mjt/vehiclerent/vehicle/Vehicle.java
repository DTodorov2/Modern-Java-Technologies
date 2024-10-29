package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract sealed class Vehicle permits Bicycle, Car {

    private String id;
    private String model;
    private boolean isRented;
    LocalDateTime startRentalTime;
    Driver driver;

    public Vehicle(String id, String model) {
        if (id == null) {
            throw new IllegalArgumentException("The id should not be null!");
        }

        if (model == null) {
            throw new IllegalArgumentException("The model should not be null!");
        }

        this.id = id;
        this.model = model;
        isRented = false;
    }

    public int getDriverTaxes() {
        return getDriver().getAgeGroup().getTaxes();
    }

    public Driver getDriver () {
        return driver;
    }

    public boolean isRented() {
        return isRented;
    }

    public LocalDateTime getStartRentalTime() {
        return startRentalTime;
    }

    public void setDriver(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver should not be null!");
        }

        this.driver = driver;
    }

    public void setStartRentalTime(LocalDateTime startRentalTime) {
        if (startRentalTime != null) {
            this.startRentalTime = startRentalTime;
        }
    }

    void checkPrice(double price, String period) {
        if (price < 0) {
            throw new IllegalArgumentException("Invalid price per" + period + "!");
        }
    }

    public void rent(Driver driver, LocalDateTime startRentTime) {
        if (isRented) {
            throw new VehicleAlreadyRentedException("The vehicle is already rented!");
        }
        if (driver == null || startRentTime == null) {
            throw new IllegalArgumentException("Arguments should not be null!");
        }

        setDriver(driver);
        setStartRentalTime(startRentTime);
        isRented = true;
    }

    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if (rentalEnd.isBefore(startRentalTime)) {
            throw new InvalidRentingPeriodException("The end time is before the start time!");
        }

        if (!isRented) {
            throw new VehicleNotRentedException("The vehicle has not been rented yet!");
        }

        isRented = false;
    }

    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;
}
