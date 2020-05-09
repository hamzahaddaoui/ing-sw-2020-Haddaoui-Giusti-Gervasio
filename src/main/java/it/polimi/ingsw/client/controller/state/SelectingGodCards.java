package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SelectingGodCards extends ControlState {

    GameBoard gameBoard = View.getGameBoard();
    Player player = View.getPlayer();

    MessageEvent messageEvent;
    //static ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public MessageEvent computeInput(String input) {
        if(gameBoard.getSelectedGodCards().size() == 0){
            messageEvent = new MessageEvent();
        }

        if(gameBoard.getSelectedGodCards().size() >= player.getPlayer())
            throw new IllegalArgumentException("Errore di scambio tra stati");

        //corretto
        if(gameBoard.getMatchCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))
                && !gameBoard.getSelectedGodCards().contains(input.toUpperCase())){
                gameBoard.getSelectedGodCards().add(input.toUpperCase());
                if(gameBoard.getSelectedGodCards().size() == player.getPlayer()){
                    messageEvent.setGodCards(gameBoard.getSelectedGodCards());
                    Controller.setMessageReady(true);
                    return messageEvent;
                }
                else{
                    View.setError(true);
                    computeView();
                    View.setError(false);
                }
        }
        //sbagliato
        else{
            View.setError(true);
            View.print();
        }
        Controller.setMessageReady(false);
        Controller.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        // aggiornare i dati base
        player.setMatchState(messageEvent.getMatchState());
        player.setPlayerState(messageEvent.getPlayerState());
        player.setTurnState(messageEvent.getTurnState());
        if(messageEvent.getMatchPlayers() != player.getMatchPlayers() && messageEvent.getMatchPlayers() != null)
            player.setMatchPlayers(messageEvent.getMatchPlayers());
        if(messageEvent.getCurrentPlayer() != player.getPlayer())
            player.setPlayer(messageEvent.getCurrentPlayer());

        //caso SELECTING_GOD_CARDS
        if(player.getMatchState() == MatchState.SELECTING_GOD_CARDS){
            gameBoard.setMatchCards(messageEvent.getMatchCards());
        }

        //caso SELECTING_SPECIAL_COMMAND
        if(player.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND){
            gameBoard.setSelectedGodCards(messageEvent.getMatchCards());
            player.setControlState(new SelectingSpecialCommand());
        }

        //caso ACTIVE
        if(player.getPlayerState() == PlayerState.ACTIVE){
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
        }

    }

    @Override
    public String computeView() {
        int number = player.getPlayerNumber() - gameBoard.getSelectedGodCards().size();
        if(View.getError()){
            return  "Select other "+ number +" God Cards from "+ gameBoard.getMatchCards().toString();
        }
        else if(View.getRefresh()){
            return  "Select "+ number +" God Cards from "+ gameBoard.getMatchCards().toString();
        }
        else return "MANCATO INSERIMENTO DI REFRESH O ERROR";
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        gameBoard.setSelectedGodCards(new HashSet<>());
        Controller.setActiveInput(true);
        System.out.println("Select "+ player.getPlayerNumber() +" God Cards:");
    }

}
   /* Set<String> selectedCards = new HashSet<>();

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        if (super.checkMessage(viewObject)) {

        GameBoard gameBoard = View.getGameBoard();
        Player player = View.getPlayer();

        if (selectedCards.contains(viewObject)) {
            System.out.println("CARD ALREADY SELECTED");
            return false;
        }

        if (gameBoard.getMatchCards().contains(viewObject)) {
            if (selectedCards.size()<player.getPlayerNumber()) {
            selectedCards.add(viewObject);
            if (selectedCards.size()==player.getPlayerNumber()) {
                Controller.getMessage().setMatchCards(selectedCards);
                return true; }
            } else System.out.println("LIMIT NUMBER OF CARDS REACHED");
        } else System.out.println("NOT-EXISTING CARD");
        }
        return false;
    }

}
*/