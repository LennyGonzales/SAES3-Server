package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.Database;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.service.dao.DAOQuestions;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestDAOQuestions {
    private DAOQuestions daoQuestions;
    private static Connection connection;
    private Database database;

    @Before
    public void beforeEachTest() throws SQLException {
        if(connection == null) {
            database = new Database();
            connection = database.initSingleConnection("TEST_STORIES");
        }
        daoQuestions = new DAOQuestionsJDBC(connection);
    }

    @Test
    public void getAllModules() throws SQLException {
        List<String> modules = daoQuestions.getAllModules();
        assertNotNull(modules);
        assertTrue(modules.size() > 0);
    }

    @Test
    public void incrementNbAnswers() throws SQLException {
        List<Integer> correctAnswerIdList = new ArrayList<>();
        correctAnswerIdList.add(1);
        List<Integer> allIdList = new ArrayList<>();
        allIdList.add(1);
        allIdList.add(2);
        boolean result = daoQuestions.incrementNbAnswers(correctAnswerIdList, allIdList);
        assertTrue(result);
    }

    @Test
    public void delete() throws SQLException {
        Question question = new Question();
        boolean result = daoQuestions.delete(question);
        assertFalse(result);
    }

    @Test
    public void insert() throws SQLException {
        Question question = new Question();
        Question result = daoQuestions.insert(question);
        assertNull(result);
    }

    @Test
    public void update() {
        Question question = new Question();
        boolean result = daoQuestions.update(question);
        assertFalse(result);
    }
}
