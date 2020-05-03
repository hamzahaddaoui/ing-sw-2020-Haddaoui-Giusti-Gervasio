package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

public class SelectionNumberStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        if (checkMessage(viewObject)) {
            int playersNum = Character.getNumericValue(viewObject.charAt(0));
            System.out.println("insertvalue"+playersNum);

            if (playersNum == 2 || playersNum == 3) {
                Controller.getMessage().setPlayersNum(playersNum);
                return true;
            }
            else System.out.println("valore non lecito");
        }
        return false;
    }

    @Override
    public boolean checkMessage(String viewObject) {
        if (super.checkMessage(viewObject)) {
            System.out.println("V");
            if (viewObject.length()>=1) {
                System.out.println("input incorretto");
                return false;
            }
            else return true;
        }
        else return false;
    }
}
