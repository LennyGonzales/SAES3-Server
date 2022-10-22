package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.Qcm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOQuizJDBC implements DAOQuiz{
    private PreparedStatement findAllQCMStatement;
    private static final Connection CONNECTION = Main.database.getConnection();

    public DAOQuizJDBC() throws SQLException {
        findAllQCMStatement = CONNECTION.prepareStatement("SELECT QUESTION, TRUE_ANSWER, ANSWER_1, ANSWER_2, ANSWER_3 FROM QUIZ_FR");
    }

    @Override
    public List<Qcm> findAllQCM() throws SQLException {
        ResultSet result = findAllQCMStatement.executeQuery();
        List<Qcm> qcmList = new ArrayList<>();

        while(result.next()) {
            Qcm qcm = new Qcm();
            qcm.setQuestion(result.getString(1));
            qcm.setTrueAnswer(result.getInt(2));
            qcm.setAnswer1(result.getString(3));
            qcm.setAnswer2(result.getString(4));
            qcm.setAnswer3(result.getString(5));
            qcmList.add(qcm);
        }
        return qcmList;
    }

    @Override
    public boolean delete(Object obj) {
        return false;
    }

    @Override
    public Object insert(Object obj) {
        return null;
    }

    @Override
    public boolean update(Object obj) {
        return false;
    }
}
