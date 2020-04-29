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
    public boolean executeRunningStatus() {

        Position coloredPosition = View.getGameBoard().getColoredPosition();

        if (View.getGameBoard().getStartingPosition() == null) {

            View.getGameBoard().setColoredPosition (View
                                        .getGameBoard()
                                        .getWorkersPositions()
                                        .stream()
                                        .filter(position -> !position.equals(coloredPosition))
                                        .findAny()
                                        .get());

        }
        else {
            CardinalDirection offset = View.getGameBoard().getStartingPosition().checkMutualPosition(coloredPosition);
            if (offset == CardinalDirection.NORTH ||
                    offset == CardinalDirection.NORTHEAST ||
                    offset == CardinalDirection.SOUTH ||
                    offset == CardinalDirection.SOUTHEAST)
                     View.getGameBoard().setColoredPosition(View.getGameBoard().getColoredPosition().translateCardinalDirectionToPosition(CardinalDirection.WEST));
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
        GameBoard gameBoard = View.getGameBoard();
        int posColoredCard = gameBoard.getSelectedGodCards().indexOf(gameBoard.getColoredGodCard());
        if (posColoredCard == 0)
            posColoredCard = View.getPlayer().getPlayersNum();
        gameBoard.setColoredGodCard(gameBoard.getSelectedGodCards().get(posColoredCard-1));
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

        gameBoard.setColoredGodCard(godCards.get(abs(godCards.indexOf(gameBoard.getColoredGodCard()) - 1 + godCards.size()) % godCards.size()));
        View.doUpdate();
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }
}
