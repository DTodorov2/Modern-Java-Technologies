package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.comparator.NationMedalComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NationMedalComparatorTest {

    @Test
    void testCompareWithDifferentTotalMedalsDesc() {
        MJTOlympics olympics = Mockito.mock(MJTOlympics.class);
        when(olympics.getTotalMedals("BG")).thenReturn(10);
        when(olympics.getTotalMedals("GR")).thenReturn(5);

        NationMedalComparator comparator = new NationMedalComparator(olympics);

        assertTrue(comparator.compare("BG", "GR") < 0);
    }

    @Test
    void testCompareWithEquivalentTotalMedalsAlphabetically() {
        MJTOlympics olympics = Mockito.mock(MJTOlympics.class);
        when(olympics.getTotalMedals("BG")).thenReturn(5);
        when(olympics.getTotalMedals("GR")).thenReturn(5);

        NationMedalComparator comparator = new NationMedalComparator(olympics);

        assertTrue(comparator.compare("BG", "GR") < 0);
    }
}
