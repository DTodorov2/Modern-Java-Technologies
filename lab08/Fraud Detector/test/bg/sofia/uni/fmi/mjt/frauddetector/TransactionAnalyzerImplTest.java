package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzer;
import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerImpl;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionAnalyzerImplTest {

    Reader stringReader;
    TransactionAnalyzer transactionAnalyzer;
    List<Rule> rules;

    @BeforeEach
    void setUp() {
        rules = List.of(
                new LocationsRule(2, 0.7),
                new FrequencyRule(2, Period.ofWeeks(4), 0.25),
                new SmallTransactionsRule(1, 10.20, 0.05)
        );

    }

    @Test
    void testAllAccountIDsWithDifferentIDs() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00128,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,Branch"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator();
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);

        List<String> accountIDs = List.of("AC00128", "AC00455", "AC00019");
        List<String> list = transactionAnalyzer.allAccountIDs();
        assertIterableEquals(accountIDs, list,
                "All accountIDs have to be in the list but are not!");
    }

    @Test
    void testAllAccountIDsWithNonUniqueIDs() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,Branch"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator();
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        List<String> accountIDs = List.of("AC00455", "AC00019");
        List<String> list = transactionAnalyzer.allAccountIDs();
        assertIterableEquals(accountIDs, list,
                "The accountIDs have to be unique but are not!");
    }

    @Test
    void testTransactionCountByChannelWithDifferentChannels() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,Branch"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator();
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);

        Map<Channel, Integer> channelIntegerMap = Map.of(Channel.ATM, 1, Channel.BRANCH, 1, Channel.ONLINE, 1);
        assertEquals(channelIntegerMap, transactionAnalyzer.transactionCountByChannel(),
                "Every Channel must have 1 instance but it is not true!");
    }

    @Test
    void testTransactionCountByChannelWithNonUniqueChannels() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator();
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);

        Map<Channel, Integer> channelIntegerMap = Map.of(Channel.ATM, 2, Channel.ONLINE, 1);
        assertEquals(channelIntegerMap, transactionAnalyzer.transactionCountByChannel(),
                "ATM must have 2 instances and ONLINE 1, but it is not true!");
    }

    @Test
    void testAmountSpentByUserWithNullAccountId() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator();
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.amountSpentByUser(null),
                "IllegalArgumentException must be thrown when the accountID is null!");
    }

    @Test
    void testAmountSpentByUserWithEmptyAccountId() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator();
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        String accountID = "";
        assertThrows(IllegalArgumentException.class, () -> transactionAnalyzer.amountSpentByUser(accountID),
                "IllegalArgumentException must be thrown when the accountID is null!");
    }

    @Test
    void testAmountSpentByUserWithValidAccountID() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00123,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator() + "TX000004,AC00455,245.07,2023-06-27 16:44:19,Houston,ATM";
        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        String accountID = "AC00455";
        assertEquals(621.31, transactionAnalyzer.amountSpentByUser(accountID),
                "The amount must be the sum of all transactions but is not!");
    }

    @Test
    void testAllTransactionsByUserWithValidInput() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00123,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator() + "TX000004,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM";

        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        String accountID = "AC00455";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse("2023-06-27 16:44:19", formatter);
        List<Transaction> transactionList = List.of(new Transaction("TX000002","AC00455",
                376.24,dateTime1,"Houston",Channel.ATM),
                new Transaction("TX000004","AC00455",
                        376.24,dateTime1,"Houston",Channel.ATM));

        assertIterableEquals(transactionList, transactionAnalyzer.allTransactionsByUser(accountID),
                "The transactions must be the same but are not!");
    }

    @Test
    void testAllTransactionsByUserWithNullAccountID() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00123,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator() + "TX000004,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM";

        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        assertThrows(IllegalArgumentException.class,() -> transactionAnalyzer.allTransactionsByUser(null),
                "IllegalArgumentException must be thrown when null accountID is passed but is not!");
    }

    @Test
    void testAllTransactionsByUserWithEmptyAccountID() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00123,14.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online"
                + System.lineSeparator() + "TX000004,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM";

        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        String accountID = "";
        assertThrows(IllegalArgumentException.class,() -> transactionAnalyzer.allTransactionsByUser(accountID),
                "IllegalArgumentException must be thrown when empty accountID is passed but is not!");
    }

    @Test
    void testAccountRating() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,9.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00455,126.29,2023-09-10 18:16:08,Mesa,Online";

        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        assertEquals(0.75, transactionAnalyzer.accountRating("AC00455"));
    }

    @Test
    void testAccountsRisk() {
        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,9.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00009,1.01,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00455,126.29,2023-09-10 18:16:08,Mesa,Online";

        stringReader = new StringReader(testData);
        transactionAnalyzer = new TransactionAnalyzerImpl(stringReader, rules);
        SortedMap<String, Double> sortedMap = new TreeMap<>();
        sortedMap.put("AC00009", 0.05);
        sortedMap.put("AC00455", 0.75);
        assertEquals(sortedMap, transactionAnalyzer.accountsRisk(),
                "The maps must be equal but are not!");
    }

    @Test
    void testTransactionAnalyzerImplWithRulesWithSumWeightDifferentThan1() {
        rules = List.of(
                new LocationsRule(2, 0.8),
                new FrequencyRule(2, Period.ofWeeks(4), 0.25),
                new SmallTransactionsRule(1, 10.20, 0.05)
        );

        String testData = "TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel"
                + System.lineSeparator() + "TX000001,AC00455,9.09,2023-04-11 16:29:14,San Diego,ATM"
                + System.lineSeparator() + "TX000002,AC00009,1.01,2023-06-27 16:44:19,Houston,ATM"
                + System.lineSeparator() + "TX000003,AC00455,126.29,2023-09-10 18:16:08,Mesa,Online";

        stringReader = new StringReader(testData);

        assertThrows(IllegalArgumentException.class, () -> new TransactionAnalyzerImpl(stringReader, rules),
                "The summary weight of the rules should not be different from 1!");
    }

    @AfterEach
    void tearDown() throws IOException {
        stringReader.close();
    }

}
