package bg.sofia.uni.fmi.mjt.glovo.exception;

public class IncorrectLimitationValue extends RuntimeException {
    public IncorrectLimitationValue(String message) {
        super(message);
    }

    public IncorrectLimitationValue(String message, Throwable cause) {
        super(message, cause);
    }
}
