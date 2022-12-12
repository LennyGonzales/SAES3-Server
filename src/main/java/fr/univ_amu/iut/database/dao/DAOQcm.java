package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.Qcm;

import java.sql.SQLException;
import java.util.List;

public interface DAOQcm extends DAO{
    /**
     * Return a certain number of qcm on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and answers
     * @throws SQLException if the request fails
     */
    List<Qcm> getACertainNumberOfQCM(int numberOfTuples, String module) throws SQLException;
}
