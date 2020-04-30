package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.CardinalDirection;
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
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = gameBoard.getColoredPosition();

        if(coloredPosition == null)
            throw new IllegalArgumentException(" Colored position is null");
        if(gameBoard.getPlacingAvailableCells() == null)
            throw new IllegalArgumentException(" Placing Available cell is null");

        while (true) {
            coloredPosition.setX(checkCorrectCoordinate(coloredPosition.getX()));
            if (gameBoard.getPlacingAvailableCells().contains(coloredPosition)) {
                gameBoard.setColoredPosition(coloredPosition);
                break;
            }
        }
        System.out.println("\nYou are on ( " + coloredPosition.getX() + " , " +coloredPosition.getY() + " )  cell   \n");
        View.doUpdate();
        return false;
    }

    /**
     * Method that adapt the coordinate of the position
     *
     * @param coordinate  X coordinate of the position
     * @return  correct X coordinate
     */
    private int checkCorrectCoordinate(int coordinate){
        if(coordinate>4 || coordinate <0)
            throw new IllegalArgumentException(" Illegal coordinate! ");
        coordinate--;
        if(coordinate<0)
            coordinate+=5;
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

        Position coloredPosition = View.getGameBoard().getColoredPosition();

        if (View.getGameBoard().getStartingPosition()!=null) {
            CardinalDirection offset = View.getGameBoard().getStartingPosition().checkMutualPosition(coloredPosition);
            if (offset == CardinalDirection.WEST ||
                    offset == CardinalDirection.SOUTHWEST ||
                    offset == CardinalDirection.EAST ||
                    offset == CardinalDirection.SOUTHEAST)
                View.getGameBoard().setColoredPosition(View.getGameBoard().getColoredPosition().translateCardinalDirectionToPosition(CardinalDirection.NORTH));
        }
        View.doUpdate();
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
