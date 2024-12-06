package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SmallTransactionsRuleTest {

    List<Transaction> transactionList;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse("2023-06-27 16:44:19", formatter);
        transactionList = List.of(new Transaction("TX000002","AC00455",
                        10.01,dateTime1,"Houston", Channel.ATM),
                new Transaction("TX000004","AC00455",
                        10,dateTime1,"Houston",Channel.ATM));
    }

    @Test
    void testApplicableTrue() {
        Rule rule = new SmallTransactionsRule(2, 10.5, 0.4);
        assertTrue(rule.applicable(transactionList),
                "Must return true but returns false instead!");
    }

    @Test
    void testApplicableFalse() {
        Rule rule = new SmallTransactionsRule(3, 10.5, 0.4);
        assertFalse(rule.applicable(transactionList),
                "Must return false but returns true instead!");
    }
}
