package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AthleteTest {

    private Athlete atl;
    private static final String name = "name";
    private static final String id = "identifier";
    private static final String nation = "nationality";

    @BeforeEach
    void setUp() {
        atl = new Athlete(id, name, nation);
    }

    @Test
    void testGetMedalsOfNewAthlete() {
        assertEquals(atl.getMedals().size(), 0,
                "No medals are expected when a new athlete is added");
    }

    @Test
    void testAddMedalWithNullReference() {
        assertThrows(IllegalArgumentException.class, () -> atl.addMedal(null),
        "Expected addMedal to throw IllegalArgumentException when medal is null!");
    }

    @Test
    void testAddMedalWithValidMedal() {
        atl.addMedal(Medal.BRONZE);
        assertTrue(atl.getMedals().contains(Medal.BRONZE),
                "Expected the medal to be added to the collection but it is not");
    }

    @Test
    void testAddGoldMedal() {
        atl.addMedal(Medal.values()[0]);
        assertEquals(atl.getMedals().getFirst(), Medal.GOLD,
                "Expected gold medal to be added to the collection but it is not");
    }

    @Test
    void testAddBronzeMedal() {
        atl.addMedal(Medal.values()[2]);
        assertEquals(atl.getMedals().getFirst(), Medal.BRONZE,
                "Expected bronze medal to be added to the collection but it is not");
    }

    @Test
    void testAddSilverMedal() {
        atl.addMedal(Medal.values()[1]);
        assertEquals(atl.getMedals().getFirst(), Medal.SILVER,
                "Expected silver medal to be added to the collection but it is not");
    }

    @Test
    void testAddMedalWithTwoSameMedals() {
        atl.addMedal(Medal.BRONZE);
        atl.addMedal(Medal.BRONZE);

        List<Medal> medals = new ArrayList<>();
        medals.add(Medal.BRONZE);
        medals.add(Medal.BRONZE);

        assertIterableEquals(atl.getMedals(), medals,
                "Expected to have equal medals in the collection");
    }

    @Test
    void testEqualsReflexivity() {
        Competitor comp1 = new Athlete(id, name, nation);
        assertEquals(comp1, comp1, "No reflexivity provided");
    }

    @Test
    void testEqualsSymmetric() {
        Competitor comp1 = new Athlete(id, name, nation);
        Competitor comp2 = new Athlete(id, name, nation);

        assertEquals(comp1, comp2, "No symmetric provided");
        assertEquals(comp2, comp1, "No symmetric provided");
    }

    @Test
    void testEqualsTransitivity() {
        Competitor comp1 = new Athlete(id, name, nation);
        Competitor comp2 = new Athlete(id, name, nation);
        Competitor comp3 = new Athlete(id, name, nation);

        assertEquals(comp1, comp2, "No transitivity provided");
        assertEquals(comp2, comp3, "No transitivity provided");
        assertEquals(comp1, comp3, "No transitivity provided");
    }

    @Test
    void testEqualsWithNullAsSecondCompetitor() {
        Competitor comp1 = new Athlete(id, name, nation);
        assertNotEquals(comp1, null,
                "Not expected competitor to be equal to null");
    }

    @Test
    void testEqualsWithDifferentClasses() {
        Competitor comp1 = new Athlete(id, name, nation);
        Object ob = new Object();
        assertNotEquals(comp1, ob,
                "Objects from two different classes have to be different");
    }

    @Test
    void testEqualsWithValidArguments() {
        Competitor comp1 = new Athlete(id, name, nation);
        Competitor comp2 = new Athlete(id, name, nation);
        assertEquals(comp1, comp2,
                "Two competitors with same data have to be equal");
    }
}
