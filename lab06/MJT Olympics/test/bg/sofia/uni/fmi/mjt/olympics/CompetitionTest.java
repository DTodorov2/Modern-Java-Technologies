package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CompetitionTest {

    @Test
    void testCreatingCompetitionWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Competition(null, null, null));
    }

    @Test
    void testCreatingCompetitionWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("  ", null, null));
    }

    @Test
    void testCreatingCompetitionWithNullDiscipline() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", null, null));
    }

    @Test
    void testCreatingCompetitionWithBlankDiscipline() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", " ", null));
    }

    @Test
    void testCreatingCompetitionWithNullCompetitors() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", "discipline", null));
    }

    @Test
    void testCreatingCompetitionWithEmptyCompetitors() {
        assertThrows(IllegalArgumentException.class, () -> new Competition("name", "discipline", new HashSet<>()));
    }

    @Test
    void testCreatingCompetitionWithValidArguments() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(new Athlete("id", "name", "null"));
        assertDoesNotThrow(() -> new Competition("name", "discipline", competitors));
    }

    @Test
    void testCompetitorsIsNotModifiable() {
        Set<Competitor> competitors = new HashSet<>();
        competitors.add(new Athlete("id", "name", "BG"));
        Competition competition = new Competition("Name", "Discipline", competitors);
        assertThrows(UnsupportedOperationException.class, () -> competition.competitors().add(
                new Athlete("id1", "name1", "GR")));
    }
}
