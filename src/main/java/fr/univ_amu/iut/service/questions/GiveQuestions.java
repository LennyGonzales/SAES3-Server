package fr.univ_amu.iut.service.questions;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.exceptions.UserIsNotInTheDatabaseException;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.EmptyQuestionsListException;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Supports the quiz/story (give questions and verify the answer)
 * @author LennyGonzales
 */
public class GiveQuestions {
    private final List<MultipleChoiceQuestion> multipleChoiceQuestionList;
    private final List<WrittenResponseQuestion> writtenResponseQuestionList;
    private final Communication communication;
    private List<Question> story;
    private HashMap<Integer, String> correctionWrittenResponse;
    private HashMap<Integer, Integer> correctionMultipleChoiceResponse;


    public GiveQuestions(Communication communication, List<MultipleChoiceQuestion> multipleChoiceQuestionList, List<WrittenResponseQuestion> writtenResponseQuestionList) throws EmptyQuestionsListException, IOException {
        this.communication = communication;
        if ((multipleChoiceQuestionList.size() < 1) && (writtenResponseQuestionList.size() < 1)) { // Verify if there are questions in the database
            throw new EmptyQuestionsListException();    // If not, throw exception
        }
        this.multipleChoiceQuestionList = multipleChoiceQuestionList;
        this.writtenResponseQuestionList = writtenResponseQuestionList;
        this.correctionWrittenResponse = new HashMap<>();
        this.correctionMultipleChoiceResponse = new HashMap<>();
    }

    /**
     * Send story to the user
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendStory() throws IOException {
        story = Stream.concat(multipleChoiceQuestionList.stream(), writtenResponseQuestionList.stream())
                .collect(Collectors.toList());
        communication.sendMessage(new CommunicationFormat(Flags.STORY, story));
    }

    /**
     * Prepare the correction and nullified the true answer before sending to the client
     */
    public void prepareStory() {
        for(MultipleChoiceQuestion question : multipleChoiceQuestionList) {
            if(question.getTrueAnswer() != null) {
                correctionMultipleChoiceResponse.put(question.getId(), question.getTrueAnswer());
                question.setTrueAnswer(null);
                continue;
            }
            correctionMultipleChoiceResponse.put(question.getId(), null);
        }
        for(WrittenResponseQuestion question : writtenResponseQuestionList) {
            if(question.getTrueAnswer() != null) {
                correctionWrittenResponse.put(question.getId(), question.getTrueAnswer());
                question.setTrueAnswer(null);
                continue;
            }
            correctionWrittenResponse.put(question.getId(), null);
        }
    }

    public void run() {
        try {
            prepareStory();
            sendStory();

            Summary summary = new Summary(communication, correctionWrittenResponse, correctionMultipleChoiceResponse);
            summary.initialize();
        } catch (IOException | UserIsNotInTheDatabaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
