package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.Client;
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

        if(input.equals("q")||input.equals("Q")) {
            Client.close();
            return null;
        }
        if(input.equals("")){
            Controller.setActiveInput(true);
            System.out.println("\nInsert something different\n");
            return null;
        }
        MessageEvent message = new MessageEvent();

        player.setNickname(input);
        message.setNickname(input);
        Controller.setMessageReady(true);

        return message;
    }

    @Override
    public void updateData(MessageEvent message) {
        Player player = View.getPlayer();
        if (player.getPlayerState()==PlayerState.WIN || player.getPlayerState()==PlayerState.LOST){
            System.out.println(computeView());
            Player player1 = new Player();
            GameBoard gameBoard1 = new GameBoard();
            View.setPlayer(player1);
            View.setGameBoard(gameBoard1);
            }
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
        Controller.setActiveInput(true);
    }

    @Override
    public String computeView() {
        Player player = View.getPlayer();
        if (player.getPlayerState() != null && player.getPlayerState()==PlayerState.WIN)
            return "Congratulations! You are the winner!\n\nIf you want to play again insert your nickname\nPress 'q' to disconnect: ";
        else if (player.getPlayerState() != null && player.getPlayerState() == PlayerState.LOST)
            return "Unlucky! You lost!\n\nIf you want to play again insert your nickname.\nPress 'q' to disconnect: ";
        else if (player.getMatchState()!= null)
            return "A user has disconnected from the match so the match is over.\n If you want to play again insert your nickname\nPress 'q' to disconnect: ";
        else return "\nPress 'q' if you want to quit from SANTORINI.\nTo start a game insert your nickname: ";
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
