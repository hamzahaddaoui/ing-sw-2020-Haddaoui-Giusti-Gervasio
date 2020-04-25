package it.polimi.ingsw.client.controller.commandsCharacter;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
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
    public boolean executeRunningStatus() {
        Position start = View.getStartingPosition();
        MessageEvent msg = Controller.getMessage();
        if (start!=null && View.isSpecialFunctionAvailable(start)) {
            msg.setStartPosition(start);
            msg.setSpecialFunction(true);
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
