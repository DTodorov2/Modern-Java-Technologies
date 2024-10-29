package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
    JUNIOR(10),
    EXPERIENCED(0),
    SENIOR(15);

    private final int taxes;

    AgeGroup(int taxes) {
        this.taxes = taxes;
    }

    public int getTaxes() {
        return taxes;
    }
}
