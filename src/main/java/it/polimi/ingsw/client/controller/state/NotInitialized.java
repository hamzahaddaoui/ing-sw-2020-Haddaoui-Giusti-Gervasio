package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

public class NotInitialized extends ControlState{

    @Override
    public MessageEvent computeInput(String input) {
        //DataBase dataBase = View.getDataBase();

        if(input.equals("q")||input.equals("Q")) {
            Client.close();
            return null;
        }
        if(input.equals("")){
            DataBase.setActiveInput(true);
            System.out.println("\nInsert something different\n");
            return null;
        }
        MessageEvent message = new MessageEvent();

        DataBase.setNickname(input);
        message.setNickname(input);
        DataBase.setMessageReady(true);

        return message;
    }

    @Override
    public void updateData(MessageEvent message) {
        //DataBase dataBase = View.getDataBase();
        if (DataBase.getPlayerState()==PlayerState.WIN || DataBase.getPlayerState()==PlayerState.LOST){
            System.out.println(computeView());
            DataBase.resetDataBase();
            /*DataBase dataBase1 = new DataBase();
            View.setDataBase(dataBase1);*/
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
        DataBase.setActiveInput(true);
    }

    @Override
    public String computeView() {
        //DataBase dataBase = View.getDataBase();
        if (DataBase.getPlayerState() != null && DataBase.getPlayerState()==PlayerState.WIN)
            return "Congratulations! You are the winner!\n\nIf you want to play again insert your nickname, else press 'q' to disconnect: ";
        else if (DataBase.getPlayerState() != null && DataBase.getPlayerState() == PlayerState.LOST)
            return "Unlucky! You lost!\n\nIf you want to play again insert your nickname, else press 'q' to disconnect: ";
        else if (DataBase.getMatchState()!= null)
            return "A user has disconnected from the match so the match is over.\nIf you want to play again insert your nickname, else press 'q' to disconnect: ";
        else return "\nPress 'q' if you want to quit from SANTORINI.\nTo start a game insert your nickname: ";
    }

    @Override
    public void error() {
        //DataBase dataBase = View.getDataBase();
        System.out.println("Nickname already taken!\n");
        DataBase.setNickname(null);
        DataBase.setActiveInput(true);
        System.out.println(computeView());
    }
}