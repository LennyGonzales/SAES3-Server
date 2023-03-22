package fr.univ_amu.iut.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestQuestion {
    private Question question;

    @BeforeEach
    public void beforeEachTest() {
        question = new Question();
        question.setId(1);
        question.setModule("module");
        question.setDescription("description");
        question.setQuestion("question");
        question.setNbAnswers(1);
        question.setNbCorrectAnswers(1);
    }


    @Test
    public void shouldGetId() {
        assertEquals(1, question.getId());
    }

    @Test
    public void shouldSetId() {
        question.setId(2);
        assertEquals(2, question.getId());
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module", question.getModule());
    }

    @Test
    public void shouldSetModule() {
        question.setModule("TCP");
        assertEquals("TCP", question.getModule());
    }

    @Test
    public void shouldGetDescription() {
        assertEquals("description", question.getDescription());
    }

    @Test
    public void shouldSetDescription() {
        question.setDescription("descriptionChange");
        assertEquals("descriptionChange", question.getDescription());
    }

    @Test
    public void shouldGetQuestion() {
        assertEquals("question", question.getQuestion());
    }

    @Test
    public void shouldSetQuestion() {
        question.setQuestion("questionChange");
        assertEquals("questionChange", question.getQuestion());
    }

    @Test
    public void shouldGetNbAnswers() {
        assertEquals(1, question.getNbAnswers());
    }

    @Test
    public void shouldSetNbAnswers() {
        question.setNbAnswers(2);
        assertEquals(2, question.getNbAnswers());
    }

    @Test
    public void shouldGetNbCorrectAnswers() {
        assertEquals(1, question.getNbCorrectAnswers());
    }

    @Test
    public void shouldSetNbCorrectAnswers() {
        question.setNbCorrectAnswers(2);
        assertEquals(2, question.getNbCorrectAnswers());
    }
}
