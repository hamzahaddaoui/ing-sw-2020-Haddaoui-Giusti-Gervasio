package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.utilities.MessageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasio1298
 *
 * WaitingForPlayers is a state of the Controller and it handles user when is in the Waiting List
 *
 */

public class WaitingForPlayers extends ControlState {

    /**
     * Sets Input active true and calls error to print info
     *
     * @param input  is the String from the Controller
     * @return  always null
     */
    @Override
    public MessageEvent computeInput(String input) {
        DataBase.setActiveInput(true);
        error();
        return null;
    }

    /**
     * Handles the disconnection of other players. It sets Input active true and prints info
     *
     * @param message  is the message from the NetWork Handler
     */
    @Override
    public void updateData(MessageEvent message) {

        if (message.getInfo()!=null && message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.resetDataBase();
            View.setRefresh(true);
            View.print();
            return;
        }

        DataBase.setActiveInput(true);
        View.setRefresh(true);
        View.print();
    }

    /**
     * Depending on the Database's state, computes different String to print
     *
     * @return  String to print on view
     */
    @Override
    public String computeView() {
        List<String> players = new ArrayList<>(DataBase.getMatchPlayers().values());
        String lastPlayer = players.get(players.size()-1);
        if (!lastPlayer.equals(DataBase.getNickname()))
            return lastPlayer + " has joined the match!";
        else return "Wait for other players to join!";
    }

    /**
     * Prints error announcement to the user
     */
    @Override
    public void error() {
        System.out.println("Please wait\n");
    }
}
