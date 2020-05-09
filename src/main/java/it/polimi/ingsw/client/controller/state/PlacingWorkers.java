package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlacingWorkers extends ControlState {

    GameBoard gameBoard = View.getGameBoard();
    Player player = View.getPlayer();
    MessageEvent messageEvent;
    Position position;

    @Override
    public MessageEvent computeInput(String input) {
        if(checkMessage(input)){
            //corretto di lunghezza due
            Set<Position> placingPosition= View.getGameBoard().getPlacingAvailableCells();
            if(placingPosition.size() == 0){
                throw new IllegalArgumentException("PLACING POSITION IS EMPTY!");
            }

            int x = Character.getNumericValue(input.charAt(0))-1;
            int y = Character.getNumericValue(input.charAt(1))-1;

            if (x <= 4 && x >= 0 && y <= 4 && y >= 0 ){
                position = new Position(x, y);
                if (gameBoard.getPlacingAvailableCells().contains(position)){
                    gameBoard.getWorkersAvailableCells().put(position, new HashSet<>());
                    gameBoard.getPlacingAvailableCells().remove(position);
                    View.doUpdate();
                    if(gameBoard.getWorkersAvailableCells().size() == 1){
                        View.setError(true);
                        computeView();
                        View.setError(false);
                        Controller.setMessageReady(false);
                        Controller.setActiveInput(true);
                        return null;
                    }
                    if (gameBoard.getWorkersAvailableCells().size() == 2) {
                        messageEvent.setInitializedPositions(gameBoard.getWorkersPositions());
                        Controller.setMessageReady(true);
                        return messageEvent;
                    }
                }
            }
            System.out.println("POSITION INCORRECT!");
        }
        View.setError(true);
        computeView();
        View.setError(false);
        Controller.setMessageReady(false);
        Controller.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        //caso PLACING_WORKERS
        if(player.getMatchState() == MatchState.PLACING_WORKERS){
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

        View.doUpdate();
    }

    @Override
    public String computeView() {
        int number = 2 - gameBoard.getWorkersPositions().size();
        if(number == 1){
            return "Insert " + number + " worker: \n ";
        }
        else if(number == 2){
            return "Insert " + number + " workers: \n";
        }
        else return "INSERIMENTO COMPLETATO";
    }

    @Override
    public void error() {
        System.out.println("Input wrong\n");
        gameBoard.setSelectedGodCards(new HashSet<>());
        Controller.setActiveInput(true);
        System.out.println("Select " + player.getPlayerNumber() + " God Cards:");
    }

    private boolean checkMessage(String viewObject) {
        if (viewObject.length() != 2) {
            System.out.println("INPUT INCORRECT");
            return false;
        }
        else return true;
    }

}
   /* Set<Position> initializedPositions = new HashSet<>();
    Position position;

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException {
        if (checkMessage(viewObject)) {
            Set<Position> placingPosition= View.getGameBoard().getPlacingAvailableCells();
            if(placingPosition.size() == 0){
                throw new IllegalArgumentException("PLACING POSITION IS EMPTY!");
            }

            int x = Character.getNumericValue(viewObject.charAt(0))-1;
            int y = Character.getNumericValue(viewObject.charAt(1))-1;

            if (x <= 4 && x >= 0 && y <= 4 && y >= 0)
                position = new Position(x, y);
            else {
                System.out.println("POSITION INCORRECT!");
                return false;
            }

            GameBoard gameBoard = View.getGameBoard();
            if (gameBoard.getPlacingAvailableCells().contains(position)){
                initializedPositions.add(position);
                gameBoard.getPlacingAvailableCells().remove(position);
                View.doUpdate();
                if(initializedPositions.size() == 1){
                    System.out.println("INSERT THE NEXT POSITION");
                }
                if (initializedPositions.size() == 2) {
                    Controller.getMessage().setInitializedPositions(initializedPositions);
                    return true;
                }
            }
            else {
                System.out.println("POSITION IS NOT AVAILABLE!");
                return false;
            }
        }
        return false;
    }

}*/