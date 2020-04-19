package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class Selecting_God_Cards_Status extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();
        commandCharacter.executeGodCardsStatus();

        if (View.getGodCards().size() == View.getPlayersNum()) {
            messageEvent.setGodCards(View.getGodCards());
            return true;
        }
        else return false;
    }

    @Override
    public void nextState(Controller ctrl) {
        if (ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND)
            ctrl.setState(new Selecting_Special_Command_Status());
        else ctrl.setState(new Do_Nothing_Status());
    }
}
