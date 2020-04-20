package it.polimi.ingsw.client.controller.commandsCharacter;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.utilities.CardinalDirection;


public class DCommand implements CommandCharacter {
    @Override
    public boolean executeNumberStatus() {
        View.playersNumChoiceIncrement();
        return false;
    }

    @Override
    public void executePlacingWorkerStatus() {

    }

    /**
     * Method that changes the colored position of the View.
     * <p>
     * If the player hasn't chosen yet the worker, the method changes the colored position to the other worker's position.
     * Else the method changes the colored position to the current position's east one.
     *
     *
     * @return              always false, you can't notify the message yet
     */

    @Override
    public boolean executeRunningStatus() {if (View.getStartingPosition() == null) {

        View.setColoredPosition( View
                .getWorkersPositions()
                .stream()
                .filter(position -> position != View.getColoredPosition())
                .findAny()
                .get());

    }
    else View
                .setColoredPosition(View
                        .getColoredPosition()
                        .translateCardinalDirectionToPosition(CardinalDirection.EAST));
        return false;
    }

    /**
     * Method that change the colored god card of the View.
     * <p>
     * The method sets the current card's left one as the colored god card.
     *
     *
     * @return  always false, you can't notify the message yet
     */

    @Override
    public boolean executeSpecialCommandsStatus() {
        int posColoredCard = View.getSelectedGodCards().indexOf(View.getColoredGodCard());
        if (posColoredCard == View.getPlayersNum()-1)
            posColoredCard = -1;
        View.setColoredGodCard(View.getSelectedGodCards().get(posColoredCard+1));
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
