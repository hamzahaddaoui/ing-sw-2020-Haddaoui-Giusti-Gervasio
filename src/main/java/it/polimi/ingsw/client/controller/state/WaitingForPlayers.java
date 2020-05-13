package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.*;

public class WaitingForPlayers extends ControlState {

    Player player = View.getPlayer();

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

        //Controller.updateStandardData(message);

        View.setRefresh(true);
        View.print();
    }

    @Override
    public String computeView() {
        if ((player.getPlayerState() == PlayerState.ACTIVE && player.getMatchPlayers().size() > 1)){
            Optional <Map.Entry<Integer,String>> value =  player.getMatchPlayers().entrySet().stream().max(Comparator.comparing(Map.Entry::getValue));
            return value.get().getValue() + " joins to the match";
        }
        return "Wait for other players to join!";
    }

    @Override
    public void error() {

    }
}
