package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

public class WaitingForPlayers extends State{
    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
        //potrei far cambiare numero di giocatori...
    }
}
