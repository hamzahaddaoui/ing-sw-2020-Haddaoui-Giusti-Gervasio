package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.*;

import java.util.List;

import static it.polimi.ingsw.server.model.GameModel.*;

public class Running extends State{
    @Override
    public void handleRequest(MessageEvent messageEvent) {
        Integer matchID = messageEvent.getMatchID();
        if (messageEvent.getEndTurn()) {
            if (isTerminateTurnAvailable(matchID)){
                setHasFinished(matchID);
            }
            else {
                notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), messageEvent.getPlayerID()), matchID)));
            }
        }

        else if (messageEvent.getSpecialFunction()) {
            if (isSpecialFunctionAvailable(matchID).keySet().size() == 2 || isSpecialFunctionAvailable(matchID).get(messageEvent.getStartPosition())){
                setUnsetSpecialFunction(matchID, messageEvent.getSpecialFunction());
            }
            else{
                notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), messageEvent.getPlayerID()),matchID)));
            }
        }

        else{
            Position startPosition = messageEvent.getStartPosition();
            Position endPosition = messageEvent.getEndPosition();

            if (checkPosition(startPosition) && checkPosition(endPosition)
                && getWorkersAvailableCells(matchID).containsKey(startPosition)
                && getWorkersAvailableCells(matchID).get(startPosition).contains(endPosition)){

                playerTurn(matchID, startPosition, endPosition);
            }
            else{
                notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), messageEvent.getPlayerID()), matchID)));
            }
        }
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> {
                    MessageEvent message = basicMatchConfig(basicPlayerConfig(new MessageEvent(), player), matchID);
                    if (getPlayerState(matchID,player) == PlayerState.ACTIVE){
                        message.setWorkersAvailableCells(getWorkersAvailableCells(matchID));
                        message.setSpecialFunctionAvailable(isSpecialFunctionAvailable(matchID));
                        message.setTerminateTurnAvailable(isTerminateTurnAvailable(matchID));
                    }
                    notify(observers, message);
                });
        if (getMatchState(matchID) == MatchState.FINISHED) {
            getMatchPlayers(matchID).keySet().forEach(this::clientHandlerReset);
            getMatchPlayers(matchID).keySet().forEach(Server::removeClientSocket);
            deleteMatch(matchID);
        }

        else if (getMatchLosers(matchID).size()!=0) {
            getMatchLosers(matchID).keySet().forEach(this::clientHandlerReset);
            getMatchLosers(matchID).keySet().forEach(Server::removeClientSocket);
            removeLosers(matchID);
        }
    }

}
