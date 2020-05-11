package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.utilities.*;

public class Running extends ControlState {

    //TODO CHIUSURA CLIENT NEL CASO DI PARTITA FINITA


    GameBoard gameBoard = View.getGameBoard();
    Player player = View.getPlayer();
    MessageEvent message = new MessageEvent() ;

    @Override
    public MessageEvent computeInput(String input) {

        if (input.length() == 1 && processingChar(input.charAt(0))) {
            Controller.setMessageReady(true);
            player.setPlayerState(PlayerState.IDLE);
            return message;
        }
        else if (input.length() == 2) {
            int x = Character.getNumericValue(input.charAt(0)) - 1;
            int y = Character.getNumericValue(input.charAt(1)) - 1;

            if (x <= 4 && x >= 0 && y <= 4 && y >= 0 && processingPosition(x, y)){
                //View.doUpdate();
                return message;}
        }
        View.setError(true);
        View.print();
        Controller.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {
        View.doUpdate();

        /*if (message.getMatchState() == MatchState.FINISHED) {
            if (message.getPlayerState()==PlayerState.WIN)
                System.out.println("Congratulations! You are the winner!");
            else System.out.println("Unlucky! You lost!");
            player.setControlState(new NotInitialized());
        }
        else*/
        if (message.getPlayerState()==PlayerState.LOST) {
            System.out.println("Unlucky! You lost!");
            player.setControlState(new NotInitialized());
        }
        else {
            /*player.setMatchState(message.getMatchState());
            player.setPlayerState(message.getPlayerState());
            player.setTurnState(message.getTurnState());
            player.setMatchPlayers(message.getMatchPlayers());
            player.setCurrentPlayer(message.getCurrentPlayer());*/

            //Controller.updateStandardData(message);

            gameBoard.setBillboardStatus(message.getBillboardStatus());

            if (message.getPlayerState() == PlayerState.ACTIVE) {
                gameBoard.setWorkersAvailableCells(message.getWorkersAvailableCells());
                player.setTerminateTurnAvailable(message.getTerminateTurnAvailable());
                player.setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
                Controller.setActiveInput(true);
            }
            else if (gameBoard.getStartingPosition()!=null)
                gameBoard.setStartingPosition(null);


            View.setRefresh(true);
            View.print();
        }
    }

    @Override
    public String computeView() {
        Position startingPosition = gameBoard.getStartingPosition();

        if (player.getPlayerState()!= PlayerState.ACTIVE)
            return player.getMatchPlayers().get(player.getPlayer())+" is doing his turn";

        if (startingPosition==null)
            return "Choose your starting worker, insert its coordinates: ";
        else if (player.getSpecialFunctionAvailable()!=null && player.getSpecialFunctionAvailable().get(startingPosition) && player.isTerminateTurnAvailable()) {
            if (player.getTurnState() == TurnState.MOVE)
                return "Insert the position you want to move to OR type 'f' to use your special function OR type 'e' to terminate your turn: ";
            else //if (player.getTurnState() == TurnState.BUILD)
                return "Insert the position you want to build in OR type 'f' to use your special function OR type 'e' to terminate your turn: ";
        } else if (player.getSpecialFunctionAvailable()!=null && player.getSpecialFunctionAvailable().get(startingPosition)) {
            if (player.getTurnState() == TurnState.MOVE)
                return "Insert the position you want to move to OR type 'f' to use your special function: ";
            else //if (player.getTurnState() == TurnState.BUILD)
                return "Insert the position you want to build in OR type 'f' to use your special function: ";
        } else if (player.isTerminateTurnAvailable()) {
            if (player.getTurnState() == TurnState.MOVE)
                return "Insert the position you want to move to OR type 'e' to terminate your turn: ";
            else //if (player.getTurnState() == TurnState.BUILD)
                return "Insert the position you want to build in OR type 'e' to terminate your turn: ";
        } else {
            if (player.getTurnState() == TurnState.MOVE)
                return "Insert the position you want to move to: ";
            else //if (player.getTurnState() == TurnState.BUILD)
                return "Insert the position you want to build in: ";
        }
    }

    @Override
    public void error() {
        Controller.setActiveInput(true);
    }

    private boolean processingChar(char input) {
        message = new MessageEvent();
        int num = Character.getNumericValue(input);

        if (num == 15) {
            Position startingPosition = gameBoard.getStartingPosition();

            if (startingPosition != null) {
                if (player.getSpecialFunctionAvailable().get(startingPosition)) {
                    message.setStartPosition(startingPosition);
                    message.setSpecialFunction(true);
                    //View.doUpdate();

                    return true;
                } else System.out.println("SPECIAL FUNCTION IS NOT AVAILABLE FOR THIS WORKER!");
            } else System.out.println("YOU MUST SELECT YOUR WORKER!");
        }
        else if (num == 14) {

            if (player.isTerminateTurnAvailable()) {
                message.setTerminateTurnAvailable(true);
                return true;
            } else System.out.println("YOU CAN'T TERMINATE THE TURN!");
        }
        else System.out.println("INPUT INCORRECT!");
        return false;
    }

    private boolean processingPosition(int x, int y) {
        Position startingPosition = gameBoard.getStartingPosition();
        Position position = new Position(x,y);
        message = new MessageEvent();

        if (startingPosition == null) {
            if (gameBoard.isWorkerPresent(position)){
                gameBoard.setStartingPosition(position);
                player.setTurnState(TurnState.MOVE);
                View.doUpdate();
                System.out.println(computeView());
                Controller.setActiveInput(true);
                return true;
            }
            else System.out.println("WORKER IS NOT AVAILABLE!");
        }
        else if (gameBoard.getWorkersAvailableCells(startingPosition).contains(position)) {
            message.setStartPosition(startingPosition);
            message.setEndPosition(position);
            Controller.setMessageReady(true);
            if (player.getTurnState()==TurnState.MOVE)
                gameBoard.setStartingPosition(position);
            player.setPlayerState(PlayerState.IDLE);
            return true;
        }
        else System.out.println("POSITION IS NOT AVAILABLE!");
        return false;
    }
}
   /* MessageEvent message = Controller.getMessage();
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
}*/