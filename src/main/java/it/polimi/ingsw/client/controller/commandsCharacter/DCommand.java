package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;

public class DCommand implements CommandCharacter {

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

        player.setPlayersNum(coloredPlayersNum.get((coloredPlayersNum.indexOf(player.getPlayersNum()) + 1) % coloredPlayersNum.size()));
        System.out.println(  "\nYou select match of n."+ player.getPlayersNum()+ " player " );
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
    public boolean executeRunningStatus() {
        GameBoard gameBoard = View.getGameBoard();
        Position coloredPosition = gameBoard.getColoredPosition();

        if (gameBoard.getStartingPosition() == null) {

            gameBoard.setColoredPosition( gameBoard
                    .getWorkersPositions()
                    .stream()
                    .filter(position -> !position.equals(coloredPosition))
                    .findAny()
                    .get());

        }
        else {
            CardinalDirection offset = View.getGameBoard().getStartingPosition().checkMutualPosition(coloredPosition);
            if (offset == CardinalDirection.NORTH ||
                    offset == CardinalDirection.NORTHWEST ||
                    offset == CardinalDirection.SOUTH ||
                    offset == CardinalDirection.SOUTHWEST)
                View.getGameBoard().setColoredPosition(View.getGameBoard().getColoredPosition().translateCardinalDirectionToPosition(CardinalDirection.EAST));
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
    public boolean executeSpecialCommandsStatus() {
        int posColoredCard = View.getGameBoard().getSelectedGodCards().indexOf(View.getGameBoard().getColoredGodCard());

        if (posColoredCard == View.getPlayer().getPlayersNum()-1)
            posColoredCard = -1;
        View.getGameBoard().setColoredGodCard(View.getGameBoard().getSelectedGodCards().get(posColoredCard+1));
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

    @Override
    public void executeWaitingStatus() {

    }
}
