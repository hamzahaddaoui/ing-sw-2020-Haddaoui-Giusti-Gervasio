package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MessageEvent;

public abstract class ControlState {

    public boolean processingMessage(String viewObject) {return false;}

    public void checkMessage(String viewObject) {
        if (viewObject == null)
            throw new NullPointerException("\nNull message!");
        else if (viewObject.equals(""))
            throw new IllegalArgumentException("\nLa stringa è vuota!");
    }

}
