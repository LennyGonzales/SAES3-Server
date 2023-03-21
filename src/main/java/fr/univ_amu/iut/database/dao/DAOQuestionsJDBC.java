package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.service.dao.DAOQuestions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods' for the Questions table
 * @author LennyGonzales
 */
public class DAOQuestionsJDBC implements DAOQuestions {
    private final PreparedStatement getAllModulesStatement;
    private final PreparedStatement incrementNbAnswersAndNbCorrectAnswers;
    private static final Connection CONNECTION = Main.database.getConnections().get("STORIES");

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOQuestionsJDBC() throws SQLException {
        getAllModulesStatement = CONNECTION.prepareStatement("SELECT DISTINCT MODULE FROM QUESTIONS;");
        incrementNbAnswersAndNbCorrectAnswers = CONNECTION.prepareStatement("UPDATE QUESTIONS SET nbAnswers = nbAnswers + 1, nbCorrectAnswers = nbCorrectAnswers + CASE WHEN id = ANY (?) THEN 1 ELSE 0 END WHERE id = ANY (?);");

    }

    /**
     * Get all the modules
     * @return all the modules
     * @throws SQLException the SQL request didn't go well
     */
    @Override
    public List<String> getAllModules() throws SQLException {
        ResultSet result = getAllModulesStatement.executeQuery();

        List<String> modules = new ArrayList<>();
        while(result.next()) {
            modules.add(result.getString(1));
        }
        return modules;
    }

    /**
     * Increment the number of answers for a list of question IDs and the number of correct answers for another list of question IDs
     * @param correctAnswerIdList a list containing all the correct answers questions IDs
     * @param allIdList a list containing all the questions IDs
     * @return if the query worked or failed
     * @throws SQLException the SQL request didn't go well
     */
    public boolean incrementNbAnswers(List<Integer> correctAnswerIdList, List<Integer> allIdList) throws SQLException {
        incrementNbAnswersAndNbCorrectAnswers.setArray(1, CONNECTION.createArrayOf("INTEGER", correctAnswerIdList.toArray()));
        incrementNbAnswersAndNbCorrectAnswers.setArray(2, CONNECTION.createArrayOf("INTEGER", allIdList.toArray()));
        return (incrementNbAnswersAndNbCorrectAnswers.executeUpdate() > 0);
    }

    /**
     * Allows removal of a tuple from the base
     * @param question tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(Question question) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param question tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public Question insert(Question question) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param question tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(Question question) {
        return false;
    }
}
