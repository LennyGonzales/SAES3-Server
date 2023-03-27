package fr.univ_amu.iut.communication;

/**
 * All the flags for the communication with server | Use to provide or get the reason of the communication
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
    SESSION_EXISTS,
    SESSION_NOT_EXISTS,
    BEGIN,
    NEW_PLAYER,
    MULTIPLAYER_JOIN,
    CREATE_SESSION,
    LEADERBOARD,
    CANCEL_SESSION,
    LEAVE_SESSION
}
