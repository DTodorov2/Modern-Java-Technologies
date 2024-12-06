package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZScoreRuleTest {

    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        Transaction t1 = new Transaction("TX001", "AC001", 100.0,
                LocalDateTime.now(), "Sofia", Channel.ONLINE);
        Transaction t2 = new Transaction("TX002", "AC001", 110.0,
                LocalDateTime.now(), "Sofia", Channel.ATM);
        Transaction t3 = new Transaction("TX003", "AC001", 105.0,
                LocalDateTime.now(), "Sofia", Channel.BRANCH);

        transactions = new LinkedList<>();
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
    }

    @Test
    void testApplicableTrue() {
        Transaction t4 = new Transaction("TX004", "AC001", 1000.0,
                LocalDateTime.now(), "Sofia", Channel.ATM);
        transactions.add(t4);

        ZScoreRule zScoreRule = new ZScoreRule(1.5, 0.5);
        assertTrue(zScoreRule.applicable(transactions), "True must be returned but false is returned instead");
    }

    @Test
    void testApplicableFalse() {
        ZScoreRule zScoreRule = new ZScoreRule(1.5, 0.5);
        assertFalse(zScoreRule.applicable(transactions), "True must be returned false but true is returned instead");
    }
}
