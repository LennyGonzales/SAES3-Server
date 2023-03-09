package fr.univ_amu.iut.domain;

/**
 * Represents a tuple of the WRITTENRESPONSE table
 * @author LennyGonzales
 */
public class WrittenResponseQuestion extends Question{
    private String trueAnswer;

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }
}
