package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class Selecting_Special_Command_Status extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();
        commandCharacter.executeSpecialCommandsStatus();
        //messageEvent.setGodCard(View.getGodCard);
        return true;
    }

    @Override
    public void nextState(Controller ctrl) {
        if (ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.PLACING_WORKERS)
            ctrl.setState(new Placing_Workers_Status());
        else ctrl.setState(new Do_Nothing_Status());
    }
}
