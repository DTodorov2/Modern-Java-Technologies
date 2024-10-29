package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public class Driver {
    AgeGroup ageGroup;

    public Driver(AgeGroup group) {
        ageGroup = group;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }
}
