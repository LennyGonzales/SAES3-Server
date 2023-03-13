package fr.univ_amu.iut.service;

import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import fr.univ_amu.iut.service.dao.DAOQuestions;
import fr.univ_amu.iut.service.dao.DAOUsers;
import fr.univ_amu.iut.service.dao.DAOWrittenResponseQuestions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author LennyGonzales
 */
public class StoryChecking {
    private HashMap<Integer, Integer> currentMultipleChoiceResponse;
    private HashMap<Integer, String> currentWrittenResponse;
    private int currentCorrectAnswers;

    public StoryChecking() {
        currentMultipleChoiceResponse = new HashMap<>();
        currentWrittenResponse = new HashMap<>();
        currentCorrectAnswers = 0;
    }

    /**
     * Get a story
     * @param module module of the story
     * @param numberOfQuestions number of questions in the story
     * @param daoMultipleChoiceQuestions interface (Reversing dependencies) to access database
     * @param daoWrittenResponseQuestions interface (Reversing dependencies) to access database
     * @return a list of Question (story)
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     * @throws CloneNotSupportedException if the clone in StoryChecking.getStory isn't supported
     */
    public List<Question> getStory(String module, int numberOfQuestions, DAOMultipleChoiceQuestions daoMultipleChoiceQuestions, DAOWrittenResponseQuestions daoWrittenResponseQuestions) throws SQLException, CloneNotSupportedException {
        List<MultipleChoiceQuestion> multipleChoiceQuestionList = daoMultipleChoiceQuestions.getACertainNumberOfQCM(numberOfQuestions/2, module);
        List<WrittenResponseQuestion> writtenResponseQuestionList = daoWrittenResponseQuestions.getACertainNumberOfWrittenResponseQuestion(numberOfQuestions/2, module);

        MultipleChoiceQuestion multipleChoiceQuestionClone;
        for(MultipleChoiceQuestion question : multipleChoiceQuestionList) {
            multipleChoiceQuestionClone = question.clone(); // => not the same reference (setTrueAnswer isn't going to modify currentWrittenResponse HashMap
            currentMultipleChoiceResponse.put(question.getId(), multipleChoiceQuestionClone.getTrueAnswer());
            question.setTrueAnswer(null);
        }
        WrittenResponseQuestion writtenResponseQuestionClone;
        for(WrittenResponseQuestion question : writtenResponseQuestionList) {
            writtenResponseQuestionClone = question.clone();    // => not the same reference (setTrueAnswer isn't going to modify currentWrittenResponse HashMap
            currentWrittenResponse.put(question.getId(), writtenResponseQuestionClone.getTrueAnswer());
            question.setTrueAnswer(null);
        }

        List<Question> story = new ArrayList<>();
        story.addAll(multipleChoiceQuestionList);
        story.addAll(writtenResponseQuestionList);
        return story;
    }

    /**
     * Get the summary
     * @param story story received
     * @param usersChecking an instance of UsersChecking
     * @param daoUsers interface (Reversing dependencies)
     * @return a hashmap containing Question and if the user sent corrects answers
     * @throws UserIsNotInTheDatabaseException if the user isn't in the database
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public HashMap<Question, Boolean> getSummary(Object story, UsersChecking usersChecking, DAOUsers daoUsers) throws UserIsNotInTheDatabaseException, SQLException {
        List<Question> storyReceived = (List<Question>) story;
        HashMap<Question, Boolean> storyToSend = new HashMap<>();
        boolean answerStatus;

        for(Question question : storyReceived) {
            if(question instanceof MultipleChoiceQuestion) {
                answerStatus = currentMultipleChoiceResponse.get(question.getId()).equals(((MultipleChoiceQuestion) question).getTrueAnswer()); // Verify if the answer is corrects
                storyToSend.put(question, answerStatus);

                if(answerStatus) { ++currentCorrectAnswers; }
            }
            else if(question instanceof WrittenResponseQuestion) {
                answerStatus = currentWrittenResponse.get(question.getId()).equals(((WrittenResponseQuestion) question).getTrueAnswer());   // Verify if the answer is corrects
                storyToSend.put(question, answerStatus);

                if(answerStatus) { ++currentCorrectAnswers; }
            }
        }

        usersChecking.updateUsersPoints((int) (10 * (currentCorrectAnswers - (storyReceived.size()/2.0)) * (1 - (usersChecking.getUser().getPoints()) / 2000.0)), daoUsers);   // function to calculate the new user points

        return storyToSend;
    }

    /**
     * Get user's points
     * @param usersChecking an instance of UsersChecking
     * @return user's points
     */
    public int getUserPoints(UsersChecking usersChecking) {
        return usersChecking.getUser().getPoints();
    }

    /**
     * Get modules
     * @param daoQuestions interface (Reversing dependencies) to access database
     * @return a list of modules
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public List<String> getModules(DAOQuestions daoQuestions) throws SQLException {
        return daoQuestions.getAllModules();
    }
}
