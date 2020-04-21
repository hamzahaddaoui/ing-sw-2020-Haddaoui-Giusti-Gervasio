package it.polimi.ingsw.client.controller.commandsCharacter;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;

import java.util.ArrayList;

public class ECommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {
        return false;
    }

    @Override
    public boolean executePlacingWorkerStatus() {
        return false;
    }

    /**
     * Method that recognize if the player wants to end the turn.
     * <p>
     * If the View's terminate turn boolean is true, the method sets the message's end turn boolean as true.
     *
     *
     * @return              true if you can send the message, false otherwise
     */
    @Override
    public boolean executeRunningStatus() {
        if (View.isTerminateTurnAvailable()) {
        Controller.getMessage().setEndTurn(true);
        return true;
        }
        else return false;
    }

    @Override
    public boolean executeSpecialCommandsStatus() {
        return false;
    }

    /**
     * Method that delete the coloredGodCard that has been insert in the array list of selectedGodCards
     *
     * @return  always false
     */
    @Override
    public boolean executeSelectingGodCardsStatus() {
        String coloredGodCard = View.getColoredGodCard();
        ArrayList<String> godCards = View.getGodCards();
        ArrayList<String> selectedGodCards = View.getSelectedGodCards();

        if(coloredGodCard!=null){
            godCards.add(coloredGodCard);
            selectedGodCards.remove(selectedGodCards.indexOf(coloredGodCard));
            View.setColoredGodCard(godCards.get(0));
        }
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

}
