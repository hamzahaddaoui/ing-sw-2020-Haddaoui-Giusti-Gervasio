package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

import static it.polimi.ingsw.server.model.GameModel.*;

public class Running extends State{
    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
        if (messageEvent.getEndTurn() != null) {
            setHasFinished(matchID);
        }

        else if (messageEvent.getSpecialFunction() != null) {
            setUnsetSpecialFunction(matchID, messageEvent.getSpecialFunction());
        }

        else{
            playerTurn(matchID, messageEvent.getStartPosition(),messageEvent.getEndPosition());
        }

        switch(getCurrentPlayerState(matchID)){
            case "ACTIVE":
                changeView(matchID,messageEvent.getPlayerID());
            case "IDLE":
                nextMatchTurn(matchID);
                changeView(matchID);
                break;
            case "WIN":
                break;
            case "LOSE":
                break;

        }
    }
}
