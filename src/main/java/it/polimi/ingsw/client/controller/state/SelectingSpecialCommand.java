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
        // aggiornare i dati base
        player.setMatchState(messageEvent.getMatchState());
        player.setPlayerState(messageEvent.getPlayerState());
        player.setTurnState(messageEvent.getTurnState());
        if(messageEvent.getMatchPlayers() != player.getMatchPlayers() && messageEvent.getMatchPlayers() != null)
            player.setMatchPlayers(messageEvent.getMatchPlayers());
        if(messageEvent.getCurrentPlayer() != player.getPlayer())
            player.setPlayer(messageEvent.getCurrentPlayer());

        //caso SELECTING_SPECIAL_COMMAND
        if(player.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND && messageEvent.getMatchCards() != null){
            gameBoard.setSelectedGodCards(messageEvent.getMatchCards());
        }

        //caso PLACING_WORKERS
        if(player.getMatchState() == MatchState.PLACING_WORKERS){
            player.setControlState(new PlacingWorkers());
            if(messageEvent.getBillboardStatus() != gameBoard.getBillboardStatus() && messageEvent.getBillboardStatus() != null){
                gameBoard.setBillboardStatus(messageEvent.getBillboardStatus());
            }
            if(messageEvent.getAvailablePlacingCells() != gameBoard.getPlacingAvailableCells() && messageEvent.getAvailablePlacingCells() != null){
                gameBoard.setPlacingAvailableCells(messageEvent.getAvailablePlacingCells());
            }
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
            return  "Select a God Card from "+ gameBoard.getSelectedGodCards().toString();
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        player.setGodCard(null);
        Controller.setActiveInput(true);
        System.out.println("Select GodCard from "+ gameBoard.getSelectedGodCards() +" : ");
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