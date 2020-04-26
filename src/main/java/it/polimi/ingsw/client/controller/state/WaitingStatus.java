package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class WaitingStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) {
        //forse faccio qualcosa nella view
        if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        return false;
    }

    @Override
    public void nextState(Controller ctrl) {
        if (ctrl.getMatchState() == MatchState.SELECTING_GOD_CARDS && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new SelectingGodCardsStatus());
        else if (ctrl.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new SelectingSpecialCommandStatus());
        else if (ctrl.getMatchState() == MatchState.PLACING_WORKERS && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new PlacingWorkersStatus());
        else if (ctrl.getMatchState() == MatchState.RUNNING && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new RunningStatus());
        else ctrl.setState(this);
    }
}
