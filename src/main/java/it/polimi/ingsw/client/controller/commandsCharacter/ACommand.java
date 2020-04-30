package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;


public class ACommand implements CommandCharacter {

    /**
     * Method that change the selection of the players num
     *
     * @return false always
     */
    @Override
    public boolean executeNumberStatus() {
        Player player = View.getPlayer();
        ArrayList<Integer> coloredPlayersNum = player.getColoredPlayersNum();

        if(player.getPlayersNum() == null)
            throw new IllegalArgumentException(" Players Num is empty.");
        if(player.getColoredPlayersNum() == null)
            throw new IllegalArgumentException(" Colored Players num is empty.");

        player.setPlayersNum(coloredPlayersNum.get(abs(coloredPlayersNum.indexOf(player.getPlayersNum()) - 1) % coloredPlayersNum.size()));
        System.out.println(  " You select match of n. player " + player.getPlayersNum());
        View.doUpdate();
        return false;
    }

    /**
     * Method that change the selectedPosition with the first next empty cells on his left side
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
    private int checkCorrectCoordinate(int coordinate) {
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
     * If the player hasn't chosen yet the worker, the method changes the colored position to the other worker's position.
     * Else the method changes the colored position to the current position's west one.
     *
     *
     * @return              always false, you can't notify the message yet
     */
    @Override
    public boolean executeRunningStatus() throws IllegalArgumentException{
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = View.getGameBoard().getColoredPosition();
        Position startingPosition = gameBoard.getStartingPosition();

        if (startingPosition== null) {

            Position finalColoredPosition = coloredPosition;
            View.getGameBoard().setColoredPosition (View
                                        .getGameBoard()
                                        .getWorkersPositions()
                                        .stream()
                                        .filter(position -> !position.equals(finalColoredPosition))
                                        .findAny()
                                        .get());

        }
        else {
            CardinalDirection offset = startingPosition.checkMutualPosition(coloredPosition);
            if (offset == CardinalDirection.NORTH ||
                    offset == CardinalDirection.NORTHEAST ||
                    offset == CardinalDirection.SOUTH ||
                    offset == CardinalDirection.SOUTHEAST) {
                gameBoard.setColoredPosition(coloredPosition.translateCardinalDirectionToPosition(CardinalDirection.WEST));
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

        gameBoard.setColoredGodCard(godCards.get(abs(godCards.indexOf(gameBoard.getColoredGodCard()) - 1 + godCards.size()) % godCards.size()));
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
        Player player = View.getPlayer();
        ArrayList<String> godCards = gameBoard.getMatchCards();

        if(godCards == null)
            throw new IllegalArgumentException(" GodCards is empty ");
        if(gameBoard.getColoredGodCard() == null)
            throw new IllegalArgumentException(" Colored GodCard is empty ");

        gameBoard.setColoredGodCard(godCards.get(abs(godCards.indexOf(gameBoard.getColoredGodCard()) - 1 + godCards.size()) % godCards.size()));
        System.out.println(  "\nYou select match of n."+ player.getPlayersNum()+ " player " );
        View.doUpdate();
        return false;
    }

}
