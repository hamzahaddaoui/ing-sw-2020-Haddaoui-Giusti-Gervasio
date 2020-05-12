package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
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

    private static GameBoard gameBoard;
    private static Player player;

    public View(){
        refresh = true;
        error = false;
        player = new Player();
        gameBoard = new GameBoard();
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        View.active = active;
    }

    //UPDATE FROM NETWORK HANDLER

    @Override //FROM CLIENT HANDLER
    public void update(MessageEvent messageEvent){
        System.out.println(messageEvent);
        executorData.submit(()->{
            Controller.updateStandardData(messageEvent);
            Controller.updateControllerState();
        });
        if(messageEvent.getError()){
            executorData.submit(()-> player.getControlState().error());
        }
        else {
            executorData.submit(()-> player.getControlState().updateData(messageEvent));
        }

    }

    public static void print(){
        if(refresh){
            System.out.println(player.getControlState().computeView());
            refresh = false;
        }
        if(error){
            System.out.println("Wrong output");
            System.out.println(player.getControlState().computeView());
            error = false;
        }
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setGameBoard(GameBoard newGameBoard){
        gameBoard = newGameBoard;
    }

    public static void setPlayer(Player newPlayer){
        player = newPlayer;
    }

    //VIEW
    public static void doUpdate(){
        executorView.submit(View::visualization);
    }

    public static void visualization(){
        if(player.getMatchState() == MatchState.PLACING_WORKERS ){
            getBillboardStat();
        }
        else if(player.getMatchState() == MatchState.RUNNING && gameBoard.getStartingPosition() != null){
            getBillboardStat(gameBoard.getWorkersAvailableCells(gameBoard.getStartingPosition()),gameBoard.getStartingPosition());
        }
        else {
            getBillboardStat(gameBoard.getWorkersPositions());
        }
    }

    public static void setRefresh(boolean value){
        refresh = value;
    }

    public static boolean getRefresh(){
        return refresh;
    }

    public static void setError(boolean value){
        error = value;
    }

    public static boolean getError(){
        return error;
    }

    static void getBillboardStat(){
        StringBuilder outputA = new StringBuilder();

        Map<Position, Cell> billboardCells = gameBoard.getBillboardStatus();
        List<Integer> players = new ArrayList<>(player.getMatchPlayers().keySet());


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

        System.out.println(outputA);
    }

    static void getBillboardStat( Set<Position> cells, Position p){
        StringBuilder outputA = new StringBuilder();
        StringBuilder outputB = new StringBuilder();
        StringBuilder outputC = new StringBuilder();
        StringBuilder output = new StringBuilder();

        Map<Position, Cell> billboardCells = gameBoard.getBillboardStatus();
        List<Integer> players = new ArrayList<>(player.getMatchPlayers().keySet());


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

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputB
                        .append(billboardCells.get(position).isDome() ? "⏺" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 ? "0️⃣" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 ? "1️⃣" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 ? "2️⃣" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 ? "3️⃣" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputB.append("\n");


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

        int q, w;
        int j, k;
        int c, v;
        int i;
        for (i = 0, q = 0, j = 0, c = 0, w = outputA.indexOf("\n", 0), k = outputB.indexOf("\n", 0), v = outputC.indexOf("\n", 0);
             i < 5;
             i++, w = outputA.indexOf("\n", q), k = outputB.indexOf("\n", j), v = outputC.indexOf("\n", c)) {

            output.append(outputA, q, w);
            output.append("\t");
            output.append(outputB, j, k);
            output.append("\t");
            output.append(outputC, c, v);
            output.append("\n");
            q = ++ w;
            j = ++ k;
            c = ++ v;
        }
        System.out.println(output.toString());
    }

    static void getBillboardStat( Set<Position> cells){
        StringBuilder outputA = new StringBuilder();
        StringBuilder outputB = new StringBuilder();
        StringBuilder outputC = new StringBuilder();
        StringBuilder output = new StringBuilder();

        Map<Position, Cell> billboardCells = gameBoard.getBillboardStatus();
        List<Integer> players = new ArrayList<>(player.getMatchPlayers().keySet());


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

        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputB
                        .append(billboardCells.get(position).isDome() ? "⏺" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 0 ? "0️⃣" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 1 ? "1️⃣" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 2 ? "2️⃣" : "")
                        .append(! billboardCells.get(position).isDome() && billboardCells.get(position).getTowerHeight() == 3 ? "3️⃣" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputB.append("\n");


        billboardCells
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputC
                        .append(cells.contains(position) ? "\u2B1B" : "")
                        .append(! cells.contains(position) ? "\u2B1C" : "")
                        .append((position.getY() == 4) ? "\n" : " "));

        outputC.append("\n");

        int q, w;
        int j, k;
        int c, v;
        int i;
        for (i = 0, q = 0, j = 0, c = 0, w = outputA.indexOf("\n", 0), k = outputB.indexOf("\n", 0), v = outputC.indexOf("\n", 0);
             i < 5;
             i++, w = outputA.indexOf("\n", q), k = outputB.indexOf("\n", j), v = outputC.indexOf("\n", c)) {

            output.append(outputA, q, w);
            output.append("\t");
            output.append(outputB, j, k);
            output.append("\t");
            output.append(outputC, c, v);
            output.append("\n");
            q = ++ w;
            j = ++ k;
            c = ++ v;
        }
        System.out.println(output.toString());
    }


    /*public void updateData(MessageEvent messageEvent){
        init();
        fetching(messageEvent);
        doUpdate();
    }*/

    // UPDATE OF USER VIEW

    //FETCHING

    /*public void fetching(MessageEvent messageEvent){
        standardFetching(messageEvent);
        if(player.getPlayerState() == PlayerState.WIN || player.getPlayerState() == PlayerState.LOST){
            Client.close();
        }
        switch(player.getMatchState()){
            case SELECTING_GOD_CARDS:{
                fetchingAndInitCardsStates(messageEvent);}
            case SELECTING_SPECIAL_COMMAND: {
                fetchingAndInitCardsStates(messageEvent);
                break;
            }
            case PLACING_WORKERS:{
                fetchingPlacingState(messageEvent);
                break;
            }
            case RUNNING: {
                fetchingRunning(messageEvent);
                if(player.getTurnState() == TurnState.IDLE)
                    System.out.println("\nCHOOSE YOUR STARTING WORKER\nINSERT 'XY' COORDINATES\n");
                initRunning();
                break;
            }
            case FINISHED:
                //active = false;
                Client.close();
                break;
        }
    }*/
/*
    public void standardFetching(MessageEvent messageEvent){
        if(messageEvent.getMatchState() != player.getMatchState() && messageEvent.getMatchState() != null){
            player.setMatchState(messageEvent.getMatchState());
        }
        if(messageEvent.getPlayerState() != player.getPlayerState() && messageEvent.getPlayerState() != null){
            player.setPlayerState(messageEvent.getPlayerState());
        }
        if(messageEvent.getTurnState() != player.getTurnState() && messageEvent.getTurnState() != null){
            player.setTurnState(messageEvent.getTurnState());
        }
        if(messageEvent.getMatchPlayers() != player.getMatchPlayers() && messageEvent.getMatchPlayers() != null){
            player.setMatchPlayers(messageEvent.getMatchPlayers());
        }
        if(messageEvent.getCurrentPlayer() != player.getPlayer()){
            player.setPlayer(messageEvent.getCurrentPlayer());
        }
    }

    public void fetchingAndInitCardsStates(MessageEvent messageEvent){
        if( messageEvent.getMatchCards() != null){
            if(player.getMatchState() == MatchState.SELECTING_GOD_CARDS) {
                gameBoard.setMatchCards(messageEvent.getMatchCards());
            }
            else{
                gameBoard.setSelectedGodCards(messageEvent.getMatchCards());
            }
        }
    }

    public void fetchingPlacingState(MessageEvent messageEvent){
        if(messageEvent.getBillboardStatus() != gameBoard.getBillboardStatus() && messageEvent.getBillboardStatus() != null){
            gameBoard.setBillboardStatus(messageEvent.getBillboardStatus());
        }
        if(messageEvent.getAvailablePlacingCells() != gameBoard.getPlacingAvailableCells() && messageEvent.getAvailablePlacingCells() != null){
            gameBoard.setPlacingAvailableCells(messageEvent.getAvailablePlacingCells());
        }
    }

    public void fetchingRunning(MessageEvent messageEvent){
        if(messageEvent.getBillboardStatus() != gameBoard.getBillboardStatus() && messageEvent.getBillboardStatus() != null){
            gameBoard.setBillboardStatus(messageEvent.getBillboardStatus());
        }
        if(messageEvent.getWorkersAvailableCells() != gameBoard.getWorkersAvailableCells() && messageEvent.getWorkersAvailableCells()!=  null){
            gameBoard.setWorkersAvailableCells(messageEvent.getWorkersAvailableCells());
        }
        if(messageEvent.getTerminateTurnAvailable() != player.isTerminateTurnAvailable()){
            player.setTerminateTurnAvailable(messageEvent.getTerminateTurnAvailable());
        }
        if(messageEvent.getSpecialFunctionAvailable() != player.getSpecialFunctionAvailable() && messageEvent.getSpecialFunctionAvailable() != null){
            player.setSpecialFunctionAvailable(messageEvent.getSpecialFunctionAvailable());
        }
    }

    public static void initRunning(){
        if (player.getPlayerState() != PlayerState.ACTIVE)
            gameBoard.setStartingPosition(null);
        if(player.getTurnState() == TurnState.MOVE)
            System.out.println("\nWHERE YOU WANT TO MOVE?\nINSERT 'XY' COORDINATES\n");
        else if(player.getTurnState() == TurnState.BUILD)
            System.out.println("\nWHERE YOU WANT TO BUILD IN?\nINSERT 'XY' COORDINATES\n");
        if (gameBoard.getStartingPosition() != null) {
            if (player.getSpecialFunctionAvailable().get(gameBoard.getStartingPosition()))
                System.out.println("ENTER F TO USE SPECIAL FUNCTION\n");
        }
        if(player.isTerminateTurnAvailable()){
            System.out.println("ENTER E TO USE TERMINATE TURN\n");
        }
    }*/


}
