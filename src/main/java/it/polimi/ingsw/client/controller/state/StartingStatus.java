package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;


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

    @Override
    public void nextState(Controller ctrl) {
        if(ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.GETTING_PLAYERS_NUM) {
            ctrl.setState(new SelectionNumberStatus());
        }
        //se non scegli il plasyersNum allora non hai diritto di parola finchè non vengono scelte le carte speciali
        else ctrl.setState(new WaitingStatus());
    }

}
