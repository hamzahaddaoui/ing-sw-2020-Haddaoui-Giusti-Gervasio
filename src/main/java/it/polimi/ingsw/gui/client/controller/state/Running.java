package it.polimi.ingsw.gui.client.controller.state;

import it.polimi.ingsw.gui.client.view.DataBase;
import it.polimi.ingsw.gui.client.view.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.Optional;

/*
    TODO -> MOSTRA POTERI CHE VENGONO PRESI DAGLI USER
 */
public class Running extends ControlState {

    //DataBase dataBase = View.getDataBase();

    MessageEvent message = new MessageEvent();

    @Override
    public MessageEvent computeInput(String input) {

        if (input.length() == 1 && processingChar(input.charAt(0))) {
            DataBase.setMessageReady(true);
            DataBase.setPlayerState(PlayerState.IDLE);
            return message;
        }
        else if (input.length() == 2) {
            int x = Character.getNumericValue(input.charAt(0)) - 1;
            int y = Character.getNumericValue(input.charAt(1)) - 1;

            if (x <= 4 && x >= 0 && y <= 4 && y >= 0 && processingPosition(x, y)){
                return message;}
        }
        View.setError(true);
        View.print();
        DataBase.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.setControlState(new NotInitialized());
            DataBase.setPlayerState(null);
            DataBase.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }

        if(!message.getInfo().equals("Match data update")){
            System.out.println(message.getInfo());
        }
        /*if (message.getMatchState() == MatchState.FINISHED) {
            /*if (message.getPlayerState()==PlayerState.WIN)
                System.out.println("Congratulations! You are the winner!");
            else System.out.println("Unlucky! You lost!");
            player.setControlState(new NotInitialized());
            View.setRefresh(true);
            View.print();
        }
        else*/
        if (message.getPlayerState() == PlayerState.LOST) {
            DataBase.setControlState(new NotInitialized());
            DataBase.getControlState().updateData(message);
        }
        else {
            View.doUpdate();

            DataBase.setBillboardStatus(message.getBillboardStatus());

            if (message.getPlayerState() == PlayerState.ACTIVE) {
                DataBase.setWorkersAvailableCells(message.getWorkersAvailableCells());
                DataBase.setTerminateTurnAvailable(message.getTerminateTurnAvailable());
                DataBase.setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
                DataBase.setActiveInput(true);
            }
            else if (DataBase.getStartingPosition() != null)
                DataBase.setStartingPosition(null);

            View.setRefresh(true);
            View.print();
        }
    }

    @Override
    public String computeView() {
        Position startingPosition = DataBase.getStartingPosition();

        if (DataBase.getPlayerState() != PlayerState.ACTIVE)
            return DataBase.getMatchPlayers().get(DataBase.getPlayer()) + " is doing his turn";

        if (startingPosition==null && checkSpecialFunctionAvailable())
            return "Choose your starting worker OR type 'f' to use your special function: ";
        else if (startingPosition==null)
            return "Choose your starting worker, insert its coordinates: ";
        StringBuilder string = new StringBuilder();
        if (DataBase.getTurnState() == TurnState.MOVE)
            string.append("Insert the position you want to move to");
        else if (DataBase.getTurnState() == TurnState.BUILD)
            string.append("Insert the position you want to build in");
        if (checkSpecialFunctionAvailable())
            string.append(" OR type 'f' to use your special function");
        if (DataBase.isTerminateTurnAvailable())
            string.append(" OR type 'e' to terminate your turn");
        string.append(": ");
        return string.toString();
        /*else if (checkSpecialFunctionAvailable() && player.isTerminateTurnAvailable()) {
            if (player.getTurnState() == TurnState.MOVE)
                return "Insert the position you want to move to OR type 'f' to use your special function OR type 'e' to terminate your turn: ";
            else //if (player.getTurnState() == TurnState.BUILD)
                return "Insert the position you want to build in OR type 'f' to use your special function OR type 'e' to terminate your turn: ";
        } else if (checkSpecialFunctionAvailable()) {
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
        }*/
    }

    @Override
    public void error() {
        DataBase.setActiveInput(true);
    }

    private boolean processingChar(char input) {
        message = new MessageEvent();
        int num = Character.getNumericValue(input);

        if (num == 15) {
            if (checkSpecialFunctionAvailable()) {
                    message.setSpecialFunction(true);
                    return true;
                } else System.out.println("SPECIAL FUNCTION IS NOT AVAILABLE!");
        }
        else if (num == 14) {

            if (DataBase.isTerminateTurnAvailable()) {
                message.setEndTurn(true);
                return true;
            } else System.out.println("YOU CAN'T TERMINATE THE TURN!");
        }
        else System.out.println("INPUT INCORRECT!");
        return false;
    }

    private boolean processingPosition(int x, int y) {
        Position startingPosition = DataBase.getStartingPosition();
        Position position = new Position(x,y);
        message = new MessageEvent();

        if (startingPosition == null) {
            if (DataBase.isWorkerPresent(position) && DataBase.getWorkersAvailableCells(position).size() > 0 ){
                DataBase.setStartingPosition(position);
                if (DataBase.getTurnState() == TurnState.IDLE)
                    DataBase.setTurnState(TurnState.MOVE);
                View.doUpdate();
                System.out.println(computeView());
                DataBase.setActiveInput(true);
                return true;
            }
            else System.out.println("WORKER IS NOT AVAILABLE!");
        }
        else if (DataBase.getWorkersAvailableCells(startingPosition).contains(position)) {
            message.setStartPosition(startingPosition);
            message.setEndPosition(position);
            DataBase.setMessageReady(true);
            if (DataBase.getTurnState() == TurnState.MOVE)
                DataBase.setStartingPosition(position);
            DataBase.setPlayerState(PlayerState.IDLE);
            return true;
        }
        else System.out.println("POSITION IS NOT AVAILABLE!");
        return false;
    }

    boolean checkSpecialFunctionAvailable() {
        if (DataBase.getSpecialFunctionAvailable() != null) {
            Optional<Boolean> isTrue = DataBase.getSpecialFunctionAvailable().values().stream().filter(bool -> bool != null && bool).findAny();
            return isTrue.isPresent();
        }
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