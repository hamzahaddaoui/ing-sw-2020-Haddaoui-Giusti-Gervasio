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
                put(MatchState.SELECTING_SPECIAL_COMMAND, new SelectingGodCards());
                put(MatchState.PLACING_WORKERS, new PlacingWorkers());
                put(MatchState.RUNNING, new Running());
            }
        });
    }


    @Override
    public void update(MessageEvent messageEvent){
        if (!(messageEvent.getMsgType().equals("CONTROLLER_TO_CONTROLLER"))){
            return;
        }

        Integer playerID = messageEvent.getPlayerID();

        if (playerID == null) {
            new FirstPlayerAccess().handleRequest(null, messageEvent);
            return;
        }

        Integer matchID = messageEvent.getMatchID();
        if ((matchID == null) || (! Objects.equals(getPlayerState(matchID, playerID), PlayerState.ACTIVE))){
            inputError(playerID);
            return;
        }

        stateMap.get(getMatchState(matchID)).handleRequest(matchID,messageEvent);
    }

    public void sendToView(MessageEvent messageEvent){
        notify(messageEvent);
    }


    public void changeView(Integer matchID, Integer playerID){
        notify(new MessageEvent(
                "CONTROLLER_CHANGE_VIEW",
                playerID,
                matchID,
                getPlayerState(matchID, playerID),
                getMatchState(matchID)));
    }

    public void changeView(Integer matchID){
        Set<Integer> matchPlayers = getMatchPlayers(matchID).keySet();
        for (Integer playerID: matchPlayers){
            notify(new MessageEvent(
                    "CONTROLLER_CHANGE_VIEW",
                    playerID,
                    matchID,
                    getPlayerState(matchID, playerID),
                    getMatchState(matchID)));
        }
    }

    public void inputError(Integer playerID){
        notify(new MessageEvent(playerID,true));
    }

    public void updateView(){

    }
}





