package fr.univ_amu.iut.database.table;

import fr.univ_amu.iut.domain.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestStory {
    private Story story;
    @BeforeEach
    public void beforeEachTest() {
        story = new Story("module");
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module", story.getModule());
    }

    @Test
    public void shouldSetModule() {
        story.setModule("moduleChange");
        assertEquals("moduleChange", story.getModule());
    }
}
