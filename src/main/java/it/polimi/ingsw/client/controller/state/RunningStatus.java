package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

public class RunningStatus extends ControlState {

    MessageEvent message = Controller.getMessage();
    GameBoard gameBoard = View.getGameBoard();
    Player player = View.getPlayer();
    Position startingPosition = gameBoard.getStartingPosition();

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException {

        if (super.checkMessage(viewObject)) {

            if (viewObject.length() == 1)
                return processingChar(viewObject.charAt(0));
            else if (viewObject.length() == 2) {

                int x = Character.getNumericValue(viewObject.charAt(0)) - 1;
                int y = Character.getNumericValue(viewObject.charAt(1)) - 1;

                if (x <= 4 && x >= 0 && y <= 4 && y >= 0)
                    return processingPosition(x, y);
                else System.out.println("POSITION INCORRECT!");

            } else System.out.println("INPUT INCORRECT!");
        }
        return false;
    }

    private boolean processingChar(char viewObject) {
        int num = Character.getNumericValue(viewObject);
        if (num == 15) {
            Player player = View.getPlayer();

            if (startingPosition != null) {
                if (player.getSpecialFunctionAvailable().get(startingPosition)) {
                    message.setStartPosition(startingPosition);
                    message.setSpecialFunction(true);
                    return true;
                } else System.out.println("SPECIAL FUNCTION IS NOT AVAILABLE FOR THIS WORKER!");
            } else System.out.println("YOU MUST SELECT YOUR WORKER!");
        }
        else if (num == 14) {

            Player player = View.getPlayer();

            if (player.isTerminateTurnAvailable()) {
                message.setTerminateTurnAvailable(true);
                return true;
            } else System.out.println("YOU CAN'T TERMINATE THE TURN!");
        }
        else System.out.println("INPUT INCORRECT!");
        return false;
    }

    private boolean processingPosition(int x, int y) {
        Position position = new Position(x,y);

        if (startingPosition == null) {
            if (gameBoard.isWorkerPresent(position)){
                gameBoard.setStartingPosition(position);
                player.setTurnState(TurnState.MOVE);
                View.initRunning();
                View.doUpdate();
            }
            else System.out.println("WORKER IS NOT AVAILABLE!");
        }
        else if (gameBoard.getWorkersAvailableCells(startingPosition).contains(position)) {
            message.setStartPosition(startingPosition);
            message.setEndPosition(position);
            if(TurnState.MOVE == player.getTurnState())
                gameBoard.setStartingPosition(position);
            View.doUpdate();
            return true;
        }
        else System.out.println("POSITION IS NOT AVAILABLE!");
        return false;
    }
}