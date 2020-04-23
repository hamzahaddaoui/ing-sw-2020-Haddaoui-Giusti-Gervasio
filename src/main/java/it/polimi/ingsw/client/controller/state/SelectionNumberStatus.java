package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;


public class SelectionNumberStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) {

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getColoredPlayersNum() == null)
            throw new IllegalArgumentException(" ColoredPlayersNum is empty");
        if(View.getPlayersNum() == null)
            throw new IllegalArgumentException(" PlayerNum is empty");

        return commandCharacter.executeNumberStatus();

    }

    @Override
    public void nextState(Controller ctrl) {
        if(ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.SELECTING_GOD_CARDS) {
            ctrl.setState(new SelectingGodCardsStatus());
            View.setColoredGodCard(View.getGodCards().get(0));
        }
        else ctrl.setState(new WaitingStatus());
    }

}
