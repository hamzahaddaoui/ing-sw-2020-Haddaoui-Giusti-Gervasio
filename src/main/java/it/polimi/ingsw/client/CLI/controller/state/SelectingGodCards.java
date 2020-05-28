package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.*;

/**
 * @author giusti-leo
 *
 * SelectingGodCards is a state of the Controller and it handles the selection of GodCards for the Match
 *
 */

public class SelectingGodCards extends ControlState {

    MessageEvent messageEvent = new MessageEvent();

    /**
     * It analyzes input from Controller.
     * If the input is correct, it deletes the god Card from the MatchCards List and it adds GodCard to the SelectedGodCards
     * If the size of SelectedGodCards is equals to PlayersNumber, it prepares the message for NetworkHandler
     * If the input is not correct, it prints an advice
     *
     * @param input  is the GodCard if it is correct
     * @return  true is PlayersNumber is equal to SelectedGodCards' size, else false
     */
    @Override
    public MessageEvent computeInput(String input) {
        if(DataBase.getSelectedGodCards().size() == 0){
            messageEvent = new MessageEvent();
        }

        if(DataBase.getSelectedGodCards().size() > DataBase.getPlayerNumber())
            throw new IllegalArgumentException("Errore di scambio tra stati");

        if(DataBase.getMatchCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))
                && !DataBase.getSelectedGodCards().contains(input.toUpperCase())){

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
                    View.setRefresh(true);
                    View.handler();
                }
        }
        else{
            View.setError(true);
            View.handler();
        }
        DataBase.setMessageReady(false);
        DataBase.setActiveInput(true);
        return null;
    }

    /**
     * it contains Disconnection case.
     * if PlayerState is equals to Active it saves on DataBase the MatchCards for the selection of match's gods
     *
     * @param message  is the Network Handler 's message
     */
    @Override
    public void updateData(MessageEvent message) {

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo()!=null && message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.setDisconnectedUser(true);
            DataBase.resetDataBase();
            View.setRefresh(true);
            View.handler();
            DataBase.setDisconnectedUser(false);
            return;
        }

        //caso SELECTING_GOD_CARDS
        if( DataBase.getPlayerState() == PlayerState.ACTIVE){
            DataBase.setMatchCards(message.getMatchCards());
            DataBase.setActiveInput(true);
            View.setRefresh(true);
            View.handler();
        }
        else{
            View.setRefresh(true);
            View.handler();
        }

    }

    /**
     * Depending on the Database's state, compute different String to print
     *
     * @return  String to print on view
     */
    @Override
    public String computeView() {
        int number = DataBase.getMatchPlayers().size() - DataBase.getSelectedGodCards().size();
        StringBuilder string = new StringBuilder();
        List<String> players = new ArrayList<>(DataBase.getMatchPlayers().values());

        if (players.size() == 2 && DataBase.getSelectedGodCards().isEmpty() && !View.getError()) {
            players.remove(DataBase.getNickname());
            string.append("Your opponent is (" + players.get(0) + ")\n");
        }
        else if (players.size()==3 && DataBase.getSelectedGodCards().isEmpty() && !View.getError()) {
            players.remove(DataBase.getNickname());
            string.append("Your opponents are (" + players.get(0) + ", " + players.get(1) + ")\n");
        }

        if(PlayerState.ACTIVE == DataBase.getPlayerState()){
            if(View.getError()){
                string.append("Select other "+ number +" God Cards from [ ");
                DataBase.getMatchCards().stream().forEach(card -> string.append(card +", "));
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

    /**
     * Called if there is an error on the message, it announces that the input is incorrect and print the computeView method
     */
    @Override
    public String error() {
        DataBase.setActiveInput(true);
        return "Wrong input\n" +
                computeView();
    }
}
