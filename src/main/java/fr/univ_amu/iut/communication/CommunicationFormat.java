package fr.univ_amu.iut.communication;

import java.io.Serializable;

/**
 * The communication format
 * @author LennyGonzales
 */
public class CommunicationFormat implements Serializable {
    private final Flags flag;
    private final Object content;

    public CommunicationFormat(Flags flag) {
        this(flag,null);
    }

    public CommunicationFormat(Flags flag, Object content) {
        this.flag = flag;
        this.content = content;
    }

    public Flags getFlag() {
        return flag;
    }

    public Object getContent() {
        return content;
    }
}
