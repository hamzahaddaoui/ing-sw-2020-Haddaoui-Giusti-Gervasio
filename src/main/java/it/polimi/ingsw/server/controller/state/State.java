package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.utilities.MessageEvent;
import jdk.internal.org.jline.terminal.impl.LineDisciplineTerminal;

import java.util.Collections;

import static it.polimi.ingsw.server.Server.getClientHandler;
import static it.polimi.ingsw.server.model.GameModel.*;

public abstract class State extends Controller{
    public abstract void handleRequest(Integer matchID, MessageEvent messageEvent);

    public void viewNotify(Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(basicPlayerConfig(message, player)));
    }

    protected MessageEvent basicMatchConfig(MessageEvent messageEvent, Integer matchID){
        messageEvent.setMatchID(matchID);
        messageEvent.setMatchState(getMatchState(matchID));
        messageEvent.setBillboardStatus(Collections.unmodifiableMap(getBillboardStatus(matchID)));
        messageEvent.setMatchPlayers(Collections.unmodifiableMap(getMatchPlayers(matchID)));

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
        ClientHandler clientHandler = getClientHandler(playerID);
        clientHandler.setMatchID(matchID);
        clientHandler.setPlayerID(playerID);
    }
}
