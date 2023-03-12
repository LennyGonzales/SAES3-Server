package fr.univ_amu.iut.exceptions;

/**
 * Call when the list of questions is empty
 * @author LennyGonzales
 */
public class EmptyQuestionsListException extends Exception{
    public EmptyQuestionsListException() {
        super("The questions list is empty");
    }
}
