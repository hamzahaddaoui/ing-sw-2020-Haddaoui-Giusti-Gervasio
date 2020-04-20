package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MessageEvent;

public abstract class ControlState {

    public void nextState(Controller ctrl) {}
    public boolean processingMessage(Object viewObject) {return false;}

}
