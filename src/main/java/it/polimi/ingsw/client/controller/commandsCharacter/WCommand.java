package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Position;

public class WCommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {
        //messaggio View: "non fa nulla"
        return false;
    }


    /**
     * Method that change the selectedPosition with the first next empty cells behind the selectedPosition
     * it can change the side of the billboard selection
     *
     * @return  false always
     */
    @Override
    public boolean executePlacingWorkerStatus() {

        Position coloredPosition = View.getColoredPosition();

        while (true) {
            coloredPosition.setY(checkCorrectCoordinate(coloredPosition.getY()));
            if (View.getPlacingAvailableCells().contains(coloredPosition)) {
                View.setColoredPosition(coloredPosition);
                break;
            }
        }
        return false;
    }

    /**
     * Method that adapt the coordinate of the position
     *
     * @param coordinate  X coordinate of the position
     * @return  correct X coordinate
     */
    private int checkCorrectCoordinate(int coordinate){
        coordinate--;
        if(coordinate<0) coordinate+=5;
        return coordinate;
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

        Position coloredPosition = View.getColoredPosition();

        if (View.getStartingPosition()!=null) {
            coloredPosition.setX(checkCorrectCoordinate(coloredPosition.getX()));
            View.setColoredPosition(coloredPosition);
        }
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
