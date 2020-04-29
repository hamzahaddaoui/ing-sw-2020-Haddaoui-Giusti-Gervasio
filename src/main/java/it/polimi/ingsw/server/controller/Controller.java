package it.polimi.ingsw.server.controller;

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
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    private Map<MatchState, State> stateMap;

    @Override
    public void update(MessageEvent messageEvent){
        executor.submit(()->checkInput(messageEvent));
        //checkInput(messageEvent);
    }


    public void checkInput(MessageEvent messageEvent){
        State controllerBehaviour;
        Integer playerID = messageEvent.getPlayerID();
        Integer matchID = messageEvent.getMatchID();

        if (playerID == null) {
            controllerBehaviour = new FirstPlayerAccess();
        }
        else if ((matchID != null) && (Objects.equals(getPlayerState(matchID, playerID), PlayerState.ACTIVE))){
            controllerBehaviour = getBehaviour(matchID);
        }
        else {
            return;
        }

        if (messageEvent.isExit()){
            controllerBehaviour.exit(matchID);
        }
        else {
            controllerBehaviour.handleRequest(messageEvent);

            playerID = messageEvent.getClientHandler().getPlayerID();
            matchID = messageEvent.getClientHandler().getMatchID();

            if (matchID != null) {
                controllerBehaviour = getBehaviour(matchID);
                controllerBehaviour.viewNotify(getObservers(), matchID);
            }
        }

    }

    State getBehaviour(Integer matchID){
        switch (getMatchState(matchID)){
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
                return null;

        }
    }

    public List<Observer<MessageEvent>> getObs(){
        return getObservers();
    }
}




