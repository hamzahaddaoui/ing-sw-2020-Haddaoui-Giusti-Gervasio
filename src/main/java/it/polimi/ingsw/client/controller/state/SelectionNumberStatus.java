package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectionNumberStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) {

        if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getPlayer().getColoredPlayersNum() == null)
            throw new IllegalArgumentException(" ColoredPlayersNum is empty");
        if(View.getPlayer().getPlayersNum() == null)
            throw new IllegalArgumentException(" PlayerNum is empty");

        return commandCharacter.executeNumberStatus();

    }

}
