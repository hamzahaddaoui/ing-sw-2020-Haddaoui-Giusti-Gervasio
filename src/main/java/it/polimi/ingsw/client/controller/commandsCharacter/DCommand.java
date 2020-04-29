package it.polimi.ingsw.client.controller.commandsCharacter;

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
        ArrayList<Integer> coloredPlayersNum = View.getPlayer().getColoredPlayersNum();

        View.getPlayer().setPlayersNum(coloredPlayersNum.get((coloredPlayersNum.indexOf(View.getPlayer().getPlayersNum()) + 1) % coloredPlayersNum.size()));
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

        Position coloredPosition = View.getGameBoard().getColoredPosition();

        while (true) {
            System.out.println(coloredPosition);
            coloredPosition.setY(checkCorrectCoordinate(coloredPosition.getY()));
            if (View.getGameBoard().getPlacingAvailableCells().contains(coloredPosition)) {
                View.getGameBoard().setColoredPosition(coloredPosition);
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
    private int checkCorrectCoordinate(int coordinate){
        coordinate++;
        if(coordinate>4) coordinate-=5;
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

        Position coloredPosition = View.getGameBoard().getColoredPosition();

        if (View.getGameBoard().getStartingPosition() == null) {

            View.getGameBoard().setColoredPosition( View
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
        ArrayList<String> godCards=View.getGameBoard().getMatchCards();

        View.getGameBoard().setColoredGodCard(godCards.get((godCards.indexOf(View.getGameBoard().getColoredGodCard()) + 1) % godCards.size()));
        View.doUpdate();
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }
}
