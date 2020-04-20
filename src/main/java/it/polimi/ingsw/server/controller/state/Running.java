package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import static it.polimi.ingsw.server.model.GameModel.*;

public class Running extends State{
    @Override
    public void handleRequest(MessageEvent messageEvent){
        Integer matchID = messageEvent.getMatchID();
        if (isTerminateTurnAvailable(matchID) && messageEvent.getEndTurn() != null) {
            setHasFinished(matchID);
        }

        else if (isSpecialFunctionAvailable(matchID) && messageEvent.getSpecialFunction() != null) {
            setUnsetSpecialFunction(matchID, messageEvent.getSpecialFunction());
        }

        else{
            playerTurn(matchID, messageEvent.getStartPosition(), messageEvent.getEndPosition());

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
