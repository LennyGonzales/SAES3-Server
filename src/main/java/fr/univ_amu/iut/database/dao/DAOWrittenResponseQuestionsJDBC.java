package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods' for the WrittenResponses table
 * @author LennyGonzales
 */
public class DAOWrittenResponseQuestionsJDBC implements DAOWrittenResponseQuestions {
    private PreparedStatement getACertainNumberOfWrittenResponseQuestion;

    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOWrittenResponseQuestionsJDBC() throws SQLException {
        getACertainNumberOfWrittenResponseQuestion = CONNECTION.prepareStatement("SELECT DISTINCT DESCRIPTION, QUESTION, TRUE_ANSWER FROM STORIES S, WRITTENRESPONSES W WHERE S.ID = W.ID and S.ID IN (select ID from STORIES S WHERE S.MODULE = ? ORDER BY RANDOM() LIMIT ?) LIMIT ?;");
    }

    /**
     * Return a certain number of written response question on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and their answer
     * @throws SQLException if the request fails
     */
    @Override
    public List<WrittenResponseQuestion> getACertainNumberOfWrittenResponseQuestion(int numberOfTuples, String module) throws SQLException {
        // Prepare the request
        getACertainNumberOfWrittenResponseQuestion.setString(1, module);
        getACertainNumberOfWrittenResponseQuestion.setInt(2, numberOfTuples);
        getACertainNumberOfWrittenResponseQuestion.setInt(3, numberOfTuples);

        ResultSet result = getACertainNumberOfWrittenResponseQuestion.executeQuery();
        List<WrittenResponseQuestion> writtenResponseQuestions = new ArrayList<>();
        WrittenResponseQuestion writtenResponseQuestion;
        while(result.next()) {
            writtenResponseQuestion = new WrittenResponseQuestion();
            writtenResponseQuestion.setModule(module);
            writtenResponseQuestion.setDescription(result.getString(1));
            writtenResponseQuestion.setQuestion(result.getString(2));
            writtenResponseQuestion.setTrueAnswer(result.getString(3));
            writtenResponseQuestions.add(writtenResponseQuestion);
        }
        return writtenResponseQuestions;
    }

    /**
     * Allows removal of a tuple from the base
     * @param writtenResponseQuestion tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     * @throws SQLException if the deletion didn't go well
     */
    @Override
    public boolean delete(WrittenResponseQuestion writtenResponseQuestion) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param writtenResponseQuestion tuple to insert into the database
     * @return the tuple inserted
     * @throws SQLException if the insertion didn't go well
     */
    @Override
    public WrittenResponseQuestion insert(WrittenResponseQuestion writtenResponseQuestion) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param writtenResponseQuestion tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(WrittenResponseQuestion writtenResponseQuestion) {
        return false;
    }
}
