package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.Position;
import javafx.geometry.Pos;

public class SCommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {
        //messaggio View: "non fa nulla"
        return false;
    }

    /**
     * Method that change the selectedPosition with the first next empty cells in front of the actual selectedPosition
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
        coordinate++;
        if(coordinate>4)
            coordinate-=5;
        return coordinate;
    }

    /**
     * Method that changes the colored position of the View.
     * <p>
     * If the player has chosen the worker, the method changes the colored position to the current position's south one.
     *
     *
     * @return              always false, you can't notify the message yet
     */
    @Override
    public boolean executeRunningStatus() throws IllegalArgumentException{
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = gameBoard.getColoredPosition();
        Position startingPosition = gameBoard.getStartingPosition();


        if (startingPosition==null)
            throw new IllegalArgumentException("command not available yet. Choose your worker first");
        else{
            CardinalDirection offset = startingPosition.checkMutualPosition(coloredPosition);
            if (offset == CardinalDirection.WEST ||
                    offset == CardinalDirection.NORTHWEST ||
                    offset == CardinalDirection.EAST ||
                    offset == CardinalDirection.NORTHEAST) {
                gameBoard.setColoredPosition(coloredPosition.translateCardinalDirectionToPosition(CardinalDirection.SOUTH));
                coloredPosition = gameBoard.getColoredPosition();
                if (gameBoard.getWorkersAvailableCells(startingPosition).contains(coloredPosition))
                    System.out.println("\nYou are now in position: (" + coloredPosition.getX() + "," + coloredPosition.getY() + ") who's available for your worker");
                else System.out.println("\nYou are now in position: (" + coloredPosition.getX() + "," + coloredPosition.getY() + ") who's not available for your worker " +
                    "so you can't select this position!"); }
            else throw new IllegalArgumentException("you are trying to exit from your neighboring cells. Nice try! :)");
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
}
