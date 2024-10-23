package bg.sofia.uni.fmi.mjt.gameplatform.store;

public class PromoCode {
    private String code;
    private double discount;
    private boolean used = false;
    private int id;

    public PromoCode(int id, String code, double discount) {
        this.code = code;
        this.discount = discount;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getDiscount() {
        return discount;
    }

    public String getCode() {
        return code;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
