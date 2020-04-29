package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.TurnState;
import it.polimi.ingsw.utilities.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class View extends Observable<Object> implements Observer<MessageEvent> {

    static ExecutorService executorUpdate = Executors.newSingleThreadExecutor();
    static ExecutorService executorInput = Executors.newSingleThreadExecutor();

    private Scanner scanner;
    private DataInputStream dataInputStream;
    private PrintStream outputStream;

    private static char inputCharacter;
    private static boolean active = false;

    private GameBoard gameBoard;
    private Player player;

    @Override // lato NETWORK HANDLER
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
                        gameBoard.setColoredPosition(gameBoard.getWorkersPositions().stream().findAny().get());
                    }
                        case MOVE:{
                        gameBoard.setColoredPosition(gameBoard.getWorkersAvailableCells().get(gameBoard.getStartingPosition()).stream().findAny().get());
                    }
                        case BUILD:{
                        gameBoard.setColoredPosition(gameBoard.getWorkersAvailableCells().get(gameBoard.getStartingPosition()).stream().findAny().get());
                    }
            }
    }
        }
    }

    public void fetchingInit(MessageEvent messageEvent){
        if(messageEvent.getPlayerID() != null && messageEvent.getPlayerID() != player.getPlayerID()){
            player.setPlayerID(messageEvent.getPlayerID());
        }
        if(messageEvent.getMatchID() != null && messageEvent.getMatchID() != player.getMatchID()){
            player.setMatchID(messageEvent.getMatchID());
        }
    }

    public void fetching(MessageEvent messageEvent){
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

    public void doUpdate(){
        executorUpdate.submit(()-> view());
    }

    public void view(){
        // mostra la visione a schermo a seconda del differente stato del Match o del player
    }

    public void inputListener(){
        try{
            scanner.close();
            dataInputStream = new DataInputStream(System.in);
            while(active){
                inputCharacter = dataInputStream.readChar();
                notify(inputCharacter);
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

    public void init(){   // -> insert IP
        gameBoard = new GameBoard();
        player = new Player();
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        active = false;
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

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
