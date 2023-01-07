package fr.univ_amu.iut.database.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestHistory {
    private History history;
    @BeforeEach
    public void beforeEachTest() {
        history = new History(1, "module","description","question");
    }

    @Test
    public void shouldGetId() {
        assertEquals(1,history.getId());
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module",history.getModule());
    }

    @Test
    public void shouldGetDescription() {
        assertEquals("description",history.getDescription());
    }

    @Test
    public void shouldGetQuestion() {
        assertEquals("question",history.getQuestion());
    }

    @Test
    public void shouldSetId() {
        history.setId(2);
        assertEquals(2,history.getId());
    }

    @Test
    public void shouldSetModule() {
        history.setModule("moduleChange");
        assertEquals("moduleChange",history.getModule());
    }

    @Test
    public void shouldSetDescription() {
        history.setDescription("descriptionChange");
        assertEquals("descriptionChange",history.getDescription());
    }

    @Test
    public void shouldSetQuestion() {
        history.setQuestion("questionChange");
        assertEquals("questionChange",history.getQuestion());
    }
}
