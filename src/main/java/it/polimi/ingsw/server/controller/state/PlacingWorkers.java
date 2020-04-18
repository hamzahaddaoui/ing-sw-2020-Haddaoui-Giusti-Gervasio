package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

import static it.polimi.ingsw.server.model.GameModel.*;

public class PlacingWorkers extends State {
    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
        placeWorker(matchID, messageEvent.getEndPosition());
        if (hasPlacedWorkers(matchID)) {
            nextMatchTurn(matchID);
            if (hasPlacedWorkers(matchID)) {
                nextMatchState(matchID);
            }
            changeView(matchID);
        }
    }
}
