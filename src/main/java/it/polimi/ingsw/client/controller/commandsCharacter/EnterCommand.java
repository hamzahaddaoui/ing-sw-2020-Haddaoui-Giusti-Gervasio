package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class EnterCommand implements CommandCharacter {

    /**
     * Method that confirms the choice of the number of the players' game
     *
     * @return
     */
    @Override
    public boolean executeNumberStatus() {
        Controller.getMessage().setPlayersNum(View.getPlayersNum());
        return true;
    }

    /**
     * Method that confirms the position selected for the Worker.
     *
     * the position selected is deleted from PlacingAvailableCells and added to WorkersPositions
     *
     * @return  true if the player have placed his 2 worker, else false
     */
    @Override
    public boolean executePlacingWorkerStatus() {
        Position coloredPosition = View.getColoredPosition();
        Set<Position> workersPositions = View.getWorkersPositions();
        Set<Position> placingAvailableCells = View.getPlacingAvailableCells();

        if(coloredPosition!=null &&
                !placingAvailableCells.contains(coloredPosition) &&
                !workersPositions.contains(coloredPosition)){
            workersPositions.add(coloredPosition);
            placingAvailableCells.remove(coloredPosition);
            if(workersPositions.size()==2){
                return true;
            }
            View.setColoredPosition(placingAvailableCells.stream().findFirst().get());
        }
        return false;
    }

    /**
     * Method that confirms the View's colored position.
     * <p>
     * If the player hasn't chosen the worker yet, the method sets the colored position as the View's starting position.
     * Else checks if the colored position is contained in the worker's available cells and, if so, sets the message's start position and end position.
     *
     * @return              true if you can send the message, false otherwise
     */
    @Override
    public boolean executeRunningStatus() {
        if (View.getStartingPosition()==null) {
            View.setStartingPosition(View.getColoredPosition());
            return false;
        }
        else if (View
                        .getWorkersAvailableCells(View.getStartingPosition())
                        .contains(View.getColoredPosition())) {
            Controller.getMessage().setStartPosition(View.getStartingPosition());
            Controller.getMessage().setEndPosition(View.getColoredPosition());
            return true;
        }
        else return false;
    }

    /**
     * Method that confirms the View's colored god card.
     * <p>
     *
     * @return  true if the colored god card is contained in View's set of god cards, false otherwise
     */
    @Override
    public boolean executeSpecialCommandsStatus() {
        String coloredGodCard = View.getColoredGodCard();

        if (View.getSelectedGodCards().contains(coloredGodCard)) {
            Controller.getMessage().setGodCard(coloredGodCard);
            return true;
        }
        else return false;
    }

    /**
     * Method that confirms the GodCard's selection.
     *
     * the selectedPosition is deleted from selectedGodCards and is added to GodCards
     *
     * @return true if the selectedGodCard's size is 2, else false
     */
    @Override
    public boolean executeSelectingGodCardsStatus() {
        ArrayList<String> godCards = View.getGodCards();
        Integer playersNum = View.getPlayersNum();
        String coloredGodCard = View.getColoredGodCard();
        ArrayList<String> selectedGodCards = View.getSelectedGodCards();

        if(coloredGodCard!=null && selectedGodCards.size() < playersNum){
            selectedGodCards.add(coloredGodCard);
            godCards.remove(godCards.indexOf(coloredGodCard));
            if(selectedGodCards.size() < playersNum)
                View.setColoredGodCard(godCards.get(0));
            else{
                View.setColoredGodCard(null);
                return true;
            }
        }
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

}
