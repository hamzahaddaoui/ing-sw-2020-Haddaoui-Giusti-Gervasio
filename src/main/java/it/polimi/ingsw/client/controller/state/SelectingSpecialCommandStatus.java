package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

public class SelectingSpecialCommandStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) throws NullPointerException{

        if (viewObject == null)
            throw new NullPointerException("Null message!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();
        return commandCharacter.executeSpecialCommandsStatus();
    }

    @Override
    public void nextState(Controller ctrl) {
        if (ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.PLACING_WORKERS){
            ctrl.setState(new PlacingWorkersStatus());
            View.getColoredPosition().set(0,0);
        }
        else ctrl.setState(new WaitingStatus());
    }
}
