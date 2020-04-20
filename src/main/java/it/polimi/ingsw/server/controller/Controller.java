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



    @Override
    public void update(MessageEvent messageEvent){
        executor.submit(() -> new ControllerJob(messageEvent));
    }

    private class ControllerJob implements Runnable{
        private final Map<MatchState, State> stateMap;
        MessageEvent messageEvent;
        public ControllerJob(MessageEvent messageEvent){
            stateMap = Collections.unmodifiableMap(new HashMap<MatchState, State>() {
                {
                    put(MatchState.GETTING_PLAYERS_NUM, new GettingPlayersNum());
                    put(MatchState.WAITING_FOR_PLAYERS, new WaitingForPlayers());
                    put(MatchState.SELECTING_GOD_CARDS, new SelectingGodCards());
                    put(MatchState.SELECTING_SPECIAL_COMMAND, new SelectingSpecialCommand());
                    put(MatchState.PLACING_WORKERS, new PlacingWorkers());
                    put(MatchState.RUNNING, new Running());
                }
            });
            this.messageEvent = messageEvent;
        }

        @Override
        public void run(){
            State controllerBehaviour;
            Integer playerID = messageEvent.getPlayerID();
            Integer matchID = messageEvent.getMatchID();


            if (playerID == null) {
                controllerBehaviour = new FirstPlayerAccess();
                matchID = messageEvent.getClientHandler().getMatchID();
            }
            else if ((matchID != null) && (Objects.equals(getPlayerState(matchID, playerID), PlayerState.ACTIVE))){
                controllerBehaviour = stateMap.get(getMatchState(matchID));
            }
            else {
                return;
            }

            if (messageEvent.isExit()){
                controllerBehaviour.exit(matchID);
            }
            else {
                controllerBehaviour.handleRequest(messageEvent);
                controllerBehaviour.viewNotify(matchID);
            }

        }
    }
}



