package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

/**
 * @author Vasio1298
 *
 * GettingPlayersNum is a state of the Controller and it handles the selection of the playersNum
 *
 */

public class GettingPlayersNum extends ControlState {
    MessageEvent message = new MessageEvent();

    /**
     * Analyzes the input String
     * If the input is correct, it prepares the message to send
     * Else it prints error announcement
     *
     * @param input  is the string from the Controller
     * @return  true is the message is ready to send, else false
     */
    @Override
    public MessageEvent computeInput(String input) {
        int playersNum = 0;

        if(input.length() == 1){
            playersNum = Character.getNumericValue(input.charAt(0));
        }
        if (playersNum == 2 || playersNum == 3) {
            DataBase.setPlayerNumber(playersNum);
            message.setPlayersNum(playersNum);
            DataBase.setMessageReady(true);
            DataBase.setPlayerState(PlayerState.IDLE);
            return message;
        }
        else {
            View.setError(true);
            View.handler();
            DataBase.setActiveInput(true);
            return null;
        }

    }

    /**
     * Method that sets the Active Input on, then it prints info to the user
     *
     * @param message  is the message of the NetworkHandler
     */
    @Override
    public void updateData(MessageEvent message) {
        DataBase.setActiveInput(true);

        View.setRefresh(true);
        View.handler();
    }

    /**
     * Method that computes the info to the user and it resets Active Input
     *
     * @return  the String to print on view
     */
    @Override
    public String computeView() {
        DataBase.setActiveInput(true);
        return "Insert the number of players (2 or 3): ";
    }

    /**
     * Method that computes the error announcement to the user and it resets Active Input
     *
     */
    @Override
    public String error() {
        DataBase.setActiveInput(true);
        return ("Incorrect number of players!");
    }
}
