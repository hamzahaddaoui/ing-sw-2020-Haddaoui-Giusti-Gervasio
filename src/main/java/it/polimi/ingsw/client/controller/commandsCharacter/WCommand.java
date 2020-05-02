package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;

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
    public boolean executeRunningStatus() throws IllegalArgumentException{
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = gameBoard.getColoredPosition();
        Position startingPosition = gameBoard.getStartingPosition();

        if (startingPosition == null)
            throw new IllegalArgumentException("command not available yet. Choose your worker first");
        else {
            Set<Position> availableCells = gameBoard.getWorkersAvailableCells(startingPosition);

            if (availableCells.size() == 1) {
                View.doUpdate();
                return false;
            }

            Position currentPosition;
            if (availableCells.size() == 2) {
                currentPosition = availableCells.stream().filter(position -> !position.equals(coloredPosition)).findAny().get();

                if (currentPosition.getX() < coloredPosition.getX())
                    gameBoard.setColoredPosition(currentPosition);
            }
            else {
                CardinalDirection offset = startingPosition.checkMutualPosition(coloredPosition);
                currentPosition = coloredPosition.translateCardinalDirectionToPosition(CardinalDirection.NORTH);

                if (offset == CardinalDirection.SOUTH)
                    currentPosition = currentPosition.translateCardinalDirectionToPosition(CardinalDirection.NORTH);

                if (!gameBoard.getWorkersAvailableCells(startingPosition).contains(currentPosition)) {
                    int offY = coloredPosition.getX() - startingPosition.getX();

                    if (offY == 1)
                        currentPosition = checkPositionHigherX(availableCells, currentPosition, offset);
                    else if (offY == 0)
                        currentPosition = checkPositionSameX(availableCells, currentPosition, offset);
                    else {
                        View.doUpdate();
                        return false;
                    }
                }
                gameBoard.setColoredPosition(currentPosition);
            }
        }
        System.out.println("\nYou are now in position: (" + gameBoard.getColoredPosition().getX() + "," + gameBoard.getColoredPosition().getY() + ")\n");
        View.doUpdate();
        return false;
    }

    private Position checkPositionHigherX(Set<Position> positions, Position pos, CardinalDirection offset) {
        Position current = null;
        switch (offset) {
            case SOUTH:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTHWEST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTHEAST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.EAST);
                if (positions.contains(current))
                    return current;

                current = (pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTH)).translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                break;

            case SOUTHWEST:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH);
                if (positions.contains(current))
                    return current;

                current = (pos.translateCardinalDirectionToPosition(CardinalDirection.EAST)).translateCardinalDirectionToPosition(CardinalDirection.EAST);
                if (positions.contains(current))
                    return current;

                current = checkPositionSameX(positions,pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH),CardinalDirection.WEST);
                if (current.getX() == pos.getX() && current.getY() == current.getY())
                    current = current.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                break;

            case SOUTHEAST:
                current = pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH);
                if (positions.contains(current))
                    return current;

                current = (pos.translateCardinalDirectionToPosition(CardinalDirection.WEST)).translateCardinalDirectionToPosition(CardinalDirection.WEST);
                if (positions.contains(current))
                    return current;

                current = checkPositionSameX(positions,pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH),CardinalDirection.EAST);
                if (current.getX() == pos.getX() && current.getY() == current.getY())
                    current = current.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                break;
        }
        return current;
    }

    private Position checkPositionSameX(Set<Position> positions, Position pos, CardinalDirection offset) {
        Position current = null;

        switch (offset) {
            case WEST:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.EAST);
                if (positions.contains(current))
                    return current;

                current = current.translateCardinalDirectionToPosition(CardinalDirection.EAST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                break;
            case EAST:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                if (positions.contains(current))
                    return current;

                current = current.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                break;
        }
        return current;
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
