package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.utilities.*;

import java.util.Optional;
/*
    TODO -> MOSTRA POTERI CHE VENGONO PRESI DAGLI USER
 */
public class Running extends ControlState {

    //TODO CHIUSURA CLIENT NEL CASO DI PARTITA FINITA


    DataBase dataBase = View.getDataBase();

    MessageEvent message = new MessageEvent();

    @Override
    public MessageEvent computeInput(String input) {

        if (input.length() == 1 && processingChar(input.charAt(0))) {
            Controller.setMessageReady(true);
            dataBase.setPlayerState(PlayerState.IDLE);
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
        Controller.setActiveInput(true);
        return null;
    }

    @Override
    public void updateData(MessageEvent message) {

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            dataBase.setControlState(new NotInitialized());
            dataBase.setPlayerState(null);
            Controller.setActiveInput(true);
            View.setRefresh(true);
            View.print();
            return;
        }

        if(!message.getInfo().equals("Match data update")){
            System.out.println(message.getInfo());
        }

        if (message.getMatchState() == MatchState.FINISHED) {
            /*if (message.getPlayerState()==PlayerState.WIN)
                System.out.println("Congratulations! You are the winner!");
            else System.out.println("Unlucky! You lost!");*/
            Controller.setActiveInput(true);
            dataBase.setControlState(new NotInitialized());
            View.setRefresh(true);
            View.print();
        }
        else if (message.getPlayerState() == PlayerState.LOST) {
            //System.out.println("Unlucky! You lost!");
            Controller.setActiveInput(true);
            dataBase.setControlState(new NotInitialized());
            View.setRefresh(true);
            View.print();
        }
        else {
            View.doUpdate();
            /*player.setMatchState(message.getMatchState());
            player.setPlayerState(message.getPlayerState());
            player.setTurnState(message.getTurnState());
            player.setMatchPlayers(message.getMatchPlayers());
            player.setCurrentPlayer(message.getCurrentPlayer());*/

            //Controller.updateStandardData(message);

            dataBase.setBillboardStatus(message.getBillboardStatus());

            if (message.getPlayerState() == PlayerState.ACTIVE) {
                dataBase.setWorkersAvailableCells(message.getWorkersAvailableCells());
                dataBase.setTerminateTurnAvailable(message.getTerminateTurnAvailable());
                dataBase.setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
                Controller.setActiveInput(true);
            }
            else if (dataBase.getStartingPosition()!=null)
                dataBase.setStartingPosition(null);


            View.setRefresh(true);
            View.print();
        }
    }

    @Override
    public String computeView() {
        Position startingPosition = dataBase.getStartingPosition();

        if (dataBase.getPlayerState()!= PlayerState.ACTIVE)
            return dataBase.getMatchPlayers().get(dataBase.getPlayer())+" is doing his turn";

        if (startingPosition==null && checkSpecialFunctionAvailable())
            return "Choose your starting worker OR type 'f' to use your special function: ";
        else if (startingPosition==null)
            return "Choose your starting worker, insert its coordinates: ";
        StringBuilder string = new StringBuilder();
        if (dataBase.getTurnState()==TurnState.MOVE)
            string.append("Insert the position you want to move to");
        else if (dataBase.getTurnState()==TurnState.BUILD)
            string.append("Insert the position you want to build in");
        if (checkSpecialFunctionAvailable())
            string.append(" OR type 'f' to use your special function");
        if (dataBase.isTerminateTurnAvailable())
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
        Controller.setActiveInput(true);
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

            if (dataBase.isTerminateTurnAvailable()) {
                message.setEndTurn(true);
                return true;
            } else System.out.println("YOU CAN'T TERMINATE THE TURN!");
        }
        else System.out.println("INPUT INCORRECT!");
        return false;
    }

    private boolean processingPosition(int x, int y) {
        Position startingPosition = dataBase.getStartingPosition();
        Position position = new Position(x,y);
        message = new MessageEvent();

        if (startingPosition == null) {
            if (dataBase.isWorkerPresent(position) && dataBase.getWorkersAvailableCells(position).size() > 0 ){
                dataBase.setStartingPosition(position);
                if (dataBase.getTurnState()==TurnState.IDLE)
                    dataBase.setTurnState(TurnState.MOVE);
                View.doUpdate();
                System.out.println(computeView());
                Controller.setActiveInput(true);
                return true;
            }
            else System.out.println("WORKER IS NOT AVAILABLE!");
        }
        else if (dataBase.getWorkersAvailableCells(startingPosition).contains(position)) {
            message.setStartPosition(startingPosition);
            message.setEndPosition(position);
            Controller.setMessageReady(true);
            if (dataBase.getTurnState()==TurnState.MOVE)
                dataBase.setStartingPosition(position);
            dataBase.setPlayerState(PlayerState.IDLE);
            return true;
        }
        else System.out.println("POSITION IS NOT AVAILABLE!");
        return false;
    }

    boolean checkSpecialFunctionAvailable() {
        if (dataBase.getSpecialFunctionAvailable()!=null) {
            Optional<Boolean> isTrue = dataBase.getSpecialFunctionAvailable().values().stream().filter(bool -> bool != null && bool).findAny();
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