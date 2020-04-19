package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;


import java.awt.event.KeyEvent;

public class StartingStatus extends ControlState  {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        messageEvent.setNickname((String) viewObject);
        return true;
    }

    @Override
    public void nextState(Controller ctrl) {
        if(ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.GETTING_PLAYERS_NUM) {
            ctrl.setState(new Selection_Number_Status());
        }
    }

}
