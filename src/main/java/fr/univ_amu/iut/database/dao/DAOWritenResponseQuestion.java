package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.WrittenResponseQuestion;

import java.sql.SQLException;
import java.util.List;

public interface DAOWritenResponseQuestion extends DAO {

    /**
     * Return a certain number of written response question on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and their answer
     * @throws SQLException if the request fails
     */
    List<WrittenResponseQuestion> getACertainNumberOfWrittenResponseQuestion(int numberOfTuples, String module) throws SQLException;
}
