package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class PlacingWorkersStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) {
        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        return commandCharacter.executePlacingWorkerStatus();
    }

    @Override
    public void nextState(Controller ctrl) {
        if (ctrl.getMatchState() == MatchState.RUNNING && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new RunningStatus());
        else ctrl.setState(new WaitingStatus());
    }
}
