package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Comparator;
import java.util.List;

public class FrequencyRule implements Rule {

    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;
    private final double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        List<Transaction> sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::transactionDate))
                .toList();

        for (Transaction transaction : sortedTransactions) {
            LocalDateTime startTime = transaction.transactionDate();
            LocalDateTime endTime = startTime.plus(timeWindow);

            long transactionsInTimeWindow = transactions.stream()
                    .filter(currTransaction -> !currTransaction.transactionDate().isBefore(startTime) &&
                            !currTransaction.transactionDate().isAfter(endTime))
                    .count();

            if (transactionsInTimeWindow >= transactionCountThreshold) {
                return true;
            }
        }

        return false;
    }

    @Override
    public double weight() {
        return weight;
    }
}
