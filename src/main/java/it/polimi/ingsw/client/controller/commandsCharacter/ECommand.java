package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
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
        if(selectedGodCards == null){
            throw new IllegalArgumentException(" selectedGodCards empty ");
        }
        if(coloredGodCard == null){
            throw new IllegalArgumentException(" None card selected ");
        }
        if(!selectedGodCards.contains(coloredGodCard)){
            throw new IllegalArgumentException(" This card is not in the SelectedCard Deck ");
        }


        if(selectedGodCards.contains(coloredGodCard)){
            selectedGodCards.remove(selectedGodCards.indexOf(coloredGodCard));
            godCards.add(coloredGodCard);
            gameBoard.setColoredGodCard(godCards.get(0));
            System.out.println("\n"+coloredGodCard + " has been removed\n");
        }
        System.out.println("MatchCards "+godCards);
        View.doUpdate();
        return false;
    }

}
