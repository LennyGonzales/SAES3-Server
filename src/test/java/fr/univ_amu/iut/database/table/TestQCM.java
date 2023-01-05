package fr.univ_amu.iut.database.table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestQCM {
    private Qcm qcm;

    @BeforeEach
    public void beforeEachTest() {
        qcm = new Qcm();
        qcm.setId(1);
        qcm.setModule("module");
        qcm.setDescription("description");
        qcm.setQuestion("question");
        qcm.setTrueAnswer(1);
        qcm.setAnswer1("answer1");
        qcm.setAnswer2("answer2");
        qcm.setAnswer3("answer3");
    }


    @Test
    public void shouldGetId() {
        assertEquals(1, qcm.getId());
    }

    @Test
    public void shouldSetId() {
        qcm.setId(2);
        assertEquals(2, qcm.getId());
    }

    @Test
    public void shouldGetModule() {
        assertEquals("module", qcm.getModule());
    }

    @Test
    public void shouldSetModule() {
        qcm.setModule("TCP");
        assertEquals("TCP", qcm.getModule());
    }

    @Test
    public void shouldGetDescription() {
        assertEquals("description", qcm.getDescription());
    }

    @Test
    public void shouldSetDescription() {
        qcm.setDescription("descriptionChange");
        assertEquals("descriptionChange", qcm.getDescription());
    }

    @Test
    public void shouldGetQuestion() {
        assertEquals("question", qcm.getQuestion());
    }

    @Test
    public void shouldSetQuestion() {
        qcm.setQuestion("questionChange");
        assertEquals("questionChange", qcm.getQuestion());
    }

    @Test
    public void shouldGetTrueAnswer() {
        assertEquals(1, qcm.getTrueAnswer());
    }

    @Test
    public void shouldSetTrueAnswer() {
        qcm.setTrueAnswer(2);
        assertEquals(2, qcm.getTrueAnswer());
    }

    @Test
    public void shouldGetAnswer1() {
        assertEquals("answer1", qcm.getAnswer1());
    }

    @Test
    public void shouldSetAnswer1() {
        qcm.setAnswer1("answer1Change");
        assertEquals("answer1Change", qcm.getAnswer1());
    }

    @Test
    public void shouldGetAnswer2() {
        assertEquals("answer2", qcm.getAnswer2());
    }

    @Test
    public void shouldSetAnswer2() {
        qcm.setAnswer2("answer2Change");
        assertEquals("answer2Change", qcm.getAnswer2());
    }

    @Test
    public void shouldGetAnswer3() {
        assertEquals("answer3", qcm.getAnswer3());
    }

    @Test
    public void shouldSetAnswer3() {
        qcm.setAnswer3("answer3Change");
        assertEquals("answer3Change", qcm.getAnswer3());
    }
}
