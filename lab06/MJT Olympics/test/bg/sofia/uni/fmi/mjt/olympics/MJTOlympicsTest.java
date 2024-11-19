package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MJTOlympicsTest {

    private CompetitionResultFetcher resultFetcher;
    private Competitor comp1;
    private Competitor comp2;
    private Competitor comp3;
    private Set<Competitor> competitors;
    private Olympics olympics;
    private Competition competition;

    @BeforeEach
    void setUp() {
        resultFetcher = Mockito.mock(CompetitionResultFetcher.class);
        comp1 = new Athlete("one", "Karlos Nasar", "BG");
        comp2 = new Athlete("two", "Stefanos Tsitsipas", "GR");
        comp3 = new Athlete("three", "Random Name", "BG");
        competitors = new LinkedHashSet<>();
        competitors.add(comp1);
        competitors.add(comp2);
        competitors.add(comp3);
        olympics = new MJTOlympics(competitors, resultFetcher);
        competition = new Competition("Name", "Discipline", competitors);
    }

    @Test
    void testUpdateMedalStatisticsWithValidData() {
        TreeSet<Competitor> sortedComps = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        sortedComps.addAll(competitors);

        when(resultFetcher.getResult(competition)).thenReturn(sortedComps);
        olympics.updateMedalStatistics(competition);

        assertTrue(olympics.getNationsMedalTable().containsKey(comp1.getNationality()), "");
        assertTrue(olympics.getNationsMedalTable().get(comp1.getNationality()).containsKey(Medal.values()[0]), "vtoriq");
        assertEquals(olympics.getNationsMedalTable().get(comp1.getNationality()).get(Medal.values()[0]), 1, "tretiq");
    }

    @Test
    void testUpdateMedalStatisticsWithNotRegisteredCompetitor () {
        Set<Competitor> competitorSet = new HashSet<>(competition.competitors());
        competitorSet.add(new Athlete("Indetifier", "Name", "MD"));
        Competition comp = new Competition("New Comp", "Discipline", competitorSet);
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(comp));
    }
    @Test
    void testUpdateMedalStatisticsWithNullCompetition() {
        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(null));
    }

    @Test
    void testGetTotalMedalsWhenExistingNationality() {
        TreeSet<Competitor> sortedComps = new TreeSet<>(new Comparator<Competitor>() {
            @Override
            public int compare(Competitor o1, Competitor o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        sortedComps.addAll(competitors);
        when(resultFetcher.getResult(competition)).thenReturn(sortedComps);
        olympics.updateMedalStatistics(competition);

        assertEquals(2, olympics.getTotalMedals("BG"));
    }

    @Test
    void testGetTotalMedalsWhenNonExistentNationality() {
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals("NG"));
    }

    @Test
    void testGetTotalMedalsWhenNullNationality() {
        assertThrows(IllegalArgumentException.class, () -> olympics.getTotalMedals(null));
    }

    @Test
    void testGetTotalMedalsWhenNoMedals () {
        Map<String, EnumMap<Medal, Integer>> medalTable = new HashMap<>();
        medalTable.put("BG", new EnumMap<>(Medal.class));
        olympics.getNationsMedalTable().putAll(medalTable);

        assertEquals(olympics.getTotalMedals("BG"), 0);
    }

    @Test
    void testGetTotalMedalsWhenNullMedals () {
        Map<String, EnumMap<Medal, Integer>> medalTable = new HashMap<>();
        medalTable.put("BG", null);
        olympics.getNationsMedalTable().putAll(medalTable);
        assertEquals(olympics.getTotalMedals("BG"), 0);
    }

    @Test
    void testGetNationsRankListWithDifferentMedalsCount() {
        Map<String, EnumMap<Medal, Integer>> medalTable = new HashMap<>();
        EnumMap<Medal, Integer> enumMap = new EnumMap<>(Medal.class);
        enumMap.put(Medal.GOLD, 2);
        medalTable.putIfAbsent("BG", enumMap);
        EnumMap<Medal, Integer> enumMap1 = new EnumMap<>(Medal.class);
        enumMap1.put(Medal.BRONZE, 3);
        medalTable.putIfAbsent("GR", enumMap1);
        EnumMap<Medal, Integer> enumMap2 = new EnumMap<>(Medal.class);
        enumMap2.put(Medal.SILVER, 1);
        medalTable.putIfAbsent("MD", enumMap2);

        olympics.getNationsMedalTable().clear();
        olympics.getNationsMedalTable().putAll(medalTable);

        List<String> stringList = new LinkedList<>();
        stringList.add("GR");
        stringList.add("BG");
        stringList.add("MD");

        assertIterableEquals(olympics.getNationsRankList(), stringList);
    }

    @Test
    void testGetNationsRankListWithEquivalentMedalsCount() {
        Map<String, EnumMap<Medal, Integer>> medalTable = new HashMap<>();
        EnumMap<Medal, Integer> enumMap = new EnumMap<>(Medal.class);
        enumMap.put(Medal.GOLD, 2);
        medalTable.putIfAbsent("BG", enumMap);
        medalTable.putIfAbsent("GR", enumMap);
        medalTable.putIfAbsent("MD", enumMap);

        olympics.getNationsMedalTable().clear();
        olympics.getNationsMedalTable().putAll(medalTable);

        List<String> stringList = new LinkedList<>();
        stringList.add("BG");
        stringList.add("GR");
        stringList.add("MD");

        assertIterableEquals(olympics.getNationsRankList(), stringList);
    }
}
