package fr.univ_amu.iut.service.dao;

import fr.univ_amu.iut.domain.Story;

import java.sql.SQLException;
import java.util.List;

/**
 * The methods' signature for the Stories table
 * @author LennyGonzales
 */
public interface DAOQuestions extends DAO<Story,Integer> {

    /**
     * Get all the modules
     * @return all the modules
     * @throws SQLException the SQL request didn't go well
     */
    List<String> getAllModules() throws SQLException;

    /**
     * Increment the number of answers for a list of question IDs and the number of correct answers for another list of question IDs
     * @param correctAnswerIdList a list containing all the correct answers questions IDs
     * @param allIdList a list containing all the questions IDs
     * @return if the query worked or failed
     * @throws SQLException the SQL request didn't go well
     */
    boolean incrementNbAnswers(List<Integer> correctAnswerIdList, List<Integer> allIdList) throws SQLException;
}
