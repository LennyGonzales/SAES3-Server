package fr.univ_amu.iut.service.dao;

import fr.univ_amu.iut.domain.WrittenResponseQuestion;

import java.sql.SQLException;
import java.util.List;

/**
 * The methods' signature for the WrittenResponses table
 * @author LennyGonzales
 */
public interface DAOWrittenResponseQuestions extends DAO<WrittenResponseQuestion, Integer> {

    /**
     * Return a certain number of written response question on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and their answer
     * @throws SQLException if the request fails
     */
    List<WrittenResponseQuestion> getACertainNumberOfWrittenResponseQuestion(int numberOfTuples, String module) throws SQLException;
}
