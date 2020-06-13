package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.Client;
import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

/**
 * @author Vasio1298
 *
 * Not Initiazed is a state of the Controller and it handles user when is not part of a match
 *
 */

public class NotInitialized extends ControlState{

    /**
     * It analyzes the input String that is the nickname and it sets input in the database
     *
     * @param input  is Controller String input
     * @return  true is the input is correct , else false
     */
    @Override
    public MessageEvent computeInput(String input) {

        if(DataBase.isViewer() &&(!input.equals("q") && !input.equals("Q"))){
            DataBase.setActiveInput(true);
            System.out.println("\nYou can only disconnect by pressing 'q' or continue Viewer Mode\n");
            return null;
        }
        if(input.equals("")){
            DataBase.setActiveInput(true);
            System.out.println("\nInsert something different\n");
            return null;
        }
        else if (DataBase.getNickname()!=null && input.toUpperCase().equals(DataBase.getNickname().toUpperCase())) {
            DataBase.setActiveInput(true);
            System.out.println("Are you kidding? Choose a new one!");
            return null;
        }

        MessageEvent message = new MessageEvent();

        DataBase.setNickname(input);
        message.setNickname(input);
        DataBase.setMessageReady(true);

        if(input.equals("q") || input.equals("Q") ) {
            DataBase.setActiveInput(false);
            Client.close();
            return null;
        }

        return message;
    }

    /**
     * Method handles the reset of Database when the old match is ended. It sets the Input Active.
     *
     * @param message  is the message from the NetWork Handler
     */
    @Override
    public void updateData(MessageEvent message) {

        if (DataBase.getPlayerState() == PlayerState.WIN || DataBase.getPlayerState() == PlayerState.LOST || DataBase.isViewer()){
            if (DataBase.getMatchState()!=MatchState.FINISHED)
                DataBase.setActiveInput(true);

            if(message.getInfo()!=null && !message.getInfo().equals("Match data update") ){
                System.out.println(message.getInfo());
            }
            DataBase.setBillboardStatus(message.getBillboardStatus());
            View.setRefresh(true);
            View.handler();
            }


    }

    /**
     * Depending on the Database's state, it computes different String to print
     *
     * @return  String to print on view
     */
    @Override
    public String computeView() {
        if((MatchState.FINISHED == DataBase.getMatchState() && DataBase.getPlayerState() != PlayerState.WIN))
            System.out.println("The winner is "+ DataBase.getMatchPlayers().values().toString());
        if(DataBase.isViewer()) {
            if(DataBase.getMatchPlayers().size()==1)
                return "Viewer mode off. Server is trying to close the connection...\n";
            else
                return "Viewer mode on. Press 'q' if you want to quit or wait until the end of the game.";
        }
        if (DataBase.getPlayerState() != null && DataBase.getPlayerState() == PlayerState.WIN)
            return "Congratulations! You are the winner!\n\nServer is trying to close the connection...";
        else if (DataBase.getPlayerState() != null && DataBase.getPlayerState() == PlayerState.LOST){
            return "Unlucky! You lost!\n\nServer is trying to close the connection... ";}
        else if (DataBase.isDisconnectedUser())
            return "A user has disconnected from the match so the match is over.\nServer is trying to close the connection...";
        else return "\nPress 'q' if you want to quit from SANTORINI.\nTo start a game insert your nickname: ";
    }

    /**
     * Called if there is an error on the message, it announces that the input is incorrect and it prints the computeView method
     */
    @Override
    public String error() {
        DataBase.setActiveInput(true);
        return("Nickname already taken!\nInsert a new one or press 'q' to quit disconnect: ");
    }

}