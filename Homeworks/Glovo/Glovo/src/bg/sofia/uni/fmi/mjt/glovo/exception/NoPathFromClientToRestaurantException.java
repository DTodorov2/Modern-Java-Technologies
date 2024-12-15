package bg.sofia.uni.fmi.mjt.glovo.exception;

public class NoPathFromClientToRestaurantException extends RuntimeException {
    public NoPathFromClientToRestaurantException(String message) {
        super(message);
    }

    public NoPathFromClientToRestaurantException(String message, Throwable cause) {
        super(message, cause);
    }
}
