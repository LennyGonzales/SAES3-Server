package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.Qcm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods' for the Qcm table
 */
public class DAOQcmJDBC implements DAOQcm {
    private PreparedStatement getACertainNumberOfQCMStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    /**
     * Constructor | Prepare the SQL requests
     * @throws SQLException if the prepareStatement didn't go well
     */
    public DAOQcmJDBC() throws SQLException {
        getACertainNumberOfQCMStatement = CONNECTION.prepareStatement("SELECT DISTINCT DESCRIPTION, QUESTION, TRUE_ANSWER, ANSWER_1, ANSWER_2, ANSWER_3 FROM HISTORY H, QCM Q WHERE H.ID = Q.ID and H.ID IN (select ID from HISTORY H WHERE H.MODULE = ? ORDER BY RANDOM() LIMIT ?) LIMIT ?;");
    }

    /**
     * Return a certain number of qcm on a certain module
     * @param numberOfTuples number of tuples that we want to get
     * @param module The questions are attached to a specific module
     * @return a list of questions and answers
     * @throws SQLException if the request fails
     */
    @Override
    public List<Qcm> getACertainNumberOfQCM(int numberOfTuples, String module) throws SQLException {
        getACertainNumberOfQCMStatement.setString(1, module);
        getACertainNumberOfQCMStatement.setInt(2, numberOfTuples);
        getACertainNumberOfQCMStatement.setInt(3, numberOfTuples);
        ResultSet result = getACertainNumberOfQCMStatement.executeQuery();
        List<Qcm> qcmList = new ArrayList<>();

        while(result.next()) {
            Qcm qcm = new Qcm();
            qcm.setDescription(result.getString(1));
            qcm.setQuestion(result.getString(2));
            qcm.setTrueAnswer(result.getInt(3));
            qcm.setAnswer1(result.getString(4));
            qcm.setAnswer2(result.getString(5));
            qcm.setAnswer3(result.getString(6));
            qcmList.add(qcm);
        }
        return qcmList;
    }

    /**
     * Allows removal of a tuple from the base
     * @param qcm tuple to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     */
    @Override
    public boolean delete(Qcm qcm) {
        return false;
    }

    /**
     * Allows to create a tuple in the database with an object
     * @param qcm tuple to insert into the database
     * @return the tuple inserted
     */
    @Override
    public Qcm insert(Qcm qcm) {
        return null;
    }

    /**
     * Allows to update a tuple in the database with an object
     * @param qcm tuple to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    @Override
    public boolean update(Qcm qcm) {
        return false;
    }
}
