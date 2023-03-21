package fr.univ_amu.iut.domain;

import java.io.Serializable;

/**
 * Represents a tuple of the Questions table
 * @author LennyGonzales
 */
public class Question implements Serializable {
    private int id;
    private String module;
    private String description;
    private String question;
    private int nbAnswers;
    private int nbCorrectAnswers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNbAnswers() {
        return nbAnswers;
    }

    public void setNbAnswers(int nbAnswers) {
        this.nbAnswers = nbAnswers;
    }

    public int getNbCorrectAnswers() {
        return nbCorrectAnswers;
    }

    public void setNbCorrectAnswers(int nbCorrectAnswers) {
        this.nbCorrectAnswers = nbCorrectAnswers;
    }
}
