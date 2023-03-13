package fr.univ_amu.iut.service.dao;

import fr.univ_amu.iut.domain.MultipleChoiceQuestion;

import java.sql.SQLException;
import java.util.List;

/**
 * The methods' signature for the MultipleChoiceResponses table
 * @author LennyGonzales
 */
public interface DAOMultipleChoiceQuestions extends DAO<MultipleChoiceQuestion,Integer>{
    /**
     * Return a certain number of qcm on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and answers
     * @throws SQLException if the request fails
     */
    List<MultipleChoiceQuestion> getACertainNumberOfQCM(int numberOfTuples, String module) throws SQLException;
}
