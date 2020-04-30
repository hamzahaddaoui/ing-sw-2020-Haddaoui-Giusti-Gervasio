package it.polimi.ingsw.client.view;

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

    private Scanner scanner;
    private DataInputStream dataInputStream;
    private PrintStream outputStream;
    private char inputCharacter;
    private InsertCharacter insertCharacter;
    private static boolean active = false;

    private static GameBoard gameBoard;
    private static Player player;

    @Override
    public void update(MessageEvent messageEvent) {
        if(messageEvent.getError()) insertNickName();
        else if(!active && !messageEvent.getError()){
            active = true;
            fetchingInit(messageEvent);
            fetching(messageEvent);
            checkStatus();
            doUpdate();
            executorInput.submit(()-> inputListener());
        }
        else{
            fetching(messageEvent);
            checkStatus();
            doUpdate();
        }
    }

    public void checkStatus(){
        switch(player.getMatchState()){
            case GETTING_PLAYERS_NUM:{
            player.setColoredPlayersNum(new ArrayList<>());
            ArrayList<Integer> numPlayer = player.getColoredPlayersNum();
            numPlayer.add(2);
            numPlayer.add(3);
            player.setPlayersNum(numPlayer.get(0));
        }
            case WAITING_FOR_PLAYERS: outputStream.println("WAIT YOUR TURN...");
            case SELECTING_GOD_CARDS:{
            gameBoard.setColoredGodCard(gameBoard.getMatchCards().get(0));
        }
            case SELECTING_SPECIAL_COMMAND:{
            gameBoard.setColoredGodCard(gameBoard.getSelectedGodCards().get(0));
        }
            case PLACING_WORKERS:{
                gameBoard.setColoredPosition(gameBoard.getPlacingAvailableCells().stream().findAny().get());
            }
            case RUNNING: {
                if(player.getPlayerState()== PlayerState.ACTIVE)
                    switch(player.getTurnState()){
                        case IDLE:{
                            if(gameBoard.getWorkersAvailableCells().size()>0)
                                gameBoard.setStartingPosition(gameBoard.getWorkersAvailableCells().keySet().stream().findAny().get());
                            gameBoard.setColoredPosition(gameBoard.getStartingPosition());
                        }
                        case MOVE:{
                            gameBoard.setColoredPosition(gameBoard.getWorkersAvailableCells().get(gameBoard.getStartingPosition()).stream().findAny().get());
                        }
                        case BUILD:{
                            gameBoard.setColoredPosition(gameBoard.getWorkersAvailableCells().get(gameBoard.getStartingPosition()).stream().findAny().get());
                        }
                }
                else if(player.getPlayerState() == PlayerState.LOST || player.getPlayerState() == PlayerState.WIN){
                    active = false;
                }
            }
            case FINISHED:
                active = false;
        }
    }

    public static void fetchingInit(MessageEvent messageEvent){
        if(messageEvent.getPlayerID() != null && messageEvent.getPlayerID() != player.getPlayerID()){
            player.setPlayerID(messageEvent.getPlayerID());
        }
        if(messageEvent.getMatchID() != null && messageEvent.getMatchID() != player.getMatchID()){
            player.setMatchID(messageEvent.getMatchID());
        }
    }

    public void fetching(MessageEvent messageEvent){
        if(messageEvent.getMatchID() != null && messageEvent.getMatchID() != player.getMatchID()){
            player.setMatchID(messageEvent.getMatchID());
        }
        if(messageEvent.getMatchState() != player.getMatchState() && messageEvent.getMatchState() != null){
            player.setMatchState(messageEvent.getMatchState());
        }
        if(messageEvent.getPlayerState() != player.getPlayerState() && messageEvent.getPlayerState() != null){
            player.setPlayerState(messageEvent.getPlayerState());
        }
        if(messageEvent.getTurnState() != player.getTurnState() && messageEvent.getTurnState() != null){
            player.setTurnState(messageEvent.getTurnState());
        }
        if(messageEvent.getMatchCards() != gameBoard.getMatchCards() && messageEvent.getMatchCards() != null){
            gameBoard.setMatchCards(messageEvent.getMatchCards());
        }
        if(messageEvent.getAvailablePlacingCells() != gameBoard.getPlacingAvailableCells() && messageEvent.getAvailablePlacingCells() != null){
            gameBoard.setPlacingAvailableCells(messageEvent.getAvailablePlacingCells());
        }
        if(messageEvent.getBillboardStatus() != gameBoard.getBillboardStatus() && messageEvent.getBillboardStatus() != null){
            gameBoard.setBillboardStatus(messageEvent.getBillboardStatus());
        }
        if(messageEvent.getWorkersAvailableCells() != gameBoard.getWorkersAvailableCells() && messageEvent.getWorkersAvailableCells()!=  null){
            gameBoard.setWorkersAvailableCells(messageEvent.getWorkersAvailableCells());
        }
        if(messageEvent.getTerminateTurnAvailable() != player.isTerminateTurnAvailable() && messageEvent.getTerminateTurnAvailable() != null){
            player.setTerminateTurnAvailable(messageEvent.getTerminateTurnAvailable());
        }
        if(messageEvent.getSpecialFunctionAvailable() != player.getSpecialFunctionAvailable() && messageEvent.getSpecialFunctionAvailable() != null){
            player.setSpecialFunctionAvailable(messageEvent.getSpecialFunctionAvailable());
        }
        if(messageEvent.getMatchPlayers() != player.getMatchPlayers() && messageEvent.getMatchPlayers()!=null){
            player.setMatchPlayers(messageEvent.getMatchPlayers());
        }
    }

    public static void doUpdate(){
        executorUpdate.submit(()-> view());
    }

    public static void view(){
        // mostra la visione a schermo a seconda del differente stato del Match o del player
    }

    public void inputListener(){
        try{
            dataInputStream = new DataInputStream(System.in);
            scanner.close();
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

                   // ---- DISCONNESSIONE CLIENT ----

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean commute(char inputCharacter){
        InsertCharacter insertCharacters = Arrays.stream(InsertCharacter.values()).filter(insertCharacter1 -> insertCharacter1.equals(inputCharacter)).findFirst().get();
        if(insertCharacters == null){
            insertCharacter = insertCharacters;
            return true;
        }
        else return false;
    }

    public void init(){   // -> insert IP
        active = false;
        player = new Player();
        gameBoard = new GameBoard();
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        outputStream.println( "Insert your ip : ");
        player.setIp(scanner.nextLine());
        outputStream.println( "Insert your nickname : ");
        player.setNickname(scanner.nextLine());
        notify(player.getNickname());
    }

    public void insertNickName(){
        outputStream.println( "Your nickname is already used! ");
        outputStream.println( "Insert a new nickname : ");
        player.setNickname(scanner.nextLine());
        notify(player.getNickname());
    }

    public static GameBoard getGameBoard() {
        return gameBoard;
    }

    public static Player getPlayer() {
        return player;
    }

    public static View constructor(){
        active = false;
        player = new Player();
        gameBoard = new GameBoard();
        return new View();
    }
}
