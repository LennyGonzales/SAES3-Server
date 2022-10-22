package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.database.table.Qcm;

import java.sql.SQLException;
import java.util.List;

public interface DAOQuiz extends DAO{
    /**
     * Return the qcm stores in the database
     * @return List<Qcm>
     * @throws SQLException
     */
    List<Qcm> findAllQCM() throws SQLException;
}
