package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;


public class StartingStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws NullPointerException,IllegalArgumentException{

        super.checkMessage(viewObject);
        Controller.getMessage().setNickname(viewObject);
        return true;
    }


}
