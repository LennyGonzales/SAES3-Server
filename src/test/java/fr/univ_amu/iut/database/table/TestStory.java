package fr.univ_amu.iut.database.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestStory {
    private Story story;
    @BeforeEach
    public void beforeEachTest() {
        story = new Story(1, "module","description","question");
    }

    @Test
    public void shouldGetId() {
        assertEquals(1, story.getId());
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module", story.getModule());
    }

    @Test
    public void shouldGetDescription() {
        assertEquals("description", story.getDescription());
    }

    @Test
    public void shouldGetQuestion() {
        assertEquals("question", story.getQuestion());
    }

    @Test
    public void shouldSetId() {
        story.setId(2);
        assertEquals(2, story.getId());
    }

    @Test
    public void shouldSetModule() {
        story.setModule("moduleChange");
        assertEquals("moduleChange", story.getModule());
    }

    @Test
    public void shouldSetDescription() {
        story.setDescription("descriptionChange");
        assertEquals("descriptionChange", story.getDescription());
    }

    @Test
    public void shouldSetQuestion() {
        story.setQuestion("questionChange");
        assertEquals("questionChange", story.getQuestion());
    }
}
