package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationsRuleTest {

    private List<Transaction> transactionList;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse("2023-06-27 16:44:19", formatter);
        transactionList = List.of(new Transaction("TX000002","AC00455",
                        376.24,dateTime1,"Houston", Channel.ATM),
                new Transaction("TX000004","AC00455",
                        376.24,dateTime1,"Houston",Channel.ATM));
    }

    @Test
    void testApplicableTrue() {
        Rule rule = new LocationsRule(1, 0.2);
        assertTrue(rule.applicable(transactionList),
                "Must return true, but returns false instead!");
    }

    @Test
    void testApplicableFalse() {
        Rule rule = new LocationsRule(2, 0.2);
        assertFalse(rule.applicable(transactionList),
                "Must return false, but returns true instead!");
    }
}
