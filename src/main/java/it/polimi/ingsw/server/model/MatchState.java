package it.polimi.ingsw.server.model;

public enum MatchState {
    GETTING_PLAYERS_NUM,
    WAITING_FOR_PLAYERS,
    SELECTING_GOD_CARDS,
    SELECTING_SPECIAL_COMMAND,
    PLACING_WORKERS,
    RUNNING,
    FINISHED {
        @Override
        public MatchState next(){
            return null;
        }
    };

    public MatchState next(){
        return values()[ordinal() + 1];
    }
}
