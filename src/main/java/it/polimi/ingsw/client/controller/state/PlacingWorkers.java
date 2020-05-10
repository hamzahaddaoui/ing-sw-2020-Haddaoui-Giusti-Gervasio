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
    MessageEvent messageEvent = new MessageEvent();
    Position position;
    Set <Position> initializedPosition = new HashSet<>();

    @Override
    public MessageEvent computeInput(String input) {
        if(checkMessage(input)){
            //corretto di lunghezza due
            Set<Position> placingPosition= gameBoard.getPlacingAvailableCells();
            if(placingPosition.size() == 0){
                throw new IllegalArgumentException("PLACING POSITION IS EMPTY!");
            }

            int x = Character.getNumericValue(input.charAt(0))-1;
            int y = Character.getNumericValue(input.charAt(1))-1;

            if (x <= 4 && x >= 0 && y <= 4 && y >= 0 ){
                position = new Position(x, y);
                if (placingPosition.contains(position)){
                    initializedPosition.add(position);
                    placingPosition.remove(position);
                    if(initializedPosition.size() == 1){
                        System.out.println(computeView());
                        Controller.setMessageReady(false);
                        Controller.setActiveInput(true);
                        return null;
                    }
                    if (initializedPosition.size() == 2) {
                        messageEvent.setInitializedPositions(initializedPosition);
                        System.out.println(computeView());
                        Controller.setMessageReady(true);
                        player.setPlayerState(PlayerState.IDLE);
                        return messageEvent;
                    }
                }
            }
        }
        error();
        Controller.setMessageReady(false);
        Controller.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        //caso PLACING_WORKERS
        if(player.getMatchState() == MatchState.PLACING_WORKERS){
            if(message.getBillboardStatus() != gameBoard.getBillboardStatus() && message.getBillboardStatus() != null){
                gameBoard.setBillboardStatus(message.getBillboardStatus());
            }
            if(message.getAvailablePlacingCells() != gameBoard.getPlacingAvailableCells() && message.getAvailablePlacingCells() != null){
                gameBoard.setPlacingAvailableCells(message.getAvailablePlacingCells());
            }
        }

        View.doUpdate();
        //caso ACTIVE
        if(player.getPlayerState() == PlayerState.ACTIVE){
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
        }
        else{
            System.out.println(computeView());
        }

    }

    @Override
    public String computeView() {
        if(player.getPlayerState()==PlayerState.ACTIVE ){
            int number = 2 - initializedPosition.size();
            if(number == 0)
                return "Addition done";
            else if(number == 1){
                return "Insert " + number + " worker.\nInsert position XY: \n ";
            }
            else if(number == 2){
                return "Insert " + number + " workers.\nInsert position XY:\n";
            }
            else return "INSERIMENTO COMPLETATO";
        }
        else{
            return player.getMatchPlayers().get(player.getPlayer()) +" is placing his workers";
        }
    }

    @Override
    public void error() {
        System.out.println("Position is not available or incorrect\n");
        Controller.setActiveInput(true);
        System.out.println(computeView());
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