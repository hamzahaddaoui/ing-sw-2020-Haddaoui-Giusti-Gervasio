package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import java.util.Collections;
import static it.polimi.ingsw.server.Server.getClientHandler;
import static it.polimi.ingsw.server.model.GameModel.*;

public abstract class State extends Controller{
    public abstract void handleRequest(MessageEvent messageEvent);

    public void viewNotify(Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(basicPlayerConfig(message, player)));
    }

    public void exit(Integer matchID){
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> {
                    MessageEvent message = basicMatchConfig(basicPlayerConfig(new MessageEvent(), player), matchID);
                    message.setError(true);
                    message.setFinished(true);
                    notify(message);
                });
        getMatchPlayers(matchID).keySet().forEach(this::clientHandlerReset);
        getMatchPlayers(matchID).keySet().forEach(Server::removeClientSocket);
        deleteMatch(matchID);
    }




    protected MessageEvent basicMatchConfig(MessageEvent messageEvent, Integer matchID){
        messageEvent.setMatchID(matchID);
        messageEvent.setMatchState(getMatchState(matchID));
        messageEvent.setBillboardStatus(Collections.unmodifiableMap(getBillboardStatus(matchID)));
        messageEvent.setMatchPlayers(Collections.unmodifiableMap(getMatchPlayers(matchID)));
        messageEvent.setWinner(getMatchWinner(matchID));
        messageEvent.setFinished(getMatchState(matchID) == MatchState.FINISHED);

        return messageEvent;
    }

    protected MessageEvent basicPlayerConfig(MessageEvent messageEvent, Integer playerID){
        messageEvent.setPlayerID(playerID);
        messageEvent.setPlayerState(getPlayerState(messageEvent.getMatchID(), playerID));
        messageEvent.setTurnState(getPlayerTurn(messageEvent.getMatchID(), playerID));
        return messageEvent;
    }

    protected MessageEvent basicErrorConfig(MessageEvent messageEvent, Integer playerID){
        messageEvent.setPlayerID(playerID);
        messageEvent.setError(true);
        return messageEvent;
    }

    protected MessageEvent basicErrorConfig(MessageEvent messageEvent){
        messageEvent.setError(true);
        return messageEvent;
    }


    protected void checkMatchStart(Integer matchID){
        if (isNumReached(matchID)) {
            startMatch(matchID);
            nextMatchState(matchID);
        }
    }

    protected void clientHandlerUpdate(Integer matchID, Integer playerID){
        getClientHandler(playerID).setMatchID(matchID);
        getClientHandler(playerID).setPlayerID(playerID);
    }

    protected void clientHandlerReset(Integer playerID){
        getClientHandler(playerID).setMatchID(null);
        getClientHandler(playerID).setPlayerID(null);
    }
}
