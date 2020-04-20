package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;

public class RunningStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) {
        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();
        return commandCharacter.executeRunningStatus();
    }

    @Override
    public void nextState(Controller ctrl) {
        ctrl.setState(new WaitingStatus());
    }
}
