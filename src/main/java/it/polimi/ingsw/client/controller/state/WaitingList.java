package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.server.controller.state.GettingPlayersNum;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class WaitingList extends ControlState {

    @Override
    public MessageEvent computeInput(String input) {
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {

    }

    @Override
    public String computeView() {
        return null;
    }

    @Override
    public void error() {

    }
}
    /*@Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        System.out.println("WAIT FOR YOUR TURN!");

        return false;
    }

}*/
