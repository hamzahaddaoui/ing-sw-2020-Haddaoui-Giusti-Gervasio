package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.MessageEvent;

public class WCommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {

        //messaggio View: "non fa nulla"
        return false;
    }

    @Override
    public void executePlacingWorkerStatus() {

    }

    /**
     * Method that changes the colored position of the View.
     * <p>
     * If the player has chosen the worker, the method changes the colored position to the current position's north one.
     *
     *
     * @return              always false, you can't notify the message yet
     */

    @Override
    public boolean executeRunningStatus() {

        if (View.getStartingPosition()!=null)
            View
                    .setColoredPosition(View
                            .getColoredPosition()
                            .translateCardinalDirectionToPosition(CardinalDirection.NORTH));
        return false;

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
