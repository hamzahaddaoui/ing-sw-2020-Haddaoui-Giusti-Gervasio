package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.state.*;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import java.util.*;
import static it.polimi.ingsw.server.model.GameModel.*;

public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    GameModel model;
    private final Map<MatchState, State> stateMap;

    public Controller(){
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
    }


    @Override
    public void update(MessageEvent messageEvent){
        State controllerBehaviour;
        Integer playerID = messageEvent.getPlayerID();
        Integer matchID = messageEvent.getMatchID();

        if (playerID == null) {
            controllerBehaviour = new FirstPlayerAccess();
        }

        if ((matchID == null) || (! Objects.equals(getPlayerState(matchID, playerID), PlayerState.ACTIVE))){
            return; //ignoro semplicemente
        }

        controllerBehaviour = stateMap.get(getMatchState(matchID));
        controllerBehaviour.handleRequest(matchID,messageEvent);
        controllerBehaviour.viewNotify(matchID);
    }

    public void sendToView(MessageEvent messageEvent){
        notify(messageEvent);
    }
}



