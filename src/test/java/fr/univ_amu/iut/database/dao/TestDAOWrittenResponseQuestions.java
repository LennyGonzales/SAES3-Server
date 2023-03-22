package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import fr.univ_amu.iut.service.dao.DAOWrittenResponseQuestions;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class TestDAOWrittenResponseQuestions {
    private DAOWrittenResponseQuestions daoWrittenResponseQuestions;
    private static Connection connection;
    private Database database;

    @Before
    public void beforeEachTest() throws SQLException {
        if(connection == null) {
            database = new Database();
            connection = database.initSingleConnection("TEST_STORIES");
        }
        daoWrittenResponseQuestions = new DAOWrittenResponseQuestionsJDBC(connection);
    }

    @Test
    public void testGetACertainNumberOfQCM() throws SQLException {
        List<WrittenResponseQuestion> writtenResponseQuestions = daoWrittenResponseQuestions.getACertainNumberOfWrittenResponseQuestion(1, "Tous les modules");
        assertNotNull(writtenResponseQuestions);
        assertTrue(writtenResponseQuestions.size() > 0);
    }

    @Test
    public void delete() throws SQLException {
        WrittenResponseQuestion question = new WrittenResponseQuestion();
        boolean result = daoWrittenResponseQuestions.delete(question);
        assertFalse(result);
    }

    @Test
    public void insert() throws SQLException {
        WrittenResponseQuestion question = new WrittenResponseQuestion();
        WrittenResponseQuestion result = daoWrittenResponseQuestions.insert(question);
        assertNull(result);
    }

    @Test
    public void update() {
        WrittenResponseQuestion question = new WrittenResponseQuestion();
        boolean result = daoWrittenResponseQuestions.update(question);
        assertFalse(result);
    }
}
