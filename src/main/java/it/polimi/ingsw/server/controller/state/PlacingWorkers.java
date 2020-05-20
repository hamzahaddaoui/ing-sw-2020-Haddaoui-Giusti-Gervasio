package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.server.model.GameModel.*;

public class PlacingWorkers extends State {



    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        ClientHandler clientHandler = messageEvent.getClientHandler();
        int matchID = clientHandler.getMatchID();
        int playerID = clientHandler.getPlayerID();

        Set<Position> initializedPositions = messageEvent.getInitializedPositions();

        if((initializedPositions.stream().distinct().count() != 2) || ! getPlacingAvailableCells(matchID).containsAll(initializedPositions)  || ! (initializedPositions.stream().allMatch(this::checkPosition))) {
            notify(List.of(messageEvent.getClientHandler()), basicErrorConfig((basicPlayerConfig(basicMatchConfig(new MessageEvent(), matchID), playerID))));
            return false;
        }

        initializedPositions.forEach(position -> placeWorker(matchID, position));

        nextMatchTurn(matchID);
        if (hasPlacedWorkers(matchID)) {
            nextMatchState(matchID);
        }
        return true;
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
        getMatchPlayers(matchID).keySet().forEach(player -> {
                    MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
                    if (getPlayerState(matchID,player) == PlayerState.ACTIVE)
                        message.setAvailablePlacingCells(getPlacingAvailableCells(matchID));
                    notify(observers, basicPlayerConfig(message, player));
                });
    }

}
