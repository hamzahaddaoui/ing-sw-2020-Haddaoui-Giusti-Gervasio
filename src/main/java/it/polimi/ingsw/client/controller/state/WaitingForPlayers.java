package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

public class WaitingForPlayers extends ControlState {

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
