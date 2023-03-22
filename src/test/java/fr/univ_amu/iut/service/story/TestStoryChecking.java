package fr.univ_amu.iut.service.story;

import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestStoryChecking {

    private StoryChecking storyChecking;
    private List<MultipleChoiceQuestion> multipleChoiceQuestions;
    private List<WrittenResponseQuestion> writtenResponseQuestions;
    private List<Question> questions;

    @Before
    public void beforeEachTest() {
        storyChecking = new StoryChecking();
        multipleChoiceQuestions = new ArrayList<>();
        writtenResponseQuestions = new ArrayList<>();

        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
        multipleChoiceQuestion.setTrueAnswer(2);
        multipleChoiceQuestions.add(multipleChoiceQuestion);

        WrittenResponseQuestion writtenResponseQuestion = new WrittenResponseQuestion();
        writtenResponseQuestion.setTrueAnswer("true answer");
        writtenResponseQuestions.add(writtenResponseQuestion);

        questions = new ArrayList<>();
        questions.addAll(multipleChoiceQuestions);
        questions.addAll(writtenResponseQuestions);
    }

    @Test
    public void shouldPrepareStory() throws CloneNotSupportedException {
        List<Question> questionsPrepared = storyChecking.prepareStory(multipleChoiceQuestions, writtenResponseQuestions);

        assertNotNull(questionsPrepared);
        assertEquals(questions.size(), questionsPrepared.size());

        for(Question question : questionsPrepared) {
            if(question instanceof MultipleChoiceQuestion) { assertEquals(null, ((MultipleChoiceQuestion) question).getTrueAnswer()); }
            else if(question instanceof WrittenResponseQuestion) { assertEquals(null, ((WrittenResponseQuestion) question).getTrueAnswer()); }
        }
    }
}
