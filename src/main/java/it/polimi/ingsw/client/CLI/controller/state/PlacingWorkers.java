package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.client.CLI.view.DataBase;
import it.polimi.ingsw.client.CLI.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author giusti-leo
 *
 * PlacingWorkers is a state of the Controller and it handles the placement of Workers in the GameBoard
 *
 */

public class PlacingWorkers extends ControlState {

    MessageEvent messageEvent = new MessageEvent();
    Position position;
    Set <Position> initializedPosition = new HashSet<>();

    /**
     * Analyzes the input String
     * If all coordinates are correct and the position selected is contained in PlacingAvailable Cells, method inserts input in DataBase.
     * If users select 2 correct position, it prepares the message to send
     * If the position is incorrect, it prints error announcement
     *
     * @param input  is Controller String input
     * @return true if the Placing State is done, else false
     */
    @Override
    public MessageEvent computeInput(String input) {
        if(checkMessage(input)){
            Set<Position> placingPosition= DataBase.getPlacingAvailableCells();

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
                        if(DataBase.getGodCard() != null && DataBase.getGodCard().toUpperCase().equals("EROS"))
                            handleErosCase(position);

                        //AGGIUNTA WORKER ALLA BILLBOARD
                        DataBase
                                .getBillboardStatus()
                                .get(position)
                                .setPlayerID(DataBase
                                                .getMatchPlayers()
                                                .keySet()
                                                .stream()
                                                .filter(num -> DataBase.getMatchPlayers().get(num).equals(DataBase.getNickname()))
                                                .findAny().get());

                        View.setRefresh(true);
                        DataBase.setMessageReady(false);
                        DataBase.setActiveInput(true);
                        View.handler();
                        return null;
                    }
                    if (initializedPosition.size() == 2) {
                        messageEvent.setInitializedPositions(initializedPosition);
                        View.setRefresh(true);
                        DataBase.setMessageReady(true);
                        //View.handler();
                        return messageEvent;
                    }
                }
            }
        }
        View.setError(true);
        DataBase.setMessageReady(false);
        DataBase.setActiveInput(true);
        View.handler();
        return null;
    }

    /**
     * It contains Disconnection case.
     * If PlayerState is equals to Active, it saves on DataBase BillBoard status and Placing Available Cells List
     * for the placing State.
     * Then, it updates the GameBoard's visualization and launches computeView method
     *
     * @param message  is the Network Handler 's message
     */
    @Override
    public void updateData(MessageEvent message) {

        if (message.getInfo()!=null && message.getInfo().equals("A user has disconnected from the match. Closing...")) {
            DataBase.setDisconnectedUser(true);
            DataBase.resetDataBase();
            View.setRefresh(true);
            View.handler();
            DataBase.setDisconnectedUser(false);
            return;
        }

        if(DataBase.getMatchState() == MatchState.PLACING_WORKERS){
            if(message.getBillboardStatus() != DataBase.getBillboardStatus() && message.getBillboardStatus() != null){
                DataBase.setBillboardStatus(message.getBillboardStatus());
            }
            if(message.getAvailablePlacingCells() != DataBase.getPlacingAvailableCells() && message.getAvailablePlacingCells() != null){
                DataBase.setPlacingAvailableCells(message.getAvailablePlacingCells());
            }
        }


        View.setRefresh(true);
        View.handler();
        DataBase.setActiveInput(true);


    }

    /**
     * Depending on the Database's state, it computes different String to print
     *
     * @return  String to print on view
     */
    @Override
    public String computeView() {
        if(DataBase.getPlayerState() == PlayerState.ACTIVE ){
            int number = 2 - initializedPosition.size();
            if(number == 0)
                return "Placing complete";
            else if(number == 1){
                return "Insert " + number + " worker.\nInsert position XY: \n ";
            }
            else if(number == 2){
                return "Insert " + number + " workers.\nInsert position XY:\n";
            }
            else return "INSERIMENTO COMPLETATO";
        }
        else{
            return DataBase.getMatchPlayers().get(DataBase.getPlayer()) +" is placing his workers";
        }
    }

    /**
     * Called if there is an error on the message, it announces that the input is incorrect and it prints the computeView method
     */
    @Override
    public String error() {
        DataBase.setActiveInput(true);
        return ("Position is not available or incorrect\n") + computeView();
    }

    /**
     * Checks if the method is correct
     *
     * @param viewObject  is the input from the Controller
     * @return  true if input is correct, else false
     */
    private boolean checkMessage(String viewObject) {
        if (viewObject.length() != 2) {
            System.out.println("INPUT INCORRECT");
            return false;
        }
        else return true;
    }


    /**
     * Method handles the placing action of the God Eros.
     * The cells that are available for the second worker are just the cells that are along the opposite side of the GameBoard
     *
     * In case of corner, available placing cells contains the cells of 2 opposite side
     *
     * @param position  the position of your first worker placed
     */
    private void handleErosCase(Position position){
        Set<Position> positionSet = new HashSet<>();

        if( position.getX() == 0 || position.getX() == 4){
            for (int i = 0; i < 5; i++) positionSet.add(new Position((position.getX()+4)%8, i));
        }
        if(position.getY() == 0 || position.getY() == 4){
            for (int i = 0; i < 5; i++) positionSet.add(new Position(i, (position.getY()+4)%8));
        }

        positionSet = positionSet.stream().filter(pos-> DataBase.getPlacingAvailableCells().contains(pos)).collect(Collectors.toSet());
        DataBase.setPlacingAvailableCells(positionSet);
        }

}