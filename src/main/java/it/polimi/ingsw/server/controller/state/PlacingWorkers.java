package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

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
        }
    }

    @Override
    public void viewNotify(Integer matchID){
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> {
                    MessageEvent message = basicMatchConfig(basicPlayerConfig(new MessageEvent(), player), matchID);
                    if (getPlayerState(matchID,player) == PlayerState.ACTIVE)
                        message.setAvailablePlacingCells(getPlacingAvailableCells(matchID));
                    notify(message);
                });
    }

}
