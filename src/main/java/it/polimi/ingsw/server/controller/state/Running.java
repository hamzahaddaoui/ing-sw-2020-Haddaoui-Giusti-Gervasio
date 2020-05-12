package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.*;

import java.util.List;

import static it.polimi.ingsw.server.model.GameModel.*;

public class Running extends State{
    @Override
    public boolean handleRequest(MessageEvent messageEvent) {
        ClientHandler clientHandler = messageEvent.getClientHandler();
        int matchID = clientHandler.getMatchID();
        int playerID = clientHandler.getPlayerID();
        Position startPosition = messageEvent.getStartPosition();
        Position endPosition = messageEvent.getEndPosition();

        if (messageEvent.getEndTurn() && isTerminateTurnAvailable(matchID)) {
            setHasFinished(matchID);
            return true;
        }

        else if (messageEvent.getSpecialFunction() && (isSpecialFunctionAvailable(matchID).keySet().size() == 2 || isSpecialFunctionAvailable(matchID).get(messageEvent.getStartPosition()))){
            setUnsetSpecialFunction(matchID, messageEvent.getSpecialFunction());
            return true;
        }

        else if (startPosition != null && endPosition != null && checkPosition(startPosition) && checkPosition(endPosition)
                 && getWorkersAvailableCells(matchID).containsKey(startPosition) && getWorkersAvailableCells(matchID).get(startPosition).contains(endPosition)
                && isSpecialFunctionAvailable(matchID).get(startPosition)
        ){

            playerTurn(matchID, startPosition, endPosition);
            return true;
        }

        else{
            notify(List.of(messageEvent.getClientHandler()), basicErrorConfig((basicPlayerConfig(basicMatchConfig(new MessageEvent(), matchID), playerID))));
            return false;
        }
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){

        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> {
                    MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
                    basicPlayerConfig(message, player);
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
