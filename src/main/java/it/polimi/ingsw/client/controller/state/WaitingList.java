package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;

public class WaitingList extends ControlState {

    @Override
    public MessageEvent computeInput(String input) {
        DataBase.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        DataBase.setActiveInput(true);
        /*Player player = View.getPlayer();
        MatchState matchState = message.getMatchState();

        player.setMatchState(matchState);
        player.setPlayerState( message.getPlayerState() );

        /*if (matchState == MatchState.WAITING_FOR_PLAYERS)                   //ENTRO NEL MATCH MA MANCA IL TERZO GIOCATORE
            player.setControlState(new WaitingForPlayers());
        else {
            GameBoard gameBoard = View.getGameBoard();
            player.setControlState(new SelectingGodCards());                //ENTRO E IMMEDIATAMENTE IL NUMERO DI GIOCATORI VIENE RAGGIUNTO
            gameBoard.setMatchCards(message.getMatchCards());
        }*/

        //Controller.updateStandardData(message);
        View.setRefresh(true);
        View.print();
    }

    @Override
    public String computeView() {
        return "You are in the waiting list, wait to join a match!";
    }

    @Override
    public void error() {

    }
}
    /*@Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        System.out.println("WAIT FOR YOUR TURN!");

        return false;
    }

}*/
