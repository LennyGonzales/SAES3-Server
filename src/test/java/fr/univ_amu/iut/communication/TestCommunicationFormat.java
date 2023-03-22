package fr.univ_amu.iut.communication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestCommunicationFormat {

    private CommunicationFormat communicationFormat;

    @BeforeEach
    public void beforeEachTest() {
        communicationFormat = new CommunicationFormat(Flags.BEGIN, "test");
    }


    @Test
    public void shouldGetFlag() {
        assertEquals(Flags.BEGIN, communicationFormat.getFlag());
    }

    @Test
    public void shouldGetContent() {
        assertEquals("test", communicationFormat.getContent());
    }
}
