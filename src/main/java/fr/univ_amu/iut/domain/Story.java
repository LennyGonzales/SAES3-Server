package fr.univ_amu.iut.domain;

/**
 * Represents a tuple of the Stories table
 * @author LennyGonzales
 */
public class Story {
    private String module;

    public Story(String module) {
        this.module = module;
    }


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
