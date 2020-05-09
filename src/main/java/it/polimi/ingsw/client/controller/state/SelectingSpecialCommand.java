package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectingSpecialCommand extends ControlState {

    GameBoard gameBoard = View.getGameBoard();
    Player player = View.getPlayer();

    MessageEvent messageEvent;

    @Override
    public MessageEvent computeInput(String input) {
        messageEvent = new MessageEvent();
        if(gameBoard.getSelectedGodCards().size() <= 0)
            throw new IllegalArgumentException("Selected god cards empty");
        //corretto
        if(gameBoard.getSelectedGodCards().stream().anyMatch(string -> (string.toUpperCase()).equals(input.toUpperCase()))){
            String card = gameBoard
                    .getSelectedGodCards()
                    .stream()
                    .filter(card1 -> card1.toUpperCase().equals(input.toUpperCase()))
                    .findAny()
                    .get();
            gameBoard.getSelectedGodCards().remove(card);
            player.setGodCard(card);
            messageEvent.setGodCard(player.getGodCard());
            messageEvent.setGodCards(gameBoard.getSelectedGodCards());
            Controller.setMessageReady(true);
            return  messageEvent;
        }
        else{
            //sbagliato
            View.setError(true);
            View.print();
            Controller.setMessageReady(false);
            Controller.setActiveInput(true);
        }
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        //caso SELECTING_SPECIAL_COMMAND
        if(player.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND && messageEvent.getMatchCards() != null){
            gameBoard.setSelectedGodCards(messageEvent.getMatchCards());
        }

        //caso ACTIVE
        if(player.getPlayerState() == PlayerState.ACTIVE){
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
        }

    }

    @Override
    public String computeView() {
        StringBuilder string = new StringBuilder();
        if(PlayerState.ACTIVE == player.getPlayerState()){
            if(View.getError()){
                string.append("Please select your card from ");
                gameBoard.getSelectedGodCards().stream().forEach(card -> string.append(card +" "));
            }
            else if(View.getRefresh()){
                string.append("Select your God Card from ");
                gameBoard.getSelectedGodCards().stream().forEach(card -> string.append(card +" "));
            }
            return string.toString();}
        else{
            return player.getPlayer() + " is selecting the cards for the match ";
        }
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        player.setGodCard(null);
        Controller.setActiveInput(true);
        computeView();
    }
}

/*    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException{

        if (super.checkMessage(viewObject)) {

            GameBoard gameBoard = View.getGameBoard();

            if (gameBoard.getSelectedGodCards().contains(viewObject)) {
                Controller.getMessage().setGodCard(viewObject);
                return true;
            } else System.out.println("CARD NOT AVAILABLE");
        }
        return false;
    }
}
*/