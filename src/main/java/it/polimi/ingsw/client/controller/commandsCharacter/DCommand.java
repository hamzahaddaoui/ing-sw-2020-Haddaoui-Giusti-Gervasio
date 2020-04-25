package it.polimi.ingsw.client.controller.commandsCharacter;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.utilities.CardinalDirection;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;


public class DCommand implements CommandCharacter {

    /**
     * Method that change the selection of the players num
     *
     * @return false always
     */
    @Override
    public boolean executeNumberStatus() {
        ArrayList<Integer> coloredPlayersNum = View.getColoredPlayersNum();

        View.setPlayersNum(coloredPlayersNum.get((coloredPlayersNum.indexOf(View.getPlayersNum()) + 1) % coloredPlayersNum.size()));
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

        Position coloredPosition = View.getColoredPosition();

        while (true) {
            System.out.println(coloredPosition);
            coloredPosition.setX(checkCorrectCoordinate(coloredPosition.getX()));
            if (View.getPlacingAvailableCells().contains(coloredPosition)) {
                System.out.println();
                System.out.println("Colored position");
                System.out.println(coloredPosition.getX());
                System.out.println();
                View.setColoredPosition(coloredPosition);
                break;
            }
        }
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

        Position coloredPosition = View.getColoredPosition();

        if (View.getStartingPosition() == null) {

            View.setColoredPosition( View
                .getWorkersPositions()
                .stream()
                .filter(position -> !position.equals(coloredPosition))
                .findAny()
                .get());

        }
        else {
            CardinalDirection offset = View.getStartingPosition().checkMutualPosition(coloredPosition);
            if (offset == CardinalDirection.NORTH ||
                    offset == CardinalDirection.NORTHWEST ||
                    offset == CardinalDirection.SOUTH ||
                    offset == CardinalDirection.SOUTHWEST)
                View.setColoredPosition(View.getColoredPosition().translateCardinalDirectionToPosition(CardinalDirection.EAST));
        }
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
        int posColoredCard = View.getSelectedGodCards().indexOf(View.getColoredGodCard());

        if (posColoredCard == View.getPlayersNum()-1)
            posColoredCard = -1;
        View.setColoredGodCard(View.getSelectedGodCards().get(posColoredCard+1));
        return false;
    }

    /**
     * Method that change the selected GodCard
     *
     * @return  false always
     */
    @Override
    public boolean executeSelectingGodCardsStatus() {
        ArrayList<String> godCards=View.getGodCards();

        View.setColoredGodCard(godCards.get((godCards.indexOf(View.getColoredGodCard()) + 1) % godCards.size()));

        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }
}
