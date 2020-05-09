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

public class PlacingWorkers extends ControlState {

    @Override
    public MessageEvent computeInput(String input) {
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {

    }

    @Override
    public String computeView() {
        return null;
    }

    @Override
    public void error() {

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

    @Override
    public boolean checkMessage(String viewObject) {
        if (super.checkMessage(viewObject)) {
            if (viewObject.length() != 2) {
                System.out.println("INPUT INCORRECT");
                return false;
            } else return true;
        }
        return false;
    }
}*/