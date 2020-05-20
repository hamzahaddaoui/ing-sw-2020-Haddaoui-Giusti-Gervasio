package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author giusti-leo
 *
 * View class handles the receivement of message from the Network Handler, the update of DataBase and of visualization
 * of the GameBoard
 *
 */

/*
    TODO -> MOSTRA POTERI CHE VENGONO PRESI DAGLI USER
 */

public class View extends Observable<String> implements Observer<MessageEvent> {

    static ExecutorService executorView = Executors.newSingleThreadExecutor();
    static ExecutorService executorData = Executors.newSingleThreadExecutor();

    private static boolean refresh = true;
    private static boolean error;
    private static boolean active;

    public View(){
        refresh = true;
        error = false;
    }

    // From NetWorkHandler

    /**
     * Receives a Message Event from Network Handler. It fetches the message and it changes his dates on the DataBase
     *
     * @param messageEvent  is the message from Network Handler
     */
    @Override
    public synchronized void update(MessageEvent messageEvent){
        executorData.submit(()->{
            synchronized (DataBase.class){
                DataBase.updateStandardData(messageEvent);
                DataBase.updateControllerState();
                notifyAll();}
        });
        if(messageEvent.getError()){
            executorData.submit(()->{
                synchronized (DataBase.class){
                    DataBase.getControlState().error();
                    notifyAll();
                    }
            });
        }
        else {
            executorData.submit(()->{
                synchronized (DataBase.class){
                    DataBase.getControlState().updateData(messageEvent);
                    notifyAll();
                }
            });
        }
    }

    // From Controller States (VISUALIZATION CHANGES)

    /**
     * Method that is called from Controller Classes and it submits visualization method that updates the GameBoard state
     */
    public static void doUpdate(){
        executorView.submit(View::visualization);
    }

    /**
     * Depending on the MatchState and PlayerState, it launches various method that create different GameBoard State's visualization
     */
    public static void visualization(){
        if(DataBase.getMatchState() == MatchState.PLACING_WORKERS ){
            System.out.println(getBillboardStat());
        }
        else if(DataBase.getMatchState() == MatchState.RUNNING && DataBase.getStartingPosition() != null && DataBase.getPlayerState() == PlayerState.ACTIVE){ // visualizzaione delle 3 tabelle
            gameBoardVisualizationActive();
        }
        else if(DataBase.getMatchState() == MatchState.RUNNING && DataBase.getStartingPosition() == null && DataBase.getPlayerState() == PlayerState.ACTIVE){
            gameBoardVisualizationChooseCurrentWorker();
        }
        else {
            gameBoardVisualizationNotActive();
        }
    }

    /**
     * Method that organize the the visualization of the tables if the worker is active and it has to choose the worker for the turn
     *
     */
    public static synchronized void gameBoardVisualizationChooseCurrentWorker(){
        StringBuilder output = new StringBuilder();
        String coloredGameBoard = getBillboardStat();
        String heightStateGameBoard = getBillboardHeightStat();
        String availableMovements = getBillBoardEvidence(DataBase.getWorkersPositions());
        int q, w;
        int j, k;
        int c, v;
        int i;
        for (i = 0, q = 0, j = 0, c = 0, w = coloredGameBoard.indexOf("\n", 0), k = heightStateGameBoard.indexOf("\n", 0), v = availableMovements.indexOf("\n", 0);
             i < 5;
             i++, w = coloredGameBoard.indexOf("\n", q), k = heightStateGameBoard.indexOf("\n", j), v = availableMovements.indexOf("\n", c)) {

            output.append(coloredGameBoard, q, w);
            output.append("\t\t\t");
            output.append(heightStateGameBoard, j, k);
            output.append("\t\t\t");
            output.append(availableMovements, c, v);
            output.append("\n");
            q = ++ w;
            j = ++ k;
            c = ++ v;
        }
        System.out.println(output.toString());
    }

    /**
     * Method that organizes the the visualization of the tables if the worker is active and it can do his movement
     *
     */
    public static synchronized void gameBoardVisualizationActive(){
        StringBuilder output = new StringBuilder();
        String coloredGameBoard = getBillboardStat();
        String heightStateGameBoard = getBillboardHeightStat();
        String availableMovements = getBillboardStat(DataBase.getWorkersAvailableCells(DataBase.getStartingPosition()),DataBase.getStartingPosition());

        int q, w;
        int j, k;
        int c, v;
        int i;
        for (i = 0, q = 0, j = 0, c = 0, w = coloredGameBoard.indexOf("\n", 0), k = heightStateGameBoard.indexOf("\n", 0), v = availableMovements.indexOf("\n", 0);
             i < 5;
             i++, w = coloredGameBoard.indexOf("\n", q), k = heightStateGameBoard.indexOf("\n", j), v = availableMovements.indexOf("\n", c)) {

            output.append(coloredGameBoard, q, w);
            output.append("\t\t\t");
            output.append(heightStateGameBoard, j, k);
            output.append("\t\t\t");
            output.append(availableMovements, c, v);
            output.append("\n");
            q = ++ w;
            j = ++ k;
            c = ++ v;
        }
        System.out.println(output.toString());
    }

