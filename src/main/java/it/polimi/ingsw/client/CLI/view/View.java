package it.polimi.ingsw.client.CLI.view;

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
    public synchronized void update(MessageEvent messageEvent) {
            executorData.submit(() -> {
                synchronized (DataBase.class) {
                    DataBase.updateStandardData(messageEvent);
                    DataBase.updateControllerState();
                    if (messageEvent.getError()) {
                        System.out.println(DataBase.getControlState().error());
                    } else {
                        DataBase.getControlState().updateData(messageEvent);
                    }
                    notifyAll();
                }
            });
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
            if (DataBase.getPlayerState()==PlayerState.ACTIVE)
                placingWorkerSituationActive();
            else
                System.out.println(placingWorkerSituation());
        }
        else if(DataBase.getMatchState() == MatchState.RUNNING && DataBase.getStartingPosition() != null && DataBase.getPlayerState() == PlayerState.ACTIVE){ // visualizzaione delle 3 tabelle
            gameBoardVisualizationActive();
        }
        else if(DataBase.getMatchState() == MatchState.RUNNING && DataBase.getStartingPosition() == null && DataBase.getPlayerState() == PlayerState.ACTIVE){
            gameBoardVisualizationChooseCurrentWorker();
        }
        else {
            System.out.println(getBillboardPlayersAndHeights());
        }
    }

    /**
     * Prints the GameBoard situation with colored blocks during the placing phase.
     *
     * @return  the string of the GameBoard's situation
     */
    static String placingWorkerSituation(){
        StringBuilder outputA = new StringBuilder();

        Map<Position, Cell> billboardCells = DataBase.getBillboardStatus();
        Map<Integer,String> playerColors = DataBase.getMatchColors();

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_WHITE = "\u001B[37m";

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputA
                        .append(billboardCells.get(position).getPlayerID() == 0 ? ANSI_WHITE +  "⬜" + ANSI_RESET : "") //  ⬜️
                        .append(billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Blue") ? ANSI_BLUE + "🟦" + ANSI_RESET : "")
                        .append(billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Orange") ? ANSI_RED + "🟧" + ANSI_RESET : "") //🟩
                        .append(billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Purple") ? ANSI_PURPLE + "🟪" + ANSI_RESET : "") //🟦
                        .append((position.getY() == 4) ? "\n" : " "));

        outputA.append("\n");
        outputA.append(keyLegend());
        return outputA.toString();
    }

    static String getPlacingAvailableCells(Set<Position> cells) {
        StringBuilder outputB = new StringBuilder();

        Map<Position,Cell> billboardCells = DataBase.getBillboardStatus();

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputB
                        .append(cells.contains(position) ? "\u2B1B" : "" )
                        .append(!cells.contains(position) ? "\u2B1C" : "")
                        .append((position.getY() == 4) ? "\n" : " "));
        outputB.append("\n");
        return outputB.toString();
    }

    static void placingWorkerSituationActive() {
        StringBuilder output = new StringBuilder();
        String billboardStat = placingWorkerSituation();
        String availableCells = getPlacingAvailableCells(DataBase.getPlacingAvailableCells());

        int q, w;
        int j, k;
        int i;
        for (i = 0, q = 0, j = 0, w = billboardStat.indexOf("\n", 0), k = availableCells.indexOf("\n", 0);
             i < 5;
             i++, w = billboardStat.indexOf("\n", q), k = availableCells.indexOf("\n", j)) {

            output.append(billboardStat, q, w);
            output.append("\t\t\t");
            output.append(availableCells, j, k);
            output.append("\n");
            q = ++ w;
            j = ++ k;
        }
        output.append(keyLegend());
        System.out.println(output.toString());
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

    /**
     * Method that shows the situation of the cells with the player in it and the tower height.
     *
     * @return the string of the GameBoard's situation
     */
    static String getBillboardPlayersAndHeights() {
        StringBuilder output = new StringBuilder();

        Map<Position, Cell> billboardCells = DataBase.getBillboardStatus();
        Map<Integer,String> playerColors = DataBase.getMatchColors();

       final String ANSI_RESET = "\u001B[0m";
       final String ANSI_BLUE = "\u001B[34m";
       final String ANSI_PURPLE = "\u001B[35m";
       final String ANSI_RED = "\u001B[31m";
       final String ANSI_WHITE = "\u001B[37m";

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> output
                        .append(billboardCells.get(position).isDome() && billboardCells.get(position).getPlayerID() == 0 ? ANSI_WHITE + "⬜" + ANSI_RESET + "⏺ " : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 && billboardCells.get(position).getPlayerID() == 0 ? ANSI_WHITE + "⬜" + ANSI_RESET + "0️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Blue") ? ANSI_BLUE + "🟦" + ANSI_RESET + "0️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Orange") ? ANSI_RED + "🟧" + ANSI_RESET + "0️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Purple") ? ANSI_PURPLE + "🟪" + ANSI_RESET + "0️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 && billboardCells.get(position).getPlayerID() == 0 ? ANSI_WHITE + "⬜" + ANSI_RESET + "1️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Blue") ? ANSI_BLUE + "🟦" + ANSI_RESET + "1️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Orange") ? ANSI_RED + "🟧" + ANSI_RESET + "1️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Purple") ? ANSI_PURPLE + "🟪" + ANSI_RESET + "1️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 && billboardCells.get(position).getPlayerID() == 0 ? ANSI_WHITE + "⬜" + ANSI_RESET + "2️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Blue") ? ANSI_BLUE + "🟦" + ANSI_RESET + "2️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Orange") ? ANSI_RED + "🟧" + ANSI_RESET + "2️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Purple") ? ANSI_PURPLE + "🟪" + ANSI_RESET + "2️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 && billboardCells.get(position).getPlayerID() == 0 ? ANSI_WHITE + "⬜" + ANSI_RESET + "3️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Blue") ? ANSI_BLUE + "🟦" + ANSI_RESET + "3️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Orange") ? ANSI_RED + "🟧" + ANSI_RESET + "3️⃣" : "")
                        .append(!billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 && billboardCells.get(position).getPlayerID() != 0 && playerColors.get(billboardCells.get(position).getPlayerID()).equals("Purple") ? ANSI_PURPLE + "🟪" + ANSI_RESET + "3️⃣" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        output.append("\n");
        output.append(keyLegend());
        return output.toString();
    }

    /**
     * Method that organizes the the visualization of the tables if the worker is active and it can do his movement
     *
     */
    public static synchronized void gameBoardVisualizationActive(){
        StringBuilder output = new StringBuilder();
        String billboardStat2 = getBillboardPlayersAndHeights();
        String availableMovements = getBillboardStat(DataBase.getWorkersAvailableCells(DataBase.getStartingPosition()),DataBase.getStartingPosition());

        int q, w;
        int j, k;
        int i;
        for (i = 0, q = 0, j = 0, w = billboardStat2.indexOf("\n", 0), k = availableMovements.indexOf("\n", 0);
             i < 5;
             i++, w = billboardStat2.indexOf("\n", q), k = availableMovements.indexOf("\n", j)) {

            output.append(billboardStat2, q, w);
            output.append("\t\t\t");
            output.append(availableMovements, j, k);
            output.append("\n");
            q = ++ w;
            j = ++ k;
        }
        output.append(keyLegend());
        System.out.println(output.toString());
    }

    /**
     * Method that organize the the visualization of the tables if the worker is active and it has to choose the worker for the turn
     *
     */
    public static synchronized void gameBoardVisualizationChooseCurrentWorker(){
        StringBuilder output = new StringBuilder();
        String billboardStat2 = getBillboardPlayersAndHeights();
        String availableMovements = getBillBoardEvidence(DataBase.getWorkersPositions());

        int q, w;
        int j, k;
        int i;
        for (i = 0, q = 0, j = 0, w = billboardStat2.indexOf("\n", 0), k = availableMovements.indexOf("\n", 0);
             i < 5;
             i++, w = billboardStat2.indexOf("\n", q), k = availableMovements.indexOf("\n", j)) {

            output.append(billboardStat2, q, w);
            output.append("\t\t\t");
            output.append(availableMovements, j, k);
            output.append("\n");
            q = ++ w;
            j = ++ k;
        }
        output.append(keyLegend());
        System.out.println(output.toString());
    }

    /**
     * Method that shows the key legends with the players of the match.
     * <p>
     * Every player is associated with a color.
     *
     * @return the string of players, each name with a different color
     */
    public static synchronized String keyLegend() {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_RED = "\u001B[31m";

        Map<Integer,String> playersName = DataBase.getMatchPlayers();
        Map<Integer,String> playersColor = DataBase.getMatchColors();
        List<Integer> playersList = new ArrayList<>(playersColor.keySet());
        StringBuilder keyLegend = new StringBuilder();

        keyLegend.append("KEY: ");

        for (int i=0;i<playersName.size();i++) {
            if (playersName.containsKey(playersList.get(i))) {
                if (playersColor.get(playersList.get(i)).equals("Blue"))
                    keyLegend.append(ANSI_BLUE + playersName.get(playersList.get(i)) + ANSI_RESET + ", ");
                else if (playersColor.get(playersList.get(i)).equals("Orange"))
                    keyLegend.append(ANSI_RED + playersName.get(playersList.get(i)) + ANSI_RESET + ", ");
                else
                    keyLegend.append(ANSI_PURPLE + playersName.get(playersList.get(i)) + ANSI_RESET + ", ");
            }
        }
        keyLegend.deleteCharAt(keyLegend.length()-2);
        return keyLegend.toString();
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
            System.out.println(DataBase.getControlState().error());
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
