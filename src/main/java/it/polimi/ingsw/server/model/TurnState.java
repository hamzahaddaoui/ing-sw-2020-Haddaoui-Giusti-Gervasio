package it.polimi.ingsw.server.model;

public enum TurnState {
    SELECTING_CARD,
    PLACING,
    GENERIC_BUILD,
    /*se io costruisco nella cella 2x3
    * se la cella ha altezza 3 -> dome
    * altrimenti blocco
    *
    * tranne per atlante.
    * */
    MOVE,
}
