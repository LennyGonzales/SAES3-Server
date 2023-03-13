package fr.univ_amu.iut.service;

import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import fr.univ_amu.iut.service.dao.DAOUsers;
import fr.univ_amu.iut.service.dao.DAOWrittenResponseQuestions;

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
     */
    public List<Question> getStory(String module, int numberOfQuestions, DAOMultipleChoiceQuestions daoMultipleChoiceQuestions, DAOWrittenResponseQuestions daoWrittenResponseQuestions) throws SQLException, CloneNotSupportedException {
        List<MultipleChoiceQuestion> multipleChoiceQuestionList = daoMultipleChoiceQuestions.getACertainNumberOfQCM(numberOfQuestions, module);
        List<WrittenResponseQuestion> writtenResponseQuestionList = daoWrittenResponseQuestions.getACertainNumberOfWrittenResponseQuestion(numberOfQuestions, module);

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
     *
     * @param story story received
     * @return
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


    public int getUserPoints(UsersChecking usersChecking) {
        return usersChecking.getUser().getPoints();
    }
}
