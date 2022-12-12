package fr.univ_amu.iut.server.questions.exceptions;

public class EmptyQuestionsListException extends Exception{
    public EmptyQuestionsListException() {
        super("The questions list is empty");
    }
}
