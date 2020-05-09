package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;

public class GettingPlayersNum extends ControlState {

    @Override
    public MessageEvent computeInput(String input) {

        MessageEvent message = new MessageEvent();
        int playersNum = Character.getNumericValue(input.charAt(0));

        if (playersNum == 2 || playersNum == 3) {
            View.getPlayer().setPlayerNumber(playersNum);
            message.setPlayersNum(playersNum);
            Controller.setMessageReady(true);
            return message;
        }
        else {
            View.setError();
            new Thread(View::print).start();
            return null;
        }
    }

    @Override
    public void updateData(MessageEvent message) {
        Player player = View.getPlayer();

        player.setMatchState( message.getMatchState() );
        player.setPlayerState( message.getPlayerState() );
        //player.setPlayerNumber(message.getPlayersNum());
        player.setControlState(new SelectingGodCards());
        Controller.setActiveInput(true);

        View.setRefresh();
        View.print();
    }

    @Override
    public String computeView() {
        return "Insert the number of players (2 or 3): ";
    }

    @Override
    public void error() {
        System.out.println("Incorrect number of players!");
        Controller.setActiveInput(true);
    }
}

    /*@Override
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
*/
