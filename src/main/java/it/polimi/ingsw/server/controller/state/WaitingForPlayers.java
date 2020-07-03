package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;

import static it.polimi.ingsw.server.model.GameModel.*;

/**
 * @author: hamzahaddaoui
 *
 * Controller class that manages the waiting state inputs by the users.
 * No input is permitted in this phase
 */

public class WaitingForPlayers extends State{
    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        return false;
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
        MessageEvent message = basicMatchConfig(new MessageEvent(), matchID);
        getMatchPlayers(matchID)
                .keySet()
                .forEach(player -> notify(observers, basicPlayerConfig(message, player)));
    }
}
