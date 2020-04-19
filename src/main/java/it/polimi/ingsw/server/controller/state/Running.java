package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import static it.polimi.ingsw.server.model.GameModel.*;

public class Running extends State{
    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
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
                    if (isMatchFinished(matchID)){
                        getMatchPlayers(matchID)
                                .keySet()
                                .stream()
                                .filter(playerID -> getPlayerState(matchID, playerID) == PlayerState.WIN)
                                .findAny()
                                .ifPresent(message::setWinner);
                    }
                    notify(message);
                });

        if (isMatchFinished(matchID)) {
            deleteMatch(matchID);
        }
    }
}
