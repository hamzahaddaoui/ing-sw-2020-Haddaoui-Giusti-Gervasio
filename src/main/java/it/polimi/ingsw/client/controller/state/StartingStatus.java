package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;

public class StartingStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws NullPointerException,IllegalArgumentException{
        if (super.checkMessage(viewObject)) {
        Controller.getMessage().setNickname(viewObject);
        return true; }
        return false;
    }

}
