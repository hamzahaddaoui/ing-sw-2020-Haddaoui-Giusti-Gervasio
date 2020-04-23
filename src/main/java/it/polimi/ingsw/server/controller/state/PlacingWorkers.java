package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;

import static it.polimi.ingsw.server.model.GameModel.*;

public class PlacingWorkers extends State {
    @Override
    public void handleRequest(MessageEvent messageEvent){
        Integer matchID = messageEvent.getMatchID();

        Set<Position> initializedPositions = messageEvent.getInitializedPositions();

        if(initializedPositions.stream().distinct().count() != 2
            || !getPlacingAvailableCells(matchID).containsAll(initializedPositions)
            || !(initializedPositions.stream().allMatch(this::checkPosition)))
        {
            notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), messageEvent.getPlayerID()),matchID)));
        }

        initializedPositions.forEach(position -> placeWorker(matchID, position));

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
