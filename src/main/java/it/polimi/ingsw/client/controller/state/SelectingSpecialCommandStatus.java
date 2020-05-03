package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectingSpecialCommandStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        super.checkMessage(viewObject);

        GameBoard gameBoard = View.getGameBoard();

        if (gameBoard.getSelectedGodCards().contains(viewObject)) {
            Controller.getMessage().setGodCard(viewObject);
            return true;
        } else throw new IllegalArgumentException("\ncarta non disponibile");


        /*if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

            InsertCharacter characterView = (InsertCharacter) viewObject;
            CommandCharacter commandCharacter = characterView.apply();

           try {
               return commandCharacter.executeSpecialCommandsStatus();
           } catch (IllegalArgumentException e) {
               e.getMessage();
               return false;
           }*/
    }
}
