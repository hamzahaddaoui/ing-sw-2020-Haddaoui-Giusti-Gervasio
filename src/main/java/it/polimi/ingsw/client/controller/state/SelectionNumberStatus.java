package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;

public class SelectionNumberStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        if (checkMessage(viewObject)) {
            int playersNum = Character.getNumericValue(viewObject.charAt(0));

            if (playersNum == 2 || playersNum == 3) {
                View.getPlayer().setPlayerNumber(playersNum);
                Controller.getMessage().setPlayersNum(playersNum);
                return true;
            }
            else System.out.println("NOT LEGAL VALUE");
        }
        return false;
    }

    @Override
    public boolean checkMessage(String viewObject) {
        if (super.checkMessage(viewObject)) {
            if (viewObject.length()!=1) {
                System.out.println("INCORRECT INPUT");
                return false;
            }
            else return true;
        }
        else return false;
    }
}
