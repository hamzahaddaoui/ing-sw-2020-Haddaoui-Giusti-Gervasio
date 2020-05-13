package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SelectingGodCards extends ControlState {

    GameBoard gameBoard = View.getGameBoard();
    Player player = View.getPlayer();

    MessageEvent messageEvent = new MessageEvent();

    @Override
    public MessageEvent computeInput(String input) {
        if(gameBoard.getSelectedGodCards().size() == 0){
            messageEvent = new MessageEvent();
        }

        if(gameBoard.getSelectedGodCards().size() >= player.getPlayerNumber())
            throw new IllegalArgumentException("Errore di scambio tra stati");

        //corretto
        if(gameBoard.getMatchCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))
                && !gameBoard.getSelectedGodCards().contains(input.toUpperCase())){
                gameBoard.getSelectedGodCards().add(gameBoard
                        .getMatchCards()
                        .stream()
                        .filter(w-> w.toUpperCase().equals(input.toUpperCase()))
                        .findFirst()
                        .get());
                gameBoard.getMatchCards().remove(gameBoard
                        .getMatchCards()
                        .stream()
                        .filter(w-> w.toUpperCase().equals(input.toUpperCase()))
                        .findFirst()
                        .get());
                if(gameBoard.getSelectedGodCards().size() == player.getPlayerNumber()){
                    messageEvent.setMatchCards(gameBoard.getSelectedGodCards());
                    Controller.setMessageReady(true);
                    View.getPlayer().setPlayerState(PlayerState.IDLE);
                    return messageEvent;
                }
                else{
                    View.setError(true);
                    System.out.println(computeView());
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

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            player.setControlState(new NotInitialized());
            player.setPlayerState(null);
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }

        //caso SELECTING_GOD_CARDS
        if(player.getMatchState() == MatchState.SELECTING_GOD_CARDS && player.getPlayerState() == PlayerState.ACTIVE){
            gameBoard.setMatchCards(message.getMatchCards());
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
        }
        else{
            System.out.println(computeView());
        }

    }

    @Override
    public String computeView() {
        int number = player.getPlayerNumber() - gameBoard.getSelectedGodCards().size();
        StringBuilder string = new StringBuilder();
        List<String> players = new ArrayList<>(player.getMatchPlayers().values());

        if (players.size()==2 && gameBoard.getSelectedGodCards().isEmpty() && !View.getError()) {
            players.remove(player.getNickname());
            string.append("Your opponent is (" + players.get(0) + ")\n");
        }
        else if (players.size()==3 && gameBoard.getSelectedGodCards().isEmpty() && !View.getError()) {
            players.remove(player.getNickname());
            string.append("Your opponents are (" + players.get(0) + ", " + players.get(1) + ")\n");
        }

        if(PlayerState.ACTIVE == player.getPlayerState()){
            if(View.getError()){
                string.append("Select other "+ number +" God Cards from [ ");
                gameBoard.getMatchCards().stream().forEach(card -> string.append(card +", "));
                string.deleteCharAt(string.length()-2);
                string.append("]");
            } else if (View.getRefresh()) {
                string.append("Select " + number + " God Cards from [ ");
                gameBoard.getMatchCards().stream().forEach(card -> string.append(card + ", "));
                string.deleteCharAt(string.length() - 2);
                string.append("]");
            }
            //return string.toString();
        }
        else{
            string.append(player.getMatchPlayers().get(player.getPlayer()) + " is selecting the cards for the match ");
        }
        return string.toString();
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        gameBoard.setSelectedGodCards(new HashSet<>());
        Controller.setActiveInput(true);
        System.out.println(computeView());
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