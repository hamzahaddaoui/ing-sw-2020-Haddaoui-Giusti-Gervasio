package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class WaitingForPlayers extends ControlState {

    Player player = View.getPlayer();

    @Override
    public MessageEvent computeInput(String input) {
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            player.setControlState(new NotInitialized());
            player.setPlayerState(null);
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }

        /*Player player = View.getPlayer();
        MatchState matchState = message.getMatchState();

        player.setMatchState(matchState);
        player.setPlayerState( message.getPlayerState() );

        /*if (matchState != MatchState.WAITING_FOR_PLAYERS) {                 //PLAYERSNUM VIENE RAGGIUNTO
            GameBoard gameBoard = View.getGameBoard();
            player.setControlState(new SelectingGodCards());
            gameBoard.setMatchCards(message.getMatchCards());
        }*/

        //Controller.updateStandardData(message);
        View.setRefresh(true);
        View.print();
    }

    @Override
    public String computeView() {
        List<String> players = new ArrayList<>(player.getMatchPlayers().values());
        String lastPlayer = players.get(players.size()-1);
        if (!lastPlayer.equals(player.getNickname()))
            return lastPlayer + " has joined the match!";
        else return "Wait for other players to join!";
    }

    @Override
    public void error() {

    }
}
