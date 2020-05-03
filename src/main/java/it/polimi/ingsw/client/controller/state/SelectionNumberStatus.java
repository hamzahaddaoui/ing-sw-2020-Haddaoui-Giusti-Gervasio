package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectionNumberStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        super.checkMessage(viewObject);

        if (viewObject.length()!=1)
            throw new IllegalArgumentException("\ninput errato!");

        int playersNum = Character.getNumericValue(viewObject.charAt(0));

        if (playersNum == 2 || playersNum == 3) {
            Controller.getMessage().setPlayersNum(playersNum);
            return true;
        }
        else throw new IllegalArgumentException("\nvalore non lecito!");



        /*if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getPlayer().getPlayerNumber() == null)
            throw new IllegalArgumentException(" Player Number is empty");
        if(View.getPlayer().getPlayersNum() == null)
            throw new IllegalArgumentException(" Array of PlayerNumber is empty");

        return commandCharacter.executeNumberStatus();*/
    }

}
