package it.polimi.ingsw.server.model;

public enum MatchState {
    GETTING_PLAYERS_NUM,
    WAITING_FOR_PLAYERS,
    SELECTING_GOD_CARDS,
    SELECTING_SPECIAL_COMMANDS,
    PLACING_WORKER,
    SELECTING_WORKER,
    RUNNING,
    FINISHED
}
