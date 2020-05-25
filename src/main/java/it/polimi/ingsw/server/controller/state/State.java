package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.Position;

import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.server.Server.getClientHandler;
import static it.polimi.ingsw.server.Server.main;
import static it.polimi.ingsw.server.model.GameModel.*;

public abstract class State extends Controller{
    public abstract boolean handleRequest(MessageEvent messageEvent);
    public abstract void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID);

    protected MessageEvent basicMatchConfig(MessageEvent messageEvent, Integer matchID){
        messageEvent.setInfo(getMatchInfo(matchID));
        messageEvent.setMatchID(matchID);
        messageEvent.setMatchState(getMatchState(matchID));
        messageEvent.setCurrentPlayer(getCurrentPlayer(matchID));
        messageEvent.setMatchPlayers(Collections.unmodifiableMap(getMatchPlayers(matchID)));
        messageEvent.setMatchColors(Collections.unmodifiableMap(getMatchColors(matchID)));
        messageEvent.setWinner(getMatchWinner(matchID));
        messageEvent.setFinished(getMatchState(matchID) == MatchState.FINISHED);
        if(getMatchState(matchID) == MatchState.RUNNING || getMatchState(matchID) == MatchState.PLACING_WORKERS || getMatchState(matchID) == MatchState.FINISHED)
            messageEvent.setBillboardStatus(getBillboardStatus(matchID));

        return messageEvent;
    }

    protected MessageEvent basicPlayerConfig(MessageEvent messageEvent, Integer playerID){
        int matchID = messageEvent.getMatchID();
        messageEvent.setPlayerID(playerID);
        messageEvent.setPlayerState(getPlayerState(matchID, playerID));
        if(getMatchState(matchID) == MatchState.RUNNING) {
            messageEvent.setTurnState(getPlayerTurn(matchID, playerID));
        }
        return messageEvent;
    }

    protected MessageEvent basicErrorConfig(MessageEvent messageEvent){
        messageEvent.setError(true);
        return messageEvent;
    }


    public void exit(List<Observer<MessageEvent>> observers, int matchID, int playerID){
        if (matchID==0){
            removePlayerWaitingList(playerID);
        }
        else if (getMatchLosers(matchID).containsKey(playerID)){
            return;
        }
        else {
            MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
            message.setFinished(true);
            message.setInfo("A user has disconnected from the match. Closing...");
            getMatchPlayers(matchID)
                    .keySet()
                    .stream()
                    .filter(player -> player != playerID)
                    .forEach(player -> {
                        basicPlayerConfig(message, player);
                        notify(observers, message);
                    });
            getMatchPlayers(matchID).keySet().forEach(this::clientHandlerReset);
            getMatchPlayers(matchID).keySet().forEach(Server::removeClientSocket);
        }
        deleteMatch(matchID);
    }

    protected void checkMatchStart(int matchID){
        if (isNumReached(matchID)) {
            startMatch(matchID);
            nextMatchState(matchID);
        }
    }

    protected boolean checkPosition(Position position){
        return (position.getX() >= 0 && position.getX() <= 4
                && position.getY() >= 0 && position.getY() <= 4);
    }

    protected void clientHandlerReset(int playerID){
        getClientHandler(playerID).setMatchID(0);
        getClientHandler(playerID).setPlayerID(0);
    }
}
