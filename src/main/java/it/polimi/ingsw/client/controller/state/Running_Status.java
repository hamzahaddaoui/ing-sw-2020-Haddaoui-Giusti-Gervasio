package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MessageEvent;

public class Running_Status extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();
        commandCharacter.executeRunningStatus();
        return true;
    }

    @Override
    public void nextState(Controller ctrl) {
        ctrl.setState(new Do_Nothing_Status());
    }
}