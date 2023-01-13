package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.MultipleChoiceQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods' for the MultipleChoiceResponses table
 * @author LennyGonzales
 */
public class DAOMultipleChoiceQuestionsJDBC implements DAOMultipleChoiceQuestions {
    private PreparedStatement getACertainNumberOfQCMStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOMultipleChoiceQuestionsJDBC() throws SQLException {
        getACertainNumberOfQCMStatement = CONNECTION.prepareStatement("SELECT DISTINCT DESCRIPTION, QUESTION, TRUE_ANSWER, ANSWER_1, ANSWER_2, ANSWER_3 FROM STORIES S, MULTIPLECHOICERESPONSES M WHERE S.ID = M.ID and S.ID IN (select ID from STORIES S WHERE S.MODULE = ? ORDER BY RANDOM() LIMIT ?) LIMIT ?;");
    }

    /**
     * Return a certain number of MultipleChoiceResponses on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and answers
     * @throws SQLException if the request fails
     */
    @Override
    public List<MultipleChoiceQuestion> getACertainNumberOfQCM(int numberOfTuples, String module) throws SQLException {
        getACertainNumberOfQCMStatement.setString(1, module);
        getACertainNumberOfQCMStatement.setInt(2, numberOfTuples);
        getACertainNumberOfQCMStatement.setInt(3, numberOfTuples);
        ResultSet result = getACertainNumberOfQCMStatement.executeQuery();
        List<MultipleChoiceQuestion> multipleChoiceQuestionList = new ArrayList<>();
        MultipleChoiceQuestion multipleChoiceQuestion;
        while(result.next()) {
            multipleChoiceQuestion = new MultipleChoiceQuestion();
            multipleChoiceQuestion.setModule(module);
            multipleChoiceQuestion.setDescription(result.getString(1));
            multipleChoiceQuestion.setQuestion(result.getString(2));
            multipleChoiceQuestion.setTrueAnswer(result.getInt(3));
            multipleChoiceQuestion.setAnswer1(result.getString(4));
            multipleChoiceQuestion.setAnswer2(result.getString(5));
            multipleChoiceQuestion.setAnswer3(result.getString(6));
            multipleChoiceQuestionList.add(multipleChoiceQuestion);
        }
        return multipleChoiceQuestionList;
    }

    /**
     * Allows removal of a tuple from the base
     * @param multipleChoiceQuestion tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(MultipleChoiceQuestion multipleChoiceQuestion) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param multipleChoiceQuestion tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public MultipleChoiceQuestion insert(MultipleChoiceQuestion multipleChoiceQuestion) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param multipleChoiceQuestion tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(MultipleChoiceQuestion multipleChoiceQuestion) {
        return false;
    }
}
