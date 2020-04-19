package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.MessageEvent;


public class StartingStatus extends ControlState {

    @Override
    public boolean doSomething(MessageEvent messageEvent, Object viewObject) {
        messageEvent.setNickname((String) viewObject);
        return true;
    }

    @Override
    public void nextState(Controller ctrl) {
        if(ctrl.getPlayerState() == PlayerState.ACTIVE && ctrl.getMatchState() == MatchState.GETTING_PLAYERS_NUM) {
            ctrl.setState(new Selection_Number_Status());
        }
        //se non scegli il plasyersNum allora non hai diritto di parola finchè non vengono scelte le carte speciali
        else ctrl.setState(new Do_Nothing_Status());
    }

}
