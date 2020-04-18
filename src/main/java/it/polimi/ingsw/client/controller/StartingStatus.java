package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.server.model.MatchState;
import it.polimi.ingsw.server.model.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;

import java.awt.event.KeyEvent;

public class StartingStatus extends ControlState  {

    @Override
    public void nextState(Controller ctrl) {
        if(ctrl.getPlayerState().equals("ACTIVE") && ctrl.getMatchState().equals("GETTING_PLAYERS_NUM")) {
            ctrl.setState(new Selection_Number_Status());
        }
    }

}
