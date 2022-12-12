package fr.univ_amu.iut.database.dao;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.database.table.Qcm;
import fr.univ_amu.iut.database.table.WrittenResponseQuestion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOWrittenResponseQuestionJDBC implements DAOWritenResponseQuestion{
    private PreparedStatement getACertainNumberOfWrittenResponseQuestion;

    private static final Connection CONNECTION = Main.database.getConnection();

    public DAOWrittenResponseQuestionJDBC() throws SQLException {
        getACertainNumberOfWrittenResponseQuestion = CONNECTION.prepareStatement("SELECT DISTINCT DESCRIPTION, QUESTION, TRUE_ANSWER FROM HISTORY H, WRITTENRESPONSE W WHERE H.ID = W.ID and H.ID IN (select ID from HISTORY H WHERE H.MODULE = ? ORDER BY RANDOM() LIMIT ?) LIMIT ?;");
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
        getACertainNumberOfWrittenResponseQuestion.setString(1, module);
        getACertainNumberOfWrittenResponseQuestion.setInt(2, numberOfTuples);
        getACertainNumberOfWrittenResponseQuestion.setInt(3, numberOfTuples);
        ResultSet result = getACertainNumberOfWrittenResponseQuestion.executeQuery();
        List<WrittenResponseQuestion> writtenResponseQuestions = new ArrayList<>();

        while(result.next()) {
            WrittenResponseQuestion writtenResponseQuestion = new WrittenResponseQuestion();
            writtenResponseQuestion.setDescription(result.getString(1));
            writtenResponseQuestion.setQuestion(result.getString(2));
            writtenResponseQuestion.setTrueAnswer(result.getString(3));
            writtenResponseQuestions.add(writtenResponseQuestion);
        }
        return writtenResponseQuestions;
    }
    @Override
    public boolean delete(Object obj) throws SQLException {
        return false;
    }

    @Override
    public Object insert(Object obj) throws SQLException {
        return null;
    }

    @Override
    public boolean update(Object obj) {
        return false;
    }
}
