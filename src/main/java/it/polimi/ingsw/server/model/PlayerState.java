package it.polimi.ingsw.server.model;

public enum PlayerState {
    INITIALIZED {
        @Override
        public PlayerState next(){
            return ACTIVE;
        }
    },
    IDLE,
    ACTIVE{
        @Override
        public PlayerState next(){
            return IDLE;
        }
    },
    WIN,
    LOST;

    public PlayerState next(){
        return values()[ordinal() + 1];
    }
}
