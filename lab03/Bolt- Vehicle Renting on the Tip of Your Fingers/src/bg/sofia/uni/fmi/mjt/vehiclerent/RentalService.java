package bg.sofia.uni.fmi.mjt.vehiclerent;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.vehicle.Vehicle;

import java.time.LocalDateTime;

public class RentalService {

    public void rentVehicle(Driver driver, Vehicle vehicle, LocalDateTime startOfRent) {
        if (driver == null || vehicle == null || startOfRent == null) {
            throw new IllegalArgumentException("One of the arguments is null!");
        }

        if (vehicle.isRented()) {
            throw new VehicleAlreadyRentedException("The vehicle is already rented!");
        }

        vehicle.rent(driver, startOfRent);
    }

    public double returnVehicle(Vehicle vehicle, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        if (vehicle == null || endOfRent == null) {
            throw new IllegalArgumentException("One of the arguments is null!");
        }

        if (!vehicle.isRented()) {
            throw new VehicleNotRentedException("This vehicle has not been rented yet!");
        }

        vehicle.returnBack(endOfRent);
        return vehicle.calculateRentalPrice(vehicle.getStartRentalTime(), endOfRent) + vehicle.getDriverTaxes();
    }
}
