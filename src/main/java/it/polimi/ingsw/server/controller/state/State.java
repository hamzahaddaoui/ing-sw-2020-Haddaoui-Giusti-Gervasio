package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.utilities.MessageEvent;

import java.util.Set;

import static it.polimi.ingsw.server.Server.getClientHandler;
import static it.polimi.ingsw.server.model.GameModel.*;

public abstract class State {
    Controller controller = new Controller();

    public abstract void handleRequest(Integer matchID, MessageEvent messageEvent);



    public void checkMatchStart(Integer matchID){
        if (isNumReached(matchID)) {
            startMatch(matchID);
            nextMatchState(matchID);
            controller.changeView(matchID);
        }
    }

    public void clientHandlerUpdate(Integer matchID, Integer playerID){
        ClientHandler clientHandler = getClientHandler(playerID);
        clientHandler.setMatchID(matchID);
        clientHandler.setPlayerID(playerID);
    }

    public void sendToView(MessageEvent messageEvent){
        controller.sendToView(messageEvent);
    }

    public void changeView(Integer matchID, Integer playerID){
        controller.changeView(matchID, playerID);
    }

    public void changeView(Integer matchID){
        controller.changeView(matchID);
    }

    public void inputError(Integer playerID){
        controller.inputError(playerID);
    }


}
