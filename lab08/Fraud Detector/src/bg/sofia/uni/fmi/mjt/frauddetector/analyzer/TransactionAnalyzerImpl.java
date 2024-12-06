package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private final List<Transaction> transactions;
    private final List<Rule> rules;
    private static final double EPSILON = 0.000000001;

    private void checkRules(List<Rule> rules) {
        if (Math.abs(rules.stream()
                .mapToDouble(Rule::weight)
                .sum() - 1) >= EPSILON) {
            throw new IllegalArgumentException("The summary weight of rules can not be different from 1!");
        }
    }

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        checkRules(rules);
        this.rules = rules;
        var buffReader = new BufferedReader(reader);
        transactions = buffReader.lines()
                .skip(1)
                .map(Transaction::of)
                .toList();
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::channel,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue)));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null) {
            throw new IllegalArgumentException("The accountID can not be null!");
        }

        if (accountID.isEmpty()) {
            throw new IllegalArgumentException("The accountID can not be empty!");
        }
        return transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountID))
                .mapToDouble(Transaction::transactionAmount)
                .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("The accountId can not be null!");
        }

        if (accountId.isEmpty()) {
            throw new IllegalArgumentException("The accountId can not be empty!");
        }

        return transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountId))
                .toList();
    }

    @Override
    public double accountRating(String accountId) {
        List<Transaction> accountTransactions = transactions.stream()
                .filter(transaction -> transaction.accountID().equals(accountId))
                .toList();

        double result = 0;
        for (Rule rule : rules) {
            if (rule.applicable(accountTransactions)) {
                result += rule.weight();
            }
        }

        DecimalFormat df = new DecimalFormat("#.##");
        String formattedResult = df.format(result);

        return Double.parseDouble(formattedResult);
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::accountID))
                .keySet()
                .stream()
                .collect(Collectors.toMap(account -> account,
                        this::accountRating,
                        (oldValue, newValue) -> oldValue,
                        TreeMap::new));
    }
}
