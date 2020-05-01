package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.GodCards;

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
    public boolean executeRunningStatus() throws IllegalArgumentException{
        if (View.getPlayer().isTerminateTurnAvailable()) {
            Controller.getMessage().setEndTurn(true);
            System.out.println("\nYou have chosen to end your turn!\n");
            View.doUpdate();
            return true;
        }
        else throw new IllegalArgumentException("you can't terminate your turn in this moment");
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
        GameBoard gameBoard = View.getGameBoard();
        String coloredGodCard = gameBoard.getColoredGodCard();
        ArrayList<String> godCards = gameBoard.getMatchCards();
        ArrayList<String> selectedGodCards = gameBoard.getSelectedGodCards();

        if(godCards == null){
            throw new IllegalArgumentException(" godCards empty ");
        }
        if(selectedGodCards.size() == 0){
            System.out.println("Selected God Cards is empty");
            return false;
        }
        if(coloredGodCard == null){
            throw new IllegalArgumentException(" None card selected ");
        }


        if(selectedGodCards.size() > 0){
            String cardRemoved = selectedGodCards.get(selectedGodCards.size()-1);
            selectedGodCards.remove(cardRemoved);
            godCards.add(cardRemoved);
            gameBoard.setColoredGodCard(godCards.get(0));
            System.out.println("\n"+cardRemoved + " has been removed\n");
        }
        System.out.println("MatchCards "+godCards);
        View.doUpdate();
        return false;
    }

}