    /**
     * Method that organizes the the visualization of the tables if the worker is not active and it has to choose his current worker
     */
    public static synchronized void gameBoardVisualizationNotActive(){
        StringBuilder output = new StringBuilder();
        String coloredGameBoard = getBillboardStat();
        String heightStateGameBoard = getBillboardHeightStat();

        int q, w;
        int j, k;
        int i;
        for (i = 0, q = 0, j = 0, w = coloredGameBoard.indexOf("\n", 0), k = heightStateGameBoard.indexOf("\n", 0);
             i < 5;
             i++, w = coloredGameBoard.indexOf("\n", q), k = heightStateGameBoard.indexOf("\n", j)) {

            output.append(coloredGameBoard, q, w);
            output.append("\t\t\t");
            output.append(heightStateGameBoard, j, k);
            output.append("\n");
            q = ++ w;
            j = ++ k;
        }
        System.out.println(output.toString());
    }

    /**
     * Prints the GameBoard situation with colored blocks
     *
     * @return  the string of the GameBoard's situation
     */
    static String getBillboardStat(){
        StringBuilder outputA = new StringBuilder();

        Map<Position, Cell> billboardCells = DataBase.getBillboardStatus();
        List<Integer> players = new ArrayList<>(DataBase.getMatchPlayers().keySet());


        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputA
                        .append(billboardCells.get(position).getPlayerID() == 0 ? "⬜ " : "")
                        .append(players.indexOf(billboardCells.get(position).getPlayerID()) == 0 ? "🟥 " : "")
                        .append(players.indexOf(billboardCells.get(position).getPlayerID()) == 1 ? "🟩 " : "")
                        .append(players.indexOf(billboardCells.get(position).getPlayerID()) == 2 ? "🟦 " : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputA.append("\n");
        return outputA.toString();
    }

    /**
     * Prints the height of the GameBoard' cells
     *
     * @return  the string of the GameBoard's situation
     */
    static String getBillboardHeightStat() {
        StringBuilder outputB = new StringBuilder();

        Map<Position, Cell> billboardCells = DataBase.getBillboardStatus();

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputB
                        .append(billboardCells.get(position).isDome() ? "⏺" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 ? "0️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 ? "1️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 ? "2️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 ? "3️⃣" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputB.append("\n");
        return outputB.toString();
    }

    /**
     * Puts in evidence the position where the current worker can move or build
     *
     * @param cells  the set of available cells where worker can move or build
     * @param p  the actual position of the worker
     * @return  the string of the GameBoard's situation
     */
    static String getBillboardStat( Set<Position> cells, Position p){
        StringBuilder outputC = new StringBuilder();

        Map<Position, Cell> billboardCells = DataBase.getBillboardStatus();

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputC
                        .append(cells.contains(position) ? "\u2B1B" : "")
                        .append(! (p.equals(position)) && ! cells.contains(position) ? "\u2B1C" : "")
                        .append((p.equals(position)) ? "\uD83D\uDC77\uD83C\uDFFB" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputC.append("\n");
        return outputC.toString();
    }

    /**
     * Evidences the cells of the user in the billboard table
     *
     * @param cells  are the worker' positions of the user
     * @return  cells of the user in the billboard table
     */
    static String getBillBoardEvidence( Set<Position> cells){
        StringBuilder outputD = new StringBuilder();

        Map<Position, Cell> billboardCells = DataBase.getBillboardStatus();

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputD
                        .append(cells.contains(position) ? "\u2B1B" : "")
                        .append(! cells.contains(position) ? "\u2B1C" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputD.append("\n");
        return outputD.toString();
    }

    // From Controller (INPUT ERROR / INCORRECT DATA)

    /**
     * Depending on ControllerState , it prints different indication for the correct game
     */
    public static void print(){
        if(refresh){
            System.out.println(DataBase.getControlState().computeView());
            refresh = false;
        }
        if(error){
            System.out.println("Wrong input");
            System.out.println(DataBase.getControlState().computeView());
            error = false;
        }
    }

    // GETTER AND SETTER

    public static boolean getRefresh(){
        return refresh;
    }

    public static void setError(boolean value){
        error = value;
    }

    public static boolean getError(){
        return error;
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        View.active = active;
    }

    public static void setRefresh(boolean value){
        refresh = value;
    }

}
