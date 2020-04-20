package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;


public class SelectionNumberStatus extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        boolean result;
        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        result = commandCharacter.executeNumberStatus();

        if(result)
            return true;
        else return false;
    }

    @Override
    public void nextState(Controller ctrl) {
        if(ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.SELECTING_GOD_CARDS) {
            ctrl.setState(new SelectingGodCardsStatus());
        }
        else ctrl.setState(new WaitingStatus());
    }

}
