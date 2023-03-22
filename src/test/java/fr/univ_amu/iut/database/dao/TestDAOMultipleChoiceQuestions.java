package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class TestDAOMultipleChoiceQuestions {
    private DAOMultipleChoiceQuestions daoMultipleChoiceQuestions;
    private static Connection connection;
    private Database database;

    @Before
    public void beforeEachTest() throws SQLException {
        if(connection == null) {
            database = new Database();
            connection = database.initSingleConnection("TEST_STORIES");
        }
        daoMultipleChoiceQuestions = new DAOMultipleChoiceQuestionsJDBC(connection);
    }

    @Test
    public void testGetACertainNumberOfQCM() throws SQLException {
        List<MultipleChoiceQuestion> multipleChoiceQuestionsList = daoMultipleChoiceQuestions.getACertainNumberOfQCM(1, "Tous les modules");
        assertNotNull(multipleChoiceQuestionsList);
        assertTrue(multipleChoiceQuestionsList.size() > 0);
    }


    @Test
    public void delete() throws SQLException {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        boolean result = daoMultipleChoiceQuestions.delete(question);
        assertFalse(result);
    }

    @Test
    public void insert() throws SQLException {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        MultipleChoiceQuestion result = daoMultipleChoiceQuestions.insert(question);
        assertNull(result);
    }

    @Test
    public void update() {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        boolean result = daoMultipleChoiceQuestions.update(question);
        assertFalse(result);
    }
}
