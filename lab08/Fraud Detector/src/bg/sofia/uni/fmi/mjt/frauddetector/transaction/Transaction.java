package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID,
                          double transactionAmount, LocalDateTime transactionDate,
                          String location, Channel channel) {

    private static final String LINE_SEPARATOR = ",";
    private static final int NUMBER_OF_ARGUMENTS = 6;
    private static final int TRANSACTION_ID_FIELD = 0;
    private static final int ACCOUNT_ID_FIELD = 1;
    private static final int TRANSACTION_AMOUNT_FIELD = 2;
    private static final int DATE_FIELD = 3;
    private static final int LOCATION_FIELD = 4;
    private static final int CHANNEL_FIELD = 5;

    public static Transaction of(String line) {
        final String[] tokens = line.split(LINE_SEPARATOR);

        if (tokens.length != NUMBER_OF_ARGUMENTS) {
            throw new IllegalArgumentException("Field number mismatch!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new Transaction(tokens[TRANSACTION_ID_FIELD],
                tokens[ACCOUNT_ID_FIELD],
                Double.parseDouble(tokens[TRANSACTION_AMOUNT_FIELD]),
                LocalDateTime.parse(tokens[DATE_FIELD], formatter),
                tokens[LOCATION_FIELD],
                Channel.valueOf(tokens[CHANNEL_FIELD].toUpperCase()));
    }
}
