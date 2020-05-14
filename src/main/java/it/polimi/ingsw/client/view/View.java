package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class View extends Observable<String> implements Observer<MessageEvent> {

    static ExecutorService executorView = Executors.newSingleThreadExecutor();
    static ExecutorService executorData = Executors.newSingleThreadExecutor();

    private static boolean refresh = true;
    private static boolean error;
    private static boolean active;

    //private static GameBoard gameBoard;
    //private static Player player;
    private static DataBase dataBase;

    public View(){
        refresh = true;
        error = false;
        dataBase = new DataBase();
        //player = new Player();
        //gameBoard = new GameBoard();
    }

    //UPDATE FROM NETWORK HANDLER

    @Override //FROM CLIENT HANDLER
    public synchronized void update(MessageEvent messageEvent){
        //System.out.println(messageEvent);
        executorData.submit(()->{
            synchronized (DataBase.class){
                Controller.updateStandardData(messageEvent);
                Controller.updateControllerState();
                notifyAll();}
        });
        if(messageEvent.getError()){
            executorData.submit(()->{
                synchronized (DataBase.class){
                    dataBase.getControlState().error();
                    notifyAll();
                    }
            });
        }
        else {
            executorData.submit(()->{
                synchronized (DataBase.class){
                    dataBase.getControlState().updateData(messageEvent);
                    notifyAll();
                }
            });
        }
    }

    //VIEW
    public static void doUpdate(){
        executorView.submit(View::visualization);
    }

    public static void visualization(){
        if(dataBase.getMatchState() == MatchState.PLACING_WORKERS ){
            System.out.println(getBillboardStat());
        }
        else if(dataBase.getMatchState() == MatchState.RUNNING && dataBase.getStartingPosition() != null && dataBase.getPlayerState() == PlayerState.ACTIVE){ // visualizzaione delle 3 tabelle
            gameBoardVisualizationActive();
        }
        else if(dataBase.getMatchState() == MatchState.RUNNING && dataBase.getStartingPosition() == null && dataBase.getPlayerState() == PlayerState.ACTIVE){
            gameBoardVisualizationChooseCurrentWorker();
        }
        else {
            gameBoardVisualizationNotActive();
        }
    }

    /**
     * Method that organize the the visualization of the tables if the worker is active and have to choose the worker for the turn
     *
     */
    public static synchronized void gameBoardVisualizationChooseCurrentWorker(){
        StringBuilder output = new StringBuilder();
        String coloredGameBoard = getBillboardStat();
        String heightStateGameBoard = getBillboardHeightStat();
        String availableMovements = getBillBoardEvidence(dataBase.getWorkersPositions());
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
     * Method that organize the the visualization of the tables if the worker is active and can do his movement
     *
     */
    public static synchronized void gameBoardVisualizationActive(){
        StringBuilder output = new StringBuilder();
        String coloredGameBoard = getBillboardStat();
        String heightStateGameBoard = getBillboardHeightStat();
        String availableMovements = getBillboardStat(dataBase.getWorkersAvailableCells(dataBase.getStartingPosition()),dataBase.getStartingPosition());

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
     * Method that organize the the visualization of the tables if the worker is not active and have to choose his current worker
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
     * Print the GameBoard situation with colored blocks
     *
     * @return  the string of the GameBoard's situation
     */
    static String getBillboardStat(){
        StringBuilder outputA = new StringBuilder();

        Map<Position, Cell> billboardCells = dataBase.getBillboardStatus();
        List<Integer> players = new ArrayList<>(dataBase.getMatchPlayers().keySet());


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
     * Print the height of the GameBoard' cells
     *
     * @return  the string of the GameBoard's situation
     */
    static String getBillboardHeightStat() {
        StringBuilder outputB = new StringBuilder();

        Map<Position, Cell> billboardCells = dataBase.getBillboardStatus();

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
     * Put in evidence the position where the current worker can move or build
     *
     * @param cells  the set of available cells where worker can move or build
     * @param p  the actual position of the worker
     * @return  the string of the GameBoard's situation
     */
    static String getBillboardStat( Set<Position> cells, Position p){
        StringBuilder outputC = new StringBuilder();

        Map<Position, Cell> billboardCells = dataBase.getBillboardStatus();

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
     * evidence the cells of the user in the billboard table
     *
     * @param cells  are the worker' positions of the user
     * @return  cells of the user in the billboard table
     */
    static String getBillBoardEvidence( Set<Position> cells){
        StringBuilder outputD = new StringBuilder();

        Map<Position, Cell> billboardCells = dataBase.getBillboardStatus();

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

    public static void print(){
        if(refresh){
            System.out.println(dataBase.getControlState().computeView());
            refresh = false;
        }
        if(error){
            System.out.println("Wrong output");
            System.out.println(dataBase.getControlState().computeView());
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

    public static void setDataBase(DataBase newDataBase){
        dataBase = newDataBase;
    }

    public static DataBase getDataBase() {
        return dataBase;
    }

    /*public static Player getPlayer() {
        return player;
    }

    public static void setGameBoard(GameBoard newGameBoard){
        gameBoard = newGameBoard;
    }

    public static void setPlayer(Player newPlayer){
        player = newPlayer;
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }
     */

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
