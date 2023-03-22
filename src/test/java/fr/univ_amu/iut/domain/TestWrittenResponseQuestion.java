package fr.univ_amu.iut.domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestWrittenResponseQuestion {
    private WrittenResponseQuestion writtenResponseQuestion;
    @BeforeEach
    public void beforeEachTest() {
        writtenResponseQuestion = new WrittenResponseQuestion();
        writtenResponseQuestion.setId(1);
        writtenResponseQuestion.setDescription("description");
        writtenResponseQuestion.setQuestion("question");
        writtenResponseQuestion.setModule("module");
        writtenResponseQuestion.setTrueAnswer("trueAnswer");
        writtenResponseQuestion.setNbAnswers(1);
        writtenResponseQuestion.setNbCorrectAnswers(1);
    }


    @Test
    public void shouldGetId() {
        assertEquals(1, writtenResponseQuestion.getId());
    }

    @Test
    public void shouldSetId() {
        writtenResponseQuestion.setId(2);
        assertEquals(2, writtenResponseQuestion.getId());
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module", writtenResponseQuestion.getModule());
    }

    @Test
    public void shouldSetModule() {
        writtenResponseQuestion.setModule("moduleChange");
        assertEquals("moduleChange", writtenResponseQuestion.getModule());
    }

    @Test
    public void shouldGetDescription() {
        assertEquals("description", writtenResponseQuestion.getDescription());
    }

    @Test
    public void shouldSetDescription() {
        writtenResponseQuestion.setDescription("descriptionChange");
        assertEquals("descriptionChange", writtenResponseQuestion.getDescription());
    }

    @Test
    public void shouldGetQuestion() {
        assertEquals("question", writtenResponseQuestion.getQuestion());
    }

    @Test
    public void shouldSetQuestion() {
        writtenResponseQuestion.setQuestion("questionChange");
        assertEquals("questionChange", writtenResponseQuestion.getQuestion());
    }

    @Test
    public void shouldGetTrueAnswer() {
        assertEquals("trueAnswer", writtenResponseQuestion.getTrueAnswer());
    }

    @Test
    public void shouldSetTrueAnswer() {
        writtenResponseQuestion.setTrueAnswer("trueAnswerChange");
        assertEquals("trueAnswerChange", writtenResponseQuestion.getTrueAnswer());
    }

    @Test
    public void shouldGetNbAnswers() {
        assertEquals(1, writtenResponseQuestion.getNbAnswers());
    }

    @Test
    public void shouldSetNbAnswers() {
        writtenResponseQuestion.setNbAnswers(2);
        assertEquals(2, writtenResponseQuestion.getNbAnswers());
    }

    @Test
    public void shouldGetNbCorrectAnswers() {
        assertEquals(1, writtenResponseQuestion.getNbCorrectAnswers());
    }

    @Test
    public void shouldSetNbCorrectAnswers() {
        writtenResponseQuestion.setNbCorrectAnswers(2);
        assertEquals(2, writtenResponseQuestion.getNbCorrectAnswers());
    }

    @Test
    public void shouldCloneNotEquals() throws CloneNotSupportedException {
        assertFalse(writtenResponseQuestion.clone().equals(writtenResponseQuestion));
    }
}
