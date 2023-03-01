package fr.univ_amu.iut.database.table;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

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
}
