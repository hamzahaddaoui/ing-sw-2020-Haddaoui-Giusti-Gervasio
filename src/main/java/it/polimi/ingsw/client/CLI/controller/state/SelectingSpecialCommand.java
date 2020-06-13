package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

/**
 * @author giusti-leo
 *
 * SelectingSpecialCommand is a state of the Controller that handle the selection of Special GodCard for the Match
 *
 */

public class SelectingSpecialCommand extends ControlState {

    MessageEvent messageEvent = new MessageEvent();

    /**
     * It analyzes input from Controller.
     * If the input is correct and user has selected a God ,it prepares the message to the NetWork Handler and it puts the
     * PlayerState equals to Idle to allow correct computeView
     * If the input is not correct, it prints an advice
     *
     * @param input  is the GodCard ,if it is correct
     * @return  true if the Input is equals to a Card of SelectedGod Cards, else false
     */
    @Override
    public MessageEvent computeInput(String input) {
        messageEvent = new MessageEvent();

        if(DataBase.getSelectedGodCards().size() <= 0)
            throw new IllegalArgumentException("Selected god cards empty");

        if(DataBase.getSelectedGodCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))){
            String card = DataBase
                    .getSelectedGodCards()
                    .stream()
                    .filter(card1 -> card1.toUpperCase().equals(input.toUpperCase()))
                    .findAny()
                    .get();
            DataBase.getSelectedGodCards().remove(card);
            DataBase.setGodCard(card);
            messageEvent.setGodCard(DataBase.getGodCard());
            messageEvent.setGodCards(DataBase.getSelectedGodCards());
            DataBase.setMessageReady(true);
            DataBase.setPlayerState(PlayerState.IDLE);
            return  messageEvent;
        }
        else{

            View.setError(true);
            View.handler();
            DataBase.setMessageReady(false);
            DataBase.setActiveInput(true);
        }
        return null;
    }

    /**
     * It contains Disconnection case.
     * Updates the database through the message.
     * Prints which are the Gods for the Match. Depending on the state of DataBase prints a different output message
     *
     * @param message  is the message from Network Handler
     */
    @Override
    public void updateData(MessageEvent message) {
        if (message.getInfo()!=null && message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.setDisconnectedUser(true);
            DataBase.resetDataBase();
            DataBase.setActiveInput(false);
            View.setRefresh(true);
            View.handler();
            DataBase.setDisconnectedUser(false);
            return;
        }

        DataBase.setPlayer(message.getCurrentPlayer());

        if( message.getMatchCards() != null){
            DataBase.setSelectedGodCards(message.getMatchCards());
        }

        if(DataBase.getPlayerState() == PlayerState.ACTIVE){
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
     * Depending on the Database's state, computes different String to print
     *
     * @return  String to print on view
     */
    @Override
    public String computeView() {
        StringBuilder string = new StringBuilder();
        if(PlayerState.ACTIVE == DataBase.getPlayerState()){
            if(View.getError()){
                string.append("Please select your card from [ ");
                DataBase.getSelectedGodCards().forEach(card -> string.append(card).append(", "));
                string.deleteCharAt(string.length()-2);
                string.append("]");
            }
            else if(View.getRefresh()){
                if(DataBase.getSelectedGodCards().size()==1){
                    string.append("The last God is [ ");
                    DataBase.getSelectedGodCards().forEach(string::append);
                    string.append(" ] . Please select it.");
                }
                else {
                    string.append("Select your God Card from [ ");
                    DataBase.getSelectedGodCards().forEach(card -> string.append(card).append(", "));
                    string.deleteCharAt(string.length()-2);
                    string.append("]");
                }
            }
            return string.toString();}
        else{
            string.append(DataBase.getMatchPlayers().get(DataBase.getPlayer())  + " is selecting his God for the match from [ " );
            DataBase.getSelectedGodCards().forEach(card -> string.append(card).append(", "));
            string.deleteCharAt(string.length()-2);
            string.append("]");
            return string.toString();
        }
    }

    /**
     * Called if there is an error on the message, it announces that the input is incorrect and it prints the computeView method
     */
    @Override
    public String error() {
        DataBase.setGodCard(null);
        DataBase.setActiveInput(true);
        return "Wrong input\n" +
                computeView();
    }
}
