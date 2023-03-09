package fr.univ_amu.iut.domain;

import java.util.Optional;

/**
 * Represents a tuple of the QCM table
 * @author LennyGonzales
 */
public class MultipleChoiceQuestion extends Question {
    private Integer trueAnswer;
    private String answer1;
    private String answer2;
    private String answer3;

    public Optional<Integer> getTrueAnswer() {
        return Optional.ofNullable(trueAnswer);
    }

    public void setTrueAnswer(Integer trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }
}
