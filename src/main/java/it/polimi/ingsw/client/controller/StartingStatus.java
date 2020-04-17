package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.server.model.MatchState;
import it.polimi.ingsw.server.model.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;

import java.awt.event.KeyEvent;

public class StartingStatus extends ControlState  {

    @Override
    public void keyTyped(KeyEvent e, MessageEvent messageEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e, MessageEvent messageEvent) {

    }

    @Override
    public void keyReleased(KeyEvent e, MessageEvent messageEvent) {

    }

    @Override
    public void nextState(Controller ctrl) {
        if( ctrl.getPlayerState().equals( (PlayerState.ACTIVE) )&& ctrl.getMatchState().equals(MatchState.GETTING_PLAYERS_NUM )) {
            ctrl.setState(new Selection_Number_Status() );
        }
    }


}
