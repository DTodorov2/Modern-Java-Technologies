package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class Bicycle extends Vehicle  {

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour){
        super(id, model);

        checkPrice(pricePerDay, "day");
        checkPrice(pricePerHour, "hour");

        setPricePerDay(pricePerDay);
        setPricePerHour(pricePerHour);
    }

    @Override
    public int getDriverTaxes() {
        return 0;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {

        if (startOfRent.isAfter(endOfRent)) {
            throw new InvalidRentingPeriodException("Start time is after end time!");
        }

        if (ChronoUnit.WEEKS.between(startOfRent, endOfRent) > 0) {
            throw new InvalidRentingPeriodException("Bicycle cannot be rented for more than 6 days 23 hours 59 mins and 59 secs!");
        }

        long days = ChronoUnit.DAYS.between(startOfRent, endOfRent);
        int hours = Math.abs(startOfRent.getHour() - endOfRent.getHour());
        int mins = Math.abs(startOfRent.getMinute() - endOfRent.getMinute());
        int secs = Math.abs(startOfRent.getSecond() - endOfRent.getSecond());

        if (mins > 0 || secs > 0) {
            hours += 1;
        }

        return days * getPricePerDay() + hours * getPricePerHour();
    }
}
