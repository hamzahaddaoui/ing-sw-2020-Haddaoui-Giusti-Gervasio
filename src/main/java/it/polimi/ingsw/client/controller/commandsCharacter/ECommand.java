package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;

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
        if (View.getPlayer().isTerminateTurnAvailable()) {
            Controller.getMessage().setEndTurn(true);
            View.doUpdate();
            return true;
        }
        else {
            View.doUpdate();
            return false;
        }
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
        String coloredGodCard = View.getGameBoard().getColoredGodCard();
        ArrayList<String> godCards = View.getGameBoard().getMatchCards();
        ArrayList<String> selectedGodCards = View.getGameBoard().getSelectedGodCards();

        if(!selectedGodCards.contains(coloredGodCard)){
            throw new IllegalArgumentException(" This card is not in the SelectedCard Deck ");
        }

        if(coloredGodCard != null && selectedGodCards.size() > 0 && selectedGodCards.contains(coloredGodCard)){
            selectedGodCards.remove(selectedGodCards.indexOf(coloredGodCard));
            godCards.add(coloredGodCard);
            View.getGameBoard().setColoredGodCard(godCards.get(0));
        }
        View.doUpdate();
        return false;
    }

    @Override
    public void executeWaitingStatus() {
    }

}
