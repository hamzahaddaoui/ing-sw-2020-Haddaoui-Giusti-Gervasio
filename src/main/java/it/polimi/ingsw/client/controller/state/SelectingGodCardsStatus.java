package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectingGodCardsStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        super.checkMessage(viewObject);

        GameBoard gameBoard = View.getGameBoard();
        Player player = View.getPlayer();
        Set<String> selectedCards = new HashSet<>();

        if (gameBoard.getMatchCards().contains(viewObject)) {
            selectedCards.add(viewObject);

            if (selectedCards.size() == player.getPlayerNumber()) {
                Controller.getMessage().setGodCards(selectedCards);
                return true;
            } else return false;
        } else throw new IllegalArgumentException("\ncarta inesistente");

        /*if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getGameBoard().getMatchCards() == null)
            throw new IllegalArgumentException(" MatchCards is empty");

        if(viewObject == null)
            throw new IllegalArgumentException(" ViewObject is empty");

        return commandCharacter.executeSelectingGodCardsStatus();*/
    }

}
