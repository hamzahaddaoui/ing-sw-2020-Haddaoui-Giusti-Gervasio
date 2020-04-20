package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import static it.polimi.ingsw.server.model.GameModel.*;

public class Running extends State{
    @Override
    public void handleRequest(MessageEvent messageEvent){
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
            if (isSpecialFunctionAvailable(matchID)){
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
    public void viewNotify(Integer matchID){
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> {
                    MessageEvent message = basicMatchConfig(basicPlayerConfig(new MessageEvent(), player), matchID);
                    if (getPlayerState(matchID,player) == PlayerState.ACTIVE){
                        message.setWorkersAvailableCells(getWorkersAvailableCells(matchID));
                        message.setSpecialFunctionAvailable(isSpecialFunctionAvailable(matchID));
                        message.setTerminateTurnAvailable(isTerminateTurnAvailable(matchID));
                    }
                    notify(message);
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
