package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrequencyRuleTest {

    private List<Transaction> transactionList;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2023-06-27 16:44:19", formatter);
        transactionList = List.of(new Transaction("TX000002","AC00455",
                        376.24,dateTime,"Houston", Channel.ATM),
                new Transaction("TX000004","AC00455",
                        376.24,dateTime,"Houston",Channel.ATM));
    }

    @Test
    void testApplicableTrue() {
        Rule rule = new FrequencyRule(2, Period.ofWeeks(4), 0.25);
        assertTrue(rule.applicable(transactionList),
                "Must return true, but returns false instead!");
    }

    @Test
    void testApplicableFalse() {
        Rule rule = new FrequencyRule(3, Period.ofWeeks(4), 0.25);
        assertFalse(rule.applicable(transactionList),
                "Must return false, but returns true instead!");
    }
}
