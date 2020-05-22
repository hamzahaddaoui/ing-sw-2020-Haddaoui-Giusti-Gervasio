package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.DataBase;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.*;

import java.util.Map;
import java.util.Optional;

/**
 * @author Vasio1298
 *
 * Running is a state of the Controller and handles the central part of the game interaction
 *
 */

public class Running extends ControlState {

    MessageEvent message = new MessageEvent();

    /**
     * Analyzes input.
     * If input is longer then 2 is always incorrect.
     * If input length is 2 can be a position and it launches processingPosition method
     * If input length is 1 can be "e" or "f" and it launches processingChar method
     *
     * @param input  is Controller String input
     * @return  true if the message is ready to end, else false
     */
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

    /**
     * It contains Disconnection case.
     * If PlayerState is Lost, it prints info and change state of controller state for the next match
     * Else update the view, it saves the dates from the message
     * If PlayerState is Active sets WorkersAvailableCells, SpecialFunctionAvailable and SpecialFunctionAvailable for the turn
     *
     * @param message  is the Network Handler 's message
     */
    @Override
    public void updateData(MessageEvent message) {

        //CASO DISCONNESSIONE UTENTE
        if (message.getInfo()!=null && message.getInfo().equals("A user has disconnected from the match. Closing...") ) {
            DataBase.resetDataBase();
            View.setRefresh(true);
            View.print();
            return;
        }

        if(message.getInfo()!=null && !message.getInfo().equals("Match data update") ){
            System.out.println(message.getInfo());
        }

        if (DataBase.getPlayerState() == PlayerState.LOST ) {
            DataBase.setControlState(new NotInitialized());
            DataBase.getControlState().updateData(message);
        }
        else {

            DataBase.setBillboardStatus(message.getBillboardStatus());

            if (DataBase.getPlayerState() == PlayerState.ACTIVE) {
                DataBase.setWorkersAvailableCells(message.getWorkersAvailableCells());
                DataBase.setTerminateTurnAvailable(message.getTerminateTurnAvailable());
                DataBase.setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
                DataBase.setActiveInput(true);
            }
            else if (DataBase.getStartingPosition()!=null)
                DataBase.setStartingPosition(null);
            else if (DataBase.isSpecialFunction())
                DataBase.resetSpecialFunction();

            View.doUpdate();
            View.setRefresh(true);
            View.print();
        }
    }

    /**
     * Depending on the Database's state, it computes different String to print
     *
     * @return  String to print on view
     */
    @Override
    public String computeView() {
        Position startingPosition = DataBase.getStartingPosition();

        if (DataBase.getPlayerState()!= PlayerState.ACTIVE)
            return DataBase.getMatchPlayers().get(DataBase.getPlayer())+" is doing his turn";

        if (startingPosition==null && checkSpecialFunctionAvailable()) {
            if (DataBase.isSpecialFunction())
                return "Choose your starting worker OR type 'f' to disable your special function: ";
            else
                return "Choose your starting worker OR type 'f' to enable your special function: ";
        }
        else if (startingPosition==null)
            return "Choose your starting worker, insert its coordinates: ";

        StringBuilder string = new StringBuilder();
        if (DataBase.getTurnState()==TurnState.MOVE)
            string.append("Insert the position you want to move to");
        else if (DataBase.getTurnState()==TurnState.BUILD)
            string.append("Insert the position you want to build in");
        if (checkSpecialFunctionAvailable()) {
            if (DataBase.isSpecialFunction())
                string.append(" OR type 'f' to disable your special function");
            else
                string.append(" OR type 'f' to enable your special function");}
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

    /**
     * Sets Active Input true
     */
    @Override
    public void error() {
        DataBase.setActiveInput(true);
    }

    /**
     * Analyzes the input
     * If input equals to 'e', it checks if DataBase has Special Function Available. If true prepares the message, else not
     * If input equals to 'f', it checks if DataBase has Terminate Turn Available. If true prepares the message, else not
     * If input is different from 'e' or 'q' ,it prints error announcement
     *
     * @param input  is Controller String input
     * @return  true if the message is ready to end, else false
     */
    private boolean processingChar(char input) {
        message = new MessageEvent();
        int num = Character.getNumericValue(input);

        if (num == 15) {
            if (checkSpecialFunctionAvailable()) {
                    System.out.println(DataBase.isSpecialFunction());
                    DataBase.setUnsetSpecialFunction();
                    System.out.println(DataBase.isSpecialFunction());
                    message.setSpecialFunction(DataBase.isSpecialFunction());
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

    /**
     * Checks if the input is correct or not
     * If startingPosition is null, it means that user is selecting the current worker
     * If startingPosition is not null, it means that user is trying to move or build. Therefore, it checks if the position
     * is in the WorkersAvailableCells. If startingPosition and endPosition are inserted, it prepares the message.
     *
     * @param x  is the first coordinate that user has inserted
     * @param y  is the second coordinate that user has inserted
     * @return  true if startingPosition and endPosition are inserted, else false
     */
    private boolean processingPosition(int x, int y) {
        Position startingPosition = DataBase.getStartingPosition();
        Position position = new Position(x,y);
        message = new MessageEvent();

        if (startingPosition == null) {
            if (DataBase.isWorkerPresent(position) && DataBase.getWorkersAvailableCells(position).size() > 0 ){
                DataBase.setStartingPosition(position);
                if (DataBase.getTurnState()==TurnState.IDLE)
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
            if (DataBase.getTurnState()==TurnState.MOVE)
                DataBase.setStartingPosition(position);
            DataBase.setPlayerState(PlayerState.IDLE);
            return true;
        }
        else System.out.println("POSITION IS NOT AVAILABLE!");
        return false;
    }

    /**
     * Checks if the DataBase has Special Function Available
     *
     * @return  true if correct , else false
     */
    boolean checkSpecialFunctionAvailable() {
        Position startingPosition = DataBase.getStartingPosition();
        Map<Position,Boolean> specialFunctionAvailable = DataBase.getSpecialFunctionAvailable();

        if (startingPosition==null) {
            if (specialFunctionAvailable != null) {
                Optional<Boolean> isTrue = DataBase.getSpecialFunctionAvailable().values().stream().filter(bool -> bool != null && bool).findAny();
                return isTrue.isPresent();
            }
        }
        else if (specialFunctionAvailable != null)
            return specialFunctionAvailable.get(startingPosition);
        return false;
    }

}
