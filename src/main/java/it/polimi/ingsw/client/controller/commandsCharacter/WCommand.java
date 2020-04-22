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

        while(View.getPlacingAvailableCells().contains(coloredPosition)){
            coloredPosition.setY(checkCorrectCoordinate(coloredPosition.getY()));
            View.setColoredPosition(coloredPosition);}
        return false;
    }

    /**
     * Method that adapt the coordinate of the position
     *
     * @param coordinate  Y coordinate of the position
     * @return  correct Y coordinate
     */
    private int checkCorrectCoordinate(int coordinate){
        coordinate++;
        if(coordinate>4) coordinate-=5;
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
            coloredPosition.setY(checkCorrectCoordinate(coloredPosition.getY()));
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
