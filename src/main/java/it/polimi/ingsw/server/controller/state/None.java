package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;


/**
 * @author: hamzahaddaoui
 *
 * Controller class related to NO state. Does nothing.
 */


public class None extends State {
    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        return false;
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
    }
}
