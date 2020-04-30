package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Position;

public class FCommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {
        //messaggio View: "non fa nulla"
        return false;
    }

    @Override
    public boolean executePlacingWorkerStatus() {
        return false;
    }

    /**
     * Method that recognize if the player wants to set the special function.
     * <p>
     * If the View's special function available boolean is true, the method sets the message's special function boolean as true.
     *
     *
     * @return              true if you can send the message, false otherwise
     */
    @Override
    public boolean executeRunningStatus() throws IllegalArgumentException {
        Position start = View.getGameBoard().getStartingPosition();
        MessageEvent msg = Controller.getMessage();

        if (start == null)
            throw new IllegalArgumentException("you need to choose the current worker first");

        if (View.getPlayer().isSpecialFunctionAvailable(start)) {
            msg.setStartPosition(start);
            msg.setSpecialFunction(true);
            System.out.println("\n You have chosen to enable the special function! \n");
            View.doUpdate();
            return true;
        }
        else throw new IllegalArgumentException("the special function is not available for this worker");
    }

    @Override
    public boolean executeSpecialCommandsStatus() {
        return false;
    }

    @Override
    public boolean executeSelectingGodCardsStatus() {
        return false;
    }

}
