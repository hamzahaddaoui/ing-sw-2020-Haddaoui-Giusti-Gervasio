package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.utilities.MessageEvent;

/**
 * @author Vasio1298
 *
 * Waiting is a state of the Controller and it handles the waiting State of the Controller
 *
 */

public class WaitingList extends ControlState {

    /**
     * Sets Active Input true
     *
     * @param input  String from controller
     * @return  always null
     */
    @Override
    public MessageEvent computeInput(String input) {
        DataBase.setActiveInput(true);
        return null;
    }

    /**
     * Sets the Input Active and print info to the user
     *
     * @param message  Message from Network Handler
     */
    @Override
    public void updateData(MessageEvent message) {
        DataBase.setActiveInput(true);
        View.setRefresh(true);
        View.print();
    }

    /**
     * Prepares the String to print
     *
     * @return String to print on view
     */
    @Override
    public String computeView() {
        return "You are in the waiting list, wait to join a match!";
    }

    /**
     * Prepares the String to print to the user
     */
    @Override
    public String error() {
        return "You are in the waiting list, wait to join a match!";
    }

}

