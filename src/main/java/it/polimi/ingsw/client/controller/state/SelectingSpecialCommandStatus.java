package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectingSpecialCommandStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) throws IllegalArgumentException{

        if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

            InsertCharacter characterView = (InsertCharacter) viewObject;
            CommandCharacter commandCharacter = characterView.apply();

           try {
               return commandCharacter.executeSpecialCommandsStatus();
           } catch (IllegalArgumentException e) {
               e.getMessage();
               return false;
           }
    }

}
