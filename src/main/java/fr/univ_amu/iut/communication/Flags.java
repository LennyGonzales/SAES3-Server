package fr.univ_amu.iut.communication;

/**
 * All the flags for the communication with the client | Use to provide or get the reason of the communication
 * @author LennyGonzales
 */
public enum Flags {
    LOGIN,
    LOGIN_SUCCESSFULLY,
    LOGIN_NOT_SUCCESSFULLY,
    MODULES,
    STORY,
    SUMMARY,
    USER_POINTS,
    CODE,
    BEGIN,
    NEW_PLAYER,
    MULTIPLAYER_JOIN,
    SESSION_EXISTS,
    SESSION_NOT_EXISTS,
    CREATE_SESSION,
    LEADERBOARD,
    CANCEL_SESSION,
    LEAVE_SESSION
}
