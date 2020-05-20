package it.polimi.ingsw.gui.client.controller.state;

import it.polimi.ingsw.gui.client.view.DataBase;
import it.polimi.ingsw.gui.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class SelectingGodCards extends ControlState {

    //DataBase dataBase = View.getDataBase();

    MessageEvent messageEvent = new MessageEvent();

    @Override
    public MessageEvent computeInput(String input) {
        if(DataBase.getSelectedGodCards().size() == 0){
            messageEvent = new MessageEvent();
        }

        if(DataBase.getSelectedGodCards().size() >= DataBase.getPlayerNumber())
            throw new IllegalArgumentException("Errore di scambio tra stati");

        //corretto
        if(DataBase.getMatchCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))
           && ! DataBase.getSelectedGodCards().contains(input.toUpperCase())){
                DataBase.getSelectedGodCards().add(DataBase
                        .getMatchCards()
                        .stream()
                        .filter(w-> w.toUpperCase().equals(input.toUpperCase()))
                        .findFirst()
                        .get());
                DataBase.getMatchCards().remove(DataBase
                        .getMatchCards()
                        .stream()
                        .filter(w-> w.toUpperCase().equals(input.toUpperCase()))
                        .findFirst()
                        .get());
                if(DataBase.getSelectedGodCards().size() == DataBase.getPlayerNumber()){
                    messageEvent.setMatchCards(DataBase.getSelectedGodCards());
                    DataBase.setMessageReady(true);
                    DataBase.setPlayerState(PlayerState.IDLE);
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
        DataBase.setMessageReady(false);
        DataBase.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.setControlState(new NotInitialized());
            DataBase.setPlayerState(null);
            DataBase.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }

        //caso SELECTING_GOD_CARDS
        if(DataBase.getMatchState() == MatchState.SELECTING_GOD_CARDS && DataBase.getPlayerState() == PlayerState.ACTIVE){
            DataBase.setMatchCards(message.getMatchCards());
            DataBase.setActiveInput(true);
            View.setRefresh(true);
            View.print();
        }
        else{
            System.out.println(computeView());
        }

    }

    @Override
    public String computeView() {
        int number = DataBase.getPlayerNumber() - DataBase.getSelectedGodCards().size();
        StringBuilder string = new StringBuilder();
        List<String> players = new ArrayList<>(DataBase.getMatchPlayers().values());

        if (players.size()==2 && DataBase.getSelectedGodCards().isEmpty() && ! View.getError()) {
            players.remove(DataBase.getNickname());
            string.append("Your opponent is (" + players.get(0) + ")\n");
        }
        else if (players.size()==3 && DataBase.getSelectedGodCards().isEmpty() && ! View.getError()) {
            players.remove(DataBase.getNickname());
            string.append("Your opponents are (" + players.get(0) + ", " + players.get(1) + ")\n");
        }

        if(PlayerState.ACTIVE == DataBase.getPlayerState()){
            if(View.getError()){
                string.append("Select other "+ number +" God Cards from [ ");
                DataBase.getMatchCards().stream().forEach(card -> string.append(card + ", "));
                string.deleteCharAt(string.length()-2);
                string.append("]");
            } else if (View.getRefresh()) {
                string.append("Select " + number + " God Cards from [ ");
                DataBase.getMatchCards().stream().forEach(card -> string.append(card + ", "));
                string.deleteCharAt(string.length() - 2);
                string.append("]");
            }
        }
        else{
            string.append(DataBase.getMatchPlayers().get(DataBase.getPlayer()) + " is selecting the cards for the match ");
        }
        return string.toString();
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        DataBase.setSelectedGodCards(new HashSet<>());
        DataBase.setActiveInput(true);
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