package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule {

    private final double zScoreThreshold;
    private final double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double ex = transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .sum() / transactions.size();

        double dx = transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .map(amount -> amount - ex)
                .map(amount -> amount * amount)
                .sum() / transactions.size();

        double standardDeviation = Math.sqrt(dx);

        return transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .map(amount -> amount - ex)
                .map(amount -> amount / standardDeviation)
                .anyMatch(amount -> amount > zScoreThreshold);
    }

    @Override
    public double weight() {
        return weight;
    }
}
