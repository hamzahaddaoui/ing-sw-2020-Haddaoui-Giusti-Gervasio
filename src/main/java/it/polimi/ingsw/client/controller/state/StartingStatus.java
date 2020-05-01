package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;


public class StartingStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) throws NullPointerException,IllegalArgumentException{
        if (viewObject == null)
            throw new NullPointerException("Null message!");
        else if (viewObject.toString().equals(""))
            throw new IllegalArgumentException("The nickname is not valid!");
        Controller.getMessage().setNickname((String) viewObject);
        return true;
    }


}
