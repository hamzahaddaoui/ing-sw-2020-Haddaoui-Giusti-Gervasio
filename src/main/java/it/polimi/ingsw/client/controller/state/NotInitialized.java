package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotInitialized extends ControlState{

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    Player player = View.getPlayer();

    @Override
    public MessageEvent computeInput(String input) {
        MessageEvent message = new MessageEvent();
        player.setNickname(input);
        message.setNickname(input);
        Controller.setMessageReady(true);
        return message;
    }

    @Override
    public void updateData(MessageEvent message) {


        player.setMatchState( message.getMatchState() );
        player.setPlayerState( message.getPlayerState() );

        if(message.getPlayerState() == PlayerState.ACTIVE){    //HO CREATO UN MATCH
            player.setControlState(new GettingPlayersNum());
            Controller.setActiveInput(true);
        }
        else if (message.getMatchState() == MatchState.NONE){  //SONO NELLA LISTA D'ATTESA
            player.setControlState(new WaitingList());
        }
        else {                                                      //SONO ENTRATO IN UN MATCH
            player.setMatchPlayers(message.getMatchPlayers());
            player.setCurrentPlayer(message.getCurrentPlayer());
            if (message.getMatchState() == MatchState.WAITING_FOR_PLAYERS)
                player.setControlState(new WaitingForPlayers());
            else {
                GameBoard gameBoard = View.getGameBoard();
                player.setControlState(new SelectingSpecialCommand());
                gameBoard.setMatchCards(message.getMatchCards());
            }
        }
        View.setRefresh();
        executor.submit(()->View.print());
        //new Thread(View::print).start();
    }

    @Override
    public String computeView() {
        return "Insert your nickname: ";
    }

    @Override
    public void error() {
        System.out.println("Nickname already taken!");
        player.setNickname(null);
        Controller.setActiveInput(true);
    }
}
