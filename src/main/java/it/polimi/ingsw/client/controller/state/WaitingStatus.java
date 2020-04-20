package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class WaitingStatus extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        //forse faccio qualcosa nella view
        return false;
    }

    @Override
    public void nextState(Controller ctrl) {
        if (ctrl.getMatchState() == MatchState.SELECTING_GOD_CARDS && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new SelectingGodCardsStatus());
        else if (ctrl.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new Selecting_Special_Command_Status());
        else if (ctrl.getMatchState() == MatchState.PLACING_WORKERS && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new PlacingWorkersStatus());
        else if (ctrl.getMatchState() == MatchState.RUNNING && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new Running_Status());
        else ctrl.setState(this);
    }
}
