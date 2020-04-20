package it.polimi.ingsw.client.controller.commandsCharacter;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;

public class ECommand implements CommandCharacter {
    @Override
    public boolean executeNumberStatus() {

        //messaggio View: "non fa nulla"
        return false;
    }

    @Override
    public void executePlacingWorkerStatus() {

    }

    /**
     * Method that recognize if the player wants to end the turn.
     * <p>
     * If the View's terminate turn boolean is true, the method sets the message's end turn boolean as true.
     *
     *
     * @return              true if you can send the message, false otherwise
     */

    @Override
    public boolean executeRunningStatus() {
        if (View.isTerminateTurnAvailable()) {
        Controller.getMessage().setEndTurn(true);
        return true;
        }
        else return false;
    }

    @Override
    public boolean executeSpecialCommandsStatus() {
        return false;
    }

    @Override
    public boolean executeSelectingGodCardsStatus() {
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

}
