package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class NotInitialized extends ControlState{

    @Override
    public MessageEvent computeInput(String input) {
        Player player = View.getPlayer();
        MessageEvent message = new MessageEvent();

        player.setNickname(input);
        message.setNickname(input);
        Controller.setMessageReady(true);

        return message;
    }

    @Override
    public void updateData(MessageEvent message) {
        //Controller.updateStandardData(message);
        /*player.setMatchState( message.getMatchState() );
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
                player.setControlState(new SelectingGodCards());
                gameBoard.setMatchCards(message.getMatchCards());
            }
        }

        View.setRefresh(true);
        View.print();*/
    }

    @Override
    public String computeView() {
        Player player = View.getPlayer();
        if (player.getPlayerState() != null && player.getPlayerState()==PlayerState.WIN)
            return "Congratulations! You are the winner!";
        else if (player.getPlayerState() != null && player.getPlayerState() == PlayerState.LOST)
            return "Unlucky! You lost!";
        else return "Insert your nickname: ";
    }

    @Override
    public void error() {
        Player player = View.getPlayer();
        System.out.println("Nickname already taken!\n");
        player.setNickname(null);
        Controller.setActiveInput(true);
        System.out.println(computeView());
    }
}
