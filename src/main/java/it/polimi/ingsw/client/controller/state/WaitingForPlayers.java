package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;

public class WaitingForPlayers extends ControlState {

    @Override
    public MessageEvent computeInput(String input) {
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        /*Player player = View.getPlayer();
        MatchState matchState = message.getMatchState();

        player.setMatchState(matchState);
        player.setPlayerState( message.getPlayerState() );

        /*if (matchState != MatchState.WAITING_FOR_PLAYERS) {                 //PLAYERSNUM VIENE RAGGIUNTO
            GameBoard gameBoard = View.getGameBoard();
            player.setControlState(new SelectingGodCards());
            gameBoard.setMatchCards(message.getMatchCards());
        }*/

        Controller.updateStandardData(message);
        View.setRefresh(true);
        View.print();
    }

    @Override
    public String computeView() {
        return "Wait for other players to join!";
    }

    @Override
    public void error() {

    }
}
