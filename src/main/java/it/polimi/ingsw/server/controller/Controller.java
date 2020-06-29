package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.state.*;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.server.model.GameModel.*;

public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    static ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public void update(MessageEvent messageEvent){
        executor.submit(()->checkInput(messageEvent));
    }

    /**
     * Checks the input and detects which behaviour to execute.
     * The behaviour on which the message is analyzed is relayed to the match and player state.
     * If the message is an exit message, then another type of behaviour is activated.
     * The behaviour consists in two parts:
     * 1. Analyzing the message and executing the action
     * 2. Sending a message back to the clients interested by this action
     * @param messageEvent the message sent by the user
     */
    public void checkInput(MessageEvent messageEvent){
        State controllerBehaviour;
        ClientHandler clientHandler = messageEvent.getClientHandler();

        controllerBehaviour = getBehaviour(clientHandler.getMatchID(), clientHandler.getPlayerID());

        if(messageEvent.isExit()){
            controllerBehaviour.exit(getObservers(), clientHandler.getMatchID(), clientHandler.getPlayerID());
        }

        else if (controllerBehaviour.handleRequest(messageEvent)){
            controllerBehaviour = getNotifyBehaviour(clientHandler.getMatchID());
            controllerBehaviour.viewNotify(getObservers(), clientHandler.getMatchID());
        }
    }


    private State getBehaviour(int matchID, int playerID){
        if (matchID == 0){
            return new FirstPlayerAccess();
        }
        else if(getPlayerState(matchID, playerID) == PlayerState.ACTIVE) {
            switch (getMatchState(matchID)) {
                case GETTING_PLAYERS_NUM:
                    return new GettingPlayersNum();
                case WAITING_FOR_PLAYERS:
                    return new WaitingForPlayers();
                case SELECTING_GOD_CARDS:
                    return new SelectingGodCards();
                case SELECTING_SPECIAL_COMMAND:
                    return new SelectingSpecialCommand();
                case PLACING_WORKERS:
                    return new PlacingWorkers();
                case RUNNING:
                    return new Running();
                default:
                    return new None();
            }
        }
        else if (getMatchState(matchID) == MatchState.WAITING_FOR_PLAYERS )
            return new WaitingForPlayers();
        else
            return new None();
    }

    private State getNotifyBehaviour(int matchID){
        if (matchID == 0){
            return new FirstPlayerAccess();
        }
        else{
            switch (getMatchState(matchID)) {
                case GETTING_PLAYERS_NUM:
                    return new GettingPlayersNum();
                case WAITING_FOR_PLAYERS:
                    return new WaitingForPlayers();
                case SELECTING_GOD_CARDS:
                    return new SelectingGodCards();
                case SELECTING_SPECIAL_COMMAND:
                    return new SelectingSpecialCommand();
                case PLACING_WORKERS:
                    return new PlacingWorkers();
                case RUNNING:
                    return new Running();
                case FINISHED:
                    return new Running();
                default:
                    return new None();
            }
        }
    }
}




