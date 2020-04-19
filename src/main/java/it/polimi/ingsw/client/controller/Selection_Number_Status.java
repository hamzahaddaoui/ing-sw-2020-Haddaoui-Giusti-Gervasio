package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;


public class Selection_Number_Status extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        InsertCharacter characterView = (InsertCharacter) viewObject;
        characterView.execution("Selection_Number_Status");
        return true;
    }

    @Override
    public void nextState(Controller ctrl) {
        if( ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.GETTING_PLAYERS_NUM) {
            //ctrl.setState(new );
        }
    }

}
