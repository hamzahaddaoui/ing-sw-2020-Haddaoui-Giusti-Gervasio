package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.Position;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;

import java.util.List;


public class EnterCommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {
        Controller.getMessage().setPlayersNum(View.getPlayersNum());
        return true;
    }

    @Override
    public boolean executePlacingWorkerStatus() {
        Position tempPosition = View.getColoredPosition();
        //se la nuova posizione selezionata è corretta allora la aggiunge
        if(tempPosition!=null
                && !View.getPlacingAvailableCells().contains(tempPosition)
                && !View.getWorkersPositions().contains(tempPosition)){
            View.getWorkersPositions().add(tempPosition);
            View.getPlacingAvailableCells().remove(tempPosition);
            if(View.getWorkersPositions().size()==2){
                return true;
            }
            View.setColoredPosition(View.getPlacingAvailableCells().stream().findFirst().get());
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
        if (View.getSelectedGodCards().contains(View.getColoredGodCard())) {
            Controller.getMessage().setGodCard(View.getColoredGodCard());
            return true;
        }
        else return false;
    }

    @Override
    public boolean executeSelectingGodCardsStatus() {
        if(View.getGodCard()!=null && View.getSelectedGodCards().size()< View.getPlayersNum()){
            addGodCard();
            if(View.getSelectedGodCards().size() < View.getPlayersNum())
                View.setGodCard(View.getGodCards().get(0));
            else{
                View.setGodCard(null);
                return true;
            }
        }
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

    private void addGodCard() {
        View.getSelectedGodCards().add(View.getGodCard());
        View.getGodCards().remove(View.getGodCards().indexOf(View.getGodCard()));
    }

}
