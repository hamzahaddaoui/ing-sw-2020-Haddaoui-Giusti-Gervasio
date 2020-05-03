package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.controller.state.InsertCharacter;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class View extends Observable<Object> implements Observer<MessageEvent> {

    static ExecutorService executorUpdate = Executors.newSingleThreadExecutor();
    static ExecutorService executorInput = Executors.newSingleThreadExecutor();
    static ExecutorService executorData = Executors.newSingleThreadExecutor();

    private Scanner scanner;
    private DataInputStream dataInputStream;
    private PrintStream outputStream;
    private char inputCharacter;
    private InsertCharacter insertCharacter;
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
                executorInput.submit(() -> inputListener());
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
        try{
            dataInputStream = new DataInputStream(System.in);
            //scanner.close();
            while(active){
                inputCharacter = dataInputStream.readChar();
                if(commute(inputCharacter))
                    notify(insertCharacter);
                else outputStream.println("Inserimento errato...");
            }
            dataInputStream.close();
        }
        catch(EOFException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        finally {
            try {
                System.out.println("This match is finished. ");
                view();
                Client.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean commute(char inputCharacter){
        if (Set.of(InsertCharacter.values()).stream().anyMatch(insertCharacter1 -> insertCharacter1.getCode()==(int) inputCharacter)) {
            insertCharacter = Set.of(InsertCharacter.values()).stream().filter(insertCharacter1 -> insertCharacter1.getCode()==(int) inputCharacter).findAny().get();
            return true;
        }
        else return false;
    }

    //FETCHING

    public void fetching(MessageEvent messageEvent){
        standardFetching(messageEvent);
        if(player.getPlayerState() == PlayerState.WIN || player.getPlayerState() == PlayerState.LOST){
            active = false;
            outputStream.println("\nYou "+ player.getPlayerState()+"!\n");
            Client.close();
        }
        switch(player.getMatchState()){
            case NONE:{
                outputStream.println("WAIT FOR YOUR TURN...");
                break;
            }
            case GETTING_PLAYERS_NUM:{
                if(player.getPlayersNum().size() == 0)
                    initGettingPlayersNum();
                break;
            }
            case WAITING_FOR_PLAYERS:{
                outputStream.println("WAIT FOR PLAYERS...");
                break;
            }
            case SELECTING_GOD_CARDS:
            case SELECTING_SPECIAL_COMMAND: {
                fetchingAndInitCardsStates(messageEvent);
                break;
            }
            case PLACING_WORKERS:{
                fetchingPlacingState(messageEvent);
                initPlacingState();
                break;
            }
            case RUNNING: {
                fetchingRunning(messageEvent);
                initRunning();
                break;
            }
            case FINISHED:
                active = false;
                outputStream.println("\nGame Over\n");
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
                gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(0));
            }
            else{
                gameBoard.setSelectedGodCards(messageEvent.getMatchCards());
                gameBoard.setColoredGodCard(gameBoard.getSelectedGodCards().get(0));
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
            outputStream.println( "Insert your nickname: ");
            player.setNickname(scanner.nextLine());
        }
        else{
            outputStream.println( "Your nickname is already used!\nInsert a new nickname:   ");
            player.setNickname(scanner.nextLine());
        }
        notify(player.getNickname());
    }

    public void initGettingPlayersNum(){
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(2);
        numbers.add(3);
        player.setPlayersNum(numbers);
        player.setPlayerNumber(player.getPlayersNum().get(0));
    }

    public void  initPlacingState(){
        gameBoard.setColoredPosition(gameBoard.getPlacingAvailableCells().stream().findFirst().get());
    }

    public static void initRunning(){
        if(player.getPlayerState() == PlayerState.ACTIVE &&
                gameBoard.getWorkersAvailableCells() != null &&
                gameBoard.getWorkersPositions() != null){
            if(player.getTurnState() == TurnState.IDLE) {
                gameBoard.setStartingPosition(null);
                gameBoard
                        .setColoredPosition(gameBoard
                                .getWorkersPositions()
                                .stream()
                                .findAny()
                                .get());
            }
            else {
                if ( gameBoard.getStartingPosition() != null &&
                        gameBoard.getWorkersPositions().contains(gameBoard.getStartingPosition()))
                    gameBoard
                            .setColoredPosition(gameBoard
                                    .getWorkersAvailableCells(gameBoard
                                    .getStartingPosition())
                                    .stream()
                                    .findAny()
                                    .get());
                }
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
