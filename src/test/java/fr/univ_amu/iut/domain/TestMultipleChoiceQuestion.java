package fr.univ_amu.iut.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestMultipleChoiceQuestion {
    private MultipleChoiceQuestion multipleChoiceQuestion;

    @BeforeEach
    public void beforeEachTest() {
        multipleChoiceQuestion = new MultipleChoiceQuestion();
        multipleChoiceQuestion.setId(1);
        multipleChoiceQuestion.setModule("module");
        multipleChoiceQuestion.setDescription("description");
        multipleChoiceQuestion.setQuestion("question");
        multipleChoiceQuestion.setTrueAnswer(1);
        multipleChoiceQuestion.setAnswer1("answer1");
        multipleChoiceQuestion.setAnswer2("answer2");
        multipleChoiceQuestion.setAnswer3("answer3");
        multipleChoiceQuestion.setNbAnswers(1);
        multipleChoiceQuestion.setNbCorrectAnswers(1);
    }


    @Test
    public void shouldGetId() {
        assertEquals(1, multipleChoiceQuestion.getId());
    }

    @Test
    public void shouldSetId() {
        multipleChoiceQuestion.setId(2);
        assertEquals(2, multipleChoiceQuestion.getId());
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module", multipleChoiceQuestion.getModule());
    }

    @Test
    public void shouldSetModule() {
        multipleChoiceQuestion.setModule("TCP");
        assertEquals("TCP", multipleChoiceQuestion.getModule());
    }

    @Test
    public void shouldGetDescription() {
        assertEquals("description", multipleChoiceQuestion.getDescription());
    }

    @Test
    public void shouldSetDescription() {
        multipleChoiceQuestion.setDescription("descriptionChange");
        assertEquals("descriptionChange", multipleChoiceQuestion.getDescription());
    }

    @Test
    public void shouldGetQuestion() {
        assertEquals("question", multipleChoiceQuestion.getQuestion());
    }

    @Test
    public void shouldSetQuestion() {
        multipleChoiceQuestion.setQuestion("questionChange");
        assertEquals("questionChange", multipleChoiceQuestion.getQuestion());
    }

    @Test
    public void shouldGetTrueAnswer() {
        assertEquals(1, multipleChoiceQuestion.getTrueAnswer().intValue());
    }

    @Test
    public void shouldSetTrueAnswer() {
        multipleChoiceQuestion.setTrueAnswer(2);
        assertEquals(2, multipleChoiceQuestion.getTrueAnswer().intValue());
    }

    @Test
    public void shouldGetAnswer1() {
        assertEquals("answer1", multipleChoiceQuestion.getAnswer1());
    }

    @Test
    public void shouldSetAnswer1() {
        multipleChoiceQuestion.setAnswer1("answer1Change");
        assertEquals("answer1Change", multipleChoiceQuestion.getAnswer1());
    }

    @Test
    public void shouldGetAnswer2() {
        assertEquals("answer2", multipleChoiceQuestion.getAnswer2());
    }

    @Test
    public void shouldSetAnswer2() {
        multipleChoiceQuestion.setAnswer2("answer2Change");
        assertEquals("answer2Change", multipleChoiceQuestion.getAnswer2());
    }

    @Test
    public void shouldGetNbAnswers() {
        assertEquals(1, multipleChoiceQuestion.getNbAnswers());
    }

    @Test
    public void shouldSetNbAnswers() {
        multipleChoiceQuestion.setNbAnswers(2);
        assertEquals(2, multipleChoiceQuestion.getNbAnswers());
    }

    @Test
    public void shouldGetNbCorrectAnswers() {
        assertEquals(1, multipleChoiceQuestion.getNbCorrectAnswers());
    }

    @Test
    public void shouldSetNbCorrectAnswers() {
        multipleChoiceQuestion.setNbCorrectAnswers(2);
        assertEquals(2, multipleChoiceQuestion.getNbCorrectAnswers());
    }

    @Test
    public void shouldCloneNotEquals() throws CloneNotSupportedException {
        assertFalse(multipleChoiceQuestion.clone().equals(multipleChoiceQuestion));
    }
}
