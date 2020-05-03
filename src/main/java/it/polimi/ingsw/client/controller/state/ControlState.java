package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MessageEvent;

public abstract class ControlState {

    public boolean processingMessage(String viewObject) {return false;}

    public boolean checkMessage(String viewObject) {
        if (viewObject == null) {
            System.out.println("Null message!");
            return false; }
        else if (viewObject.equals("")) {
            System.out.println("La stringa è vuota!");
            return false;
        }
        return true;
    }
}
