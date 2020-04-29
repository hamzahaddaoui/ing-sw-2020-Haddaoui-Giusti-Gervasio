package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.state.InsertCharacter;
import it.polimi.ingsw.server.controller.state.WaitingForPlayers;
import it.polimi.ingsw.server.model.Billboard;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.TurnState;
import it.polimi.ingsw.utilities.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class View extends Observable<Object> implements Observer<MessageEvent> {

    static ExecutorService executorUpdate = Executors.newSingleThreadExecutor();
    static ExecutorService executorInput = Executors.newSingleThreadExecutor();

    private static boolean active = false;
    private Scanner scanner;
    private DataInputStream dataInputStream;
    private PrintStream outputStream;

    private static char inputCharacter;


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
        switch(Player.getMatchState()){
            case GETTING_PLAYERS_NUM:{
            Player.setColoredPlayersNum(new ArrayList<>());
            ArrayList<Integer> numPlayer = Player.getColoredPlayersNum();
            numPlayer.add(2);
            numPlayer.add(3);
            Player.setPlayersNum(numPlayer.get(0));
        }
            case WAITING_FOR_PLAYERS: outputStream.println("WAIT YOUR TURN...");
            case SELECTING_GOD_CARDS:{
            GameBoard.setColoredGodCard(GameBoard.getMatchCards().get(0));
        }
            case SELECTING_SPECIAL_COMMAND:{
            GameBoard.setColoredGodCard(GameBoard.getSelectedGodCards().get(0));
        }
            case PLACING_WORKERS:{
                GameBoard.setColoredPosition(GameBoard.getPlacingAvailableCells().stream().findAny().get());
            }
            case RUNNING: {
                if(Player.getPlayerState()== PlayerState.ACTIVE && Player.getTurnState()== TurnState.IDLE){
                    GameBoard.setColoredPosition(GameBoard.getWorkersPositions().stream().findAny().get());
                }
                if(Player.getPlayerState()== PlayerState.ACTIVE && Player.getTurnState()== TurnState.MOVE){
                    GameBoard.setColoredPosition(GameBoard.getWorkersAvailableCells().get(GameBoard.getStartingPosition()).stream().findAny().get());
                }
                if(Player.getPlayerState()== PlayerState.ACTIVE && Player.getTurnState()== TurnState.BUILD){
                    GameBoard.setColoredPosition(GameBoard.getWorkersAvailableCells().get(GameBoard.getStartingPosition()).stream().findAny().get());
                }
            }
    }}

    public void fetchingInit(MessageEvent messageEvent){
        if(messageEvent.getPlayerID() != null && messageEvent.getPlayerID() != Player.getPlayerID()){
            Player.setPlayerID(messageEvent.getPlayerID());
        }
        if(messageEvent.getMatchID() != null && messageEvent.getMatchID() != Player.getMatchID()){
            Player.setMatchID(messageEvent.getMatchID());
        }
    }

    public void fetching(MessageEvent messageEvent){
        if(messageEvent.getMatchState() != Player.getMatchState() && messageEvent.getMatchState() != null){
            Player.setMatchState(messageEvent.getMatchState());
        }
        if(messageEvent.getPlayerState() != Player.getPlayerState() && messageEvent.getPlayerState() != null){
            Player.setPlayerState(messageEvent.getPlayerState());
        }
        if(messageEvent.getTurnState() != Player.getTurnState() && messageEvent.getTurnState() != null){
            Player.setTurnState(messageEvent.getTurnState());
        }
        if(messageEvent.getMatchCards() != GameBoard.getMatchCards() && messageEvent.getMatchCards() != null){
            GameBoard.setMatchCards(messageEvent.getMatchCards());
        }
        if(messageEvent.getAvailablePlacingCells() != GameBoard.getPlacingAvailableCells() && messageEvent.getAvailablePlacingCells() != null){
            GameBoard.setPlacingAvailableCells(messageEvent.getAvailablePlacingCells());
        }
        if(messageEvent.getBillboardStatus() != GameBoard.getBillboardStatus() && messageEvent.getBillboardStatus() != null){
            GameBoard.setBillboardStatus(messageEvent.getBillboardStatus());
        }
        if(messageEvent.getWorkersAvailableCells() != GameBoard.getWorkersAvailableCells() && messageEvent.getWorkersAvailableCells()!=  null){
            GameBoard.setWorkersAvailableCells(messageEvent.getWorkersAvailableCells());
        }
        if(messageEvent.getTerminateTurnAvailable() != Player.isTerminateTurnAvailable() && messageEvent.getTerminateTurnAvailable() != null){
            Player.setTerminateTurnAvailable(messageEvent.getTerminateTurnAvailable());
        }
        if(messageEvent.getSpecialFunctionAvailable() != Player.getSpecialFunctionAvailable() && messageEvent.getSpecialFunctionAvailable() != null){
            Player.setSpecialFunctionAvailable(messageEvent.getSpecialFunctionAvailable());
        }
        if(messageEvent.getMatchPlayers() != Player.getMatchPlayers() && messageEvent.getMatchPlayers()!=null){
            Player.setMatchPlayers(messageEvent.getMatchPlayers());
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
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        active = false;
        outputStream.println( "Insert your ip : ");
        Player.setIp(scanner.nextLine());
        outputStream.println( "Insert your nickname : ");
        Player.setNickname(scanner.nextLine());
        notify(Player.getNickname());
    }

    public void insertNickName(){
        outputStream.println( "Your nickname is already used! ");
        outputStream.println( "Insert a new nickname : ");
        Player.setNickname(scanner.nextLine());
        notify(Player.getNickname());
    }

}

    /*public void fetchingMessage (MessageEvent message){
        if(message.getError() == true) managementError();
        else if(message.getMatchID() != null || message.getPlayerID() != null || ())  managementGenerality();
        else if(message.getMatchState() !=  matchState|| message.getPlayerState() != playerState|| message.getTurnState() != turnState) ma
        else if(message.getGodCards() != null)  setGodCards(message.getGodCards());
        if(message.getMatchCards() != null && View.getMatchState() == MatchState.SELECTING_GOD_CARDS)setGodCards(message.getMatchCards());
        setError(message.getError());
        if(message.getGodCard() != null) setColoredGodCard(message.getGodCard());
        if(message.getStartPosition() != null)setStartingPosition(message.getStartPosition());
        if(message.getSpecialFunctionAvailable() != null)setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
        if(message.getMatchCards() != null && View.getMatchState() == MatchState.SELECTING_GOD_CARDS)setGodCards(message.getMatchCards());
        if(message.getAvailablePlacingCells() != null) setPlacingAvailableCells(message.getAvailablePlacingCells());
        if(message.getBillboardStatus() != null) setBillboardStatus(message.getBillboardStatus());
        if(message.getWorkersAvailableCells() != null) setWorkersAvailableCells(message.getWorkersAvailableCells());
        if(message.getTerminateTurnAvailable() != null) setTerminateTurnAvailable(message.getTerminateTurnAvailable());
        if(message.getMatchPlayers() != null) setMatchPlayers(message.getMatchPlayers());
    }*/