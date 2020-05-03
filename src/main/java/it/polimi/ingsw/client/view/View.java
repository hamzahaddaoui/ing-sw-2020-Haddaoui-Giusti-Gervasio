package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class View extends Observable<String> implements Observer<MessageEvent> {

    static ExecutorService executorUpdate = Executors.newSingleThreadExecutor();
    static ExecutorService executorInput = Executors.newSingleThreadExecutor();
    static ExecutorService executorData = Executors.newSingleThreadExecutor();

    private Scanner scanner;
    private PrintStream outputStream;
    private static boolean active = false;

    private static GameBoard gameBoard;
    private static Player player;

    public View(){
        active = false;
        player = new Player();
        gameBoard = new GameBoard();
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
    }

    //UPDATE FROM NETWORK HANDLER

    @Override
    public void update(MessageEvent messageEvent) {
        executorData.submit(()-> updateData(messageEvent) );
    }

    public void updateData(MessageEvent messageEvent){
        
        if(messageEvent.getInfo() != null && messageEvent.getInfo().equals("Nickname not available.")){
            init();
        }
        else{
            fetching(messageEvent);
            if (!active && !messageEvent.getError()) {
                active = true;
                executorInput.submit(this::inputListener);
            } else {
                if (messageEvent.getError())
                    outputStream.println("Last Input was illegal.");
            }
        }
        doUpdate();
    }

    // UPDATE OF USER VIEW

    public static void doUpdate(){
        executorUpdate.submit(()-> view());
    }

    public static void view(){
        // mostra la visione a schermo a seconda del differente stato del Match o del player
    }

    //INPUT CHARACTER

    public void inputListener(){
            String input;
            while(active){
                input = scanner.nextLine();
                notify(input);
            }
            scanner.close();
            outputStream.println("\nTHE MATCH IS FINISHED.\n");
    }

    //FETCHING

    public void fetching(MessageEvent messageEvent){
        standardFetching(messageEvent);
        if(player.getPlayerState() == PlayerState.WIN || player.getPlayerState() == PlayerState.LOST){
            active = false;
            outputStream.println("\nYOU "+ player.getPlayerState()+"!\n");
            Client.close();
        }
        switch(player.getMatchState()){
            case NONE:{
                outputStream.println("\nWAIT FOR YOUR TURN...\n");
                break;
            }
            case GETTING_PLAYERS_NUM:{
                outputStream.println("\nINSERT PLAYER:\n");
                break;
            }
            case WAITING_FOR_PLAYERS:{
                outputStream.println("\nWAIT FOR PLAYERS...\n");
                break;
            }
            case SELECTING_GOD_CARDS:{
                fetchingAndInitCardsStates(messageEvent);
                outputStream.println("\nSELECT GOD CARDS FOR THE MATCH\n");
                break;
            }
            case SELECTING_SPECIAL_COMMAND: {
                outputStream.println("\nSELECT YOUR GOD CARD\n");
                fetchingAndInitCardsStates(messageEvent);
                break;
            }
            case PLACING_WORKERS:{
                fetchingPlacingState(messageEvent);
                outputStream.println("\nPLACE YOUR TWO WORKERS \n");
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
                active = false;
                outputStream.println("\nGAME OVER\n");
                Client.close();
                break;
        }
    }

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

    //INIT

    public void init(){
        if(player.getNickname() == null){
            new View();
            outputStream.println( "INSERT YOUR NICKNAME:\n ");
            player.setNickname(scanner.nextLine());
        }
        else{
            outputStream.println( "YOUR NICKNAME IS NOT AVAILABLE!\nINSERT A NEW NICKNAME:\n   ");
            player.setNickname(scanner.nextLine());
        }
        notify(player.getNickname());
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
    }

    // GETTER

    public static boolean isActive() { return active;}

    public static GameBoard getGameBoard() {
        return gameBoard;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setActive(boolean value){
        active = value;
    }

}
