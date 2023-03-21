package fr.univ_amu.iut.domain;

/**
 * Represents a tuple of the WrittenResponseQuestions table
 * @author LennyGonzales
 */
public class WrittenResponseQuestion extends Question implements Cloneable{
    private String trueAnswer;

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    @Override
    public WrittenResponseQuestion clone() throws CloneNotSupportedException
    {
        return (WrittenResponseQuestion) super.clone();
    }
}
