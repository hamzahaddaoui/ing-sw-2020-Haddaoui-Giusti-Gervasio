package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.controller.state.GettingPlayersNum;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class WaitingList extends ControlState {

    @Override
    public MessageEvent computeInput(String input) {
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        Player player = View.getPlayer();
        MatchState matchState = message.getMatchState();

        player.setMatchState(matchState);
        player.setPlayerState( message.getPlayerState() );

        if (matchState == MatchState.WAITING_FOR_PLAYERS)                   //ENTRO NEL MATCH MA MANCA IL TERZO GIOCATORE
            player.setControlState(new WaitingForPlayers());
        else {
            GameBoard gameBoard = View.getGameBoard();
            player.setControlState(new SelectingGodCards());                //ENTRO E IMMEDIATAMENTE IL NUMERO DI GIOCATORI VIENE RAGGIUNTO
            gameBoard.setMatchCards(message.getMatchCards());
        }

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
