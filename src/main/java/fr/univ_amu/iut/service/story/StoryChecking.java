package fr.univ_amu.iut.service.story;

import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.service.users.UsersChecking;
import fr.univ_amu.iut.service.dao.DAOMultipleChoiceQuestions;
import fr.univ_amu.iut.service.dao.DAOQuestions;
import fr.univ_amu.iut.service.dao.DAOUsers;
import fr.univ_amu.iut.service.dao.DAOWrittenResponseQuestions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supports the stories management
 * @author LennyGonzales
 */
public class StoryChecking {
    private final HashMap<Integer, Integer> currentMultipleChoiceResponse;
    private final HashMap<Integer, String> currentWrittenResponse;
    private List<Question> story;
    private int currentCorrectAnswers;

    public StoryChecking() {
        currentMultipleChoiceResponse = new HashMap<>();
        currentWrittenResponse = new HashMap<>();
        currentCorrectAnswers = 0;
    }

    /**
     * Get the current story
     * @return the current story
     */
    public List<Question> getStory() {
        return story;
    }

    /**
     * Create a story
     * @param module module of the story
     * @param numberOfQuestions number of questions in the story
     * @param daoMultipleChoiceQuestions interface (Reversing dependencies) to access database
     * @param daoWrittenResponseQuestions interface (Reversing dependencies) to access database
     * @return a list of Question (story)
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     * @throws CloneNotSupportedException if the clone in StoryChecking.getStory isn't supported
     */
    public List<Question> createStory(String module, int numberOfQuestions, DAOMultipleChoiceQuestions daoMultipleChoiceQuestions, DAOWrittenResponseQuestions daoWrittenResponseQuestions) throws SQLException, CloneNotSupportedException {
        List<MultipleChoiceQuestion> multipleChoiceQuestionList = daoMultipleChoiceQuestions.getACertainNumberOfQCM(numberOfQuestions/2, module);
        List<WrittenResponseQuestion> writtenResponseQuestionList = daoWrittenResponseQuestions.getACertainNumberOfWrittenResponseQuestion(numberOfQuestions/2, module);

        return prepareStory(multipleChoiceQuestionList, writtenResponseQuestionList);
    }

    /**
     * Prepare the story before sending to the user
     * @param multipleChoiceQuestionList the list of multiple choice questions
     * @param writtenResponseQuestionList the list of written response questions
     * @return the list prepared
     * @throws CloneNotSupportedException if the class doesn't implement the Cloneable interface
     */
    public List<Question> prepareStory(List<MultipleChoiceQuestion> multipleChoiceQuestionList, List<WrittenResponseQuestion> writtenResponseQuestionList) throws CloneNotSupportedException {
        prepareMultipleChoiceQuestions(multipleChoiceQuestionList);
        prepareWrittenResponseQuestions(writtenResponseQuestionList);

        story = new ArrayList<>();
        story.addAll(multipleChoiceQuestionList);
        story.addAll(writtenResponseQuestionList);
        return story;
    }

    /**
     * Prepare the list of multiple choice questions
     * @param multipleChoiceQuestionList the list of multiple choice questions
     * @return the list prepared
     * @throws CloneNotSupportedException if the class doesn't implement the Cloneable interface
     */
    public void prepareMultipleChoiceQuestions(List<MultipleChoiceQuestion> multipleChoiceQuestionList) throws CloneNotSupportedException {
        MultipleChoiceQuestion multipleChoiceQuestionClone;
        for(MultipleChoiceQuestion question : multipleChoiceQuestionList) {
            multipleChoiceQuestionClone = question.clone(); // => not the same reference (setTrueAnswer isn't going to modify currentWrittenResponse HashMap
            currentMultipleChoiceResponse.put(question.getId(), multipleChoiceQuestionClone.getTrueAnswer());
            question.setTrueAnswer(null);
        }
    }

    /**
     * Prepare the list of written response questions
     * @param writtenResponseQuestionList the list of multiple choice questions
     * @return the list prepared
     * @throws CloneNotSupportedException if the class doesn't implement the Cloneable interface
     */
    public void prepareWrittenResponseQuestions(List<WrittenResponseQuestion> writtenResponseQuestionList) throws CloneNotSupportedException {
        WrittenResponseQuestion writtenResponseQuestionClone;
        for(WrittenResponseQuestion question : writtenResponseQuestionList) {
            writtenResponseQuestionClone = question.clone();    // => not the same reference (setTrueAnswer isn't going to modify currentWrittenResponse HashMap
            currentWrittenResponse.put(question.getId(), writtenResponseQuestionClone.getTrueAnswer());
            question.setTrueAnswer(null);
        }
    }

    /**
     *  - Prepare the HashMap to send to the client/user
     *  - For each question, increment the number of answers and the number of good answers in the database
     *  - Update the user points
     * @param story story received
     * @param usersChecking an instance of UsersChecking
     * @param daoUsers interface (Reversing dependencies)
     * @return a hashmap containing Question and if the user sent corrects answers
     * @throws UserIsNotInTheDatabaseException if the user isn't in the database
     * @throws SQLException if a SQL request in the Login.serviceLogin() method didn't go well
     */
    public HashMap<Question, Boolean> summary(Object story, UsersChecking usersChecking, DAOUsers daoUsers, DAOQuestions daoQuestions) throws UserIsNotInTheDatabaseException, SQLException {
        List<Question> storyReceived = (List<Question>) story;

        HashMap<Question, Boolean> storyToSend = prepareSummary(storyReceived);
        incrementNumberOfAnswersAndNumberOfCorrectAnswers(storyToSend, daoQuestions);    // Increment the number of answers and the number of good answers

        usersChecking.updateUserPoints((int) (10 * (currentCorrectAnswers - (storyReceived.size()/2.0)) * (1 - (usersChecking.getUser().getPoints()) / 2000.0)), daoUsers);   // function to calculate the new user points
        currentCorrectAnswers = 0;

        return storyToSend;
    }

    /**
     * Prepare the summary to send to the user/client
     * @param storyReceived the story received
     * @return the summary prepared
     */
    public HashMap<Question, Boolean> prepareSummary(List<Question> storyReceived) {
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
        return storyToSend;
    }

    /**
     * Increment the number of answers for a given list of questions id
     * @param story the story
     * @param daoQuestions interface (Reversing dependencies)
     * @return if the incrementation worked
     * @throws SQLException if a SQL request in the incrementNbAnswers method didn't go well
     */
    public boolean incrementNumberOfAnswersAndNumberOfCorrectAnswers(HashMap<Question, Boolean> story, DAOQuestions daoQuestions) throws SQLException {
        List<Integer> allListIds = new ArrayList<>();
        List<Integer> correctAnswersListIds = new ArrayList<>();
        for (Map.Entry<Question, Boolean> entry : story.entrySet()) {
            allListIds.add(entry.getKey().getId());
            if(entry.getValue()) { correctAnswersListIds.add(entry.getKey().getId()); } // Get the correct answers questions id
        }
        return daoQuestions.incrementNbAnswers(correctAnswersListIds, allListIds);
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
