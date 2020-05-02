package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.Set;

public class DCommand implements CommandCharacter {

    /**
     * Method that change the selection of the players num
     *
     * @return false always
     */
    @Override
    public boolean executeNumberStatus() {
        Player player = View.getPlayer();
        ArrayList<Integer> PlayersNum = player.getPlayersNum();

        if(player.getPlayerNumber() == null)
            throw new IllegalArgumentException(" Player Number is empty.");
        if(player.getPlayersNum() == null)
            throw new IllegalArgumentException(" Array of number of Players num is empty.");

        player.setPlayerNumber(PlayersNum.get((PlayersNum.indexOf(player.getPlayerNumber()) + 1) % PlayersNum.size()));
        View.doUpdate();
        return false;
    }

    /**
     * Method that change the selectedPosition with the first next empty cells on his right side
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
            coloredPosition.setY(checkCorrectCoordinate(coloredPosition.getY()));
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
     * @param  coordinate  Y coordinate of the position
     * @return  correct Y coordinate
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
     * If the player hasn't chosen yet the worker, the method changes the colored position to the other worker's position.
     * Else the method changes the colored position to the current position's east one.
     *
     *
     * @return              always false, you can't notify the message yet
     */
    @Override
    public boolean executeRunningStatus() throws IllegalArgumentException{
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = View.getGameBoard().getColoredPosition();
        Position startingPosition = gameBoard.getStartingPosition();

        if (startingPosition == null) {

            gameBoard.setColoredPosition(gameBoard
                    .getWorkersPositions()
                    .stream()
                    .filter(position -> !position.equals(coloredPosition))
                    .findAny()
                    .get());

        } else {
            Set<Position> availableCells = gameBoard.getWorkersAvailableCells(startingPosition);

            if (availableCells.size() == 1) {
                View.doUpdate();
                return false;
            }

            Position currentPosition;
            if (availableCells.size() == 2) {
                currentPosition = availableCells.stream().filter(position -> !position.equals(coloredPosition)).findAny().get();

                if (currentPosition.getY() > coloredPosition.getY())
                    gameBoard.setColoredPosition(currentPosition);
            }
            else {
                CardinalDirection offset = startingPosition.checkMutualPosition(coloredPosition);
                currentPosition = coloredPosition.translateCardinalDirectionToPosition(CardinalDirection.EAST);

                if (offset == CardinalDirection.WEST)
                    currentPosition = currentPosition.translateCardinalDirectionToPosition(CardinalDirection.EAST);

                if (!gameBoard.getWorkersAvailableCells(startingPosition).contains(currentPosition)) {
                    int offY = coloredPosition.getY() - startingPosition.getY();

                    if (offY == -1)
                        currentPosition = checkPositionLowerY(availableCells, currentPosition, offset);
                    else if (offY == 0)
                        currentPosition = checkPositionSameY(availableCells, currentPosition, offset);
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

    private Position checkPositionLowerY(Set<Position> positions, Position pos, CardinalDirection offset) {
        Position current = null;
        switch (offset) {
            case WEST:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.NORTHWEST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTHWEST);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                if (positions.contains(current))
                    return current;

                current = (pos.translateCardinalDirectionToPosition(CardinalDirection.WEST)).translateCardinalDirectionToPosition(CardinalDirection.WEST);
                break;

            case NORTHWEST:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.EAST);
                if (positions.contains(current))
                    return current;

                current = (pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTH)).translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                if (positions.contains(current))
                    return current;

                current = checkPositionSameY(positions,pos.translateCardinalDirectionToPosition(CardinalDirection.EAST),CardinalDirection.NORTH);
                if (current.getX() == pos.getX() && current.getY() == current.getY())
                    current = current.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                break;

            case SOUTHWEST:
                current = pos.translateCardinalDirectionToPosition(CardinalDirection.EAST);
                if (positions.contains(current))
                    return current;

                current = (pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH)).translateCardinalDirectionToPosition(CardinalDirection.NORTH);
                if (positions.contains(current))
                    return current;

                current = checkPositionSameY(positions,pos.translateCardinalDirectionToPosition(CardinalDirection.EAST),CardinalDirection.SOUTH);
                if (current.getX() == pos.getX() && current.getY() == current.getY())
                    current = current.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                break;
        }
        return current;
    }

    private Position checkPositionSameY(Set<Position> positions, Position pos, CardinalDirection offset) {
        Position current = null;

        switch (offset) {
            case NORTH:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                if (positions.contains(current))
                    return current;

                current = current.translateCardinalDirectionToPosition(CardinalDirection.SOUTH);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                break;
            case SOUTH:

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.NORTH);
                if (positions.contains(current))
                    return current;

                current = current.translateCardinalDirectionToPosition(CardinalDirection.NORTH);
                if (positions.contains(current))
                    return current;

                current = pos.translateCardinalDirectionToPosition(CardinalDirection.WEST);
                break;
        }
        return current;
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
    public boolean executeSpecialCommandsStatus() throws IllegalArgumentException{

        GameBoard gameBoard = View.getGameBoard();
        ArrayList<String> godCards = gameBoard.getSelectedGodCards();
        String coloredCard = gameBoard.getColoredGodCard();

        if (coloredCard == null)
            throw new IllegalArgumentException("no card selected");
        if (godCards == null)
            throw new IllegalArgumentException("selected god cards is empty");

        gameBoard.setColoredGodCard(godCards.get((godCards.indexOf(coloredCard) + 1) % godCards.size()));
        View.doUpdate();
        return false;
    }

    /**
     * Method that change the selected GodCard
     *
     * @return  false always
     */
    @Override
    public boolean executeSelectingGodCardsStatus() {
        GameBoard gameBoard = View.getGameBoard();
        ArrayList<String> godCards = gameBoard.getMatchCards();

        if(godCards == null)
            throw new IllegalArgumentException(" GodCards is empty ");
        if(gameBoard.getColoredGodCard() == null)
            throw new IllegalArgumentException(" Colored GodCard is empty ");

        gameBoard.setColoredGodCard(godCards.get((godCards.indexOf(gameBoard.getColoredGodCard()) + 1) % godCards.size()));
        System.out.println("Colored Godcard is " + gameBoard.getColoredGodCard());
        View.doUpdate();
        return false;
    }

}
