package it.polimi.ingsw.client;

// REALIZZARE NOTIFY
// REALIZZARE UPDATE

import it.polimi.ingsw.client.controller.state.InsertCharacter;
import it.polimi.ingsw.server.controller.state.WaitingForPlayers;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.TurnState;
import it.polimi.ingsw.utilities.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
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

    private static boolean update = false;

    private static String inputMessage;
    private static char inputCharacter;
    private static String outputMessage;

   // executor.submit(()-> inputListener(messageEvent));


    @Override // lato NETWORK HANDLER
    public void update(MessageEvent messageEvent) {
        if(messageEvent.getError()) insertNickName();
        else if(!active){
            active = true;
            executorInput.submit(()-> inputListener());
        }
        else{
            executorUpdate.submit(()-> fetching(messageEvent));
        }
    }

    public void updatingDate(MessageEvent messageEvent){
        PlayerState playerState = Player.getPlayerState();
        MatchState matchState = Player.getMatchState();

        if(messageEvent.)

    }

    public void inputListener(){
        //try{
            while(active){

            //}
        //}
        //catch(IOException ex){

        }
    }

    public void init(){   // -> insert IP
        scanner = new Scanner(System.in);
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

    public void fetching(MessageEvent messageEvent){

    }

    public void processingOutputMessage() {
        if (error) outputMessage = "Your nickname is already used. Change it :\n";
        else if (playerState == null || playerState == PlayerState.IDLE || playerState == PlayerState.INITIALIZED)
            outputMessage = "Wait for yor turn :\n";
        else if (matchState == MatchState.GETTING_PLAYERS_NUM)
            outputMessage = "Insert num of player between 2 or 3 :\n";
        else if (matchState == MatchState.WAITING_FOR_PLAYERS) outputMessage = "Waiting for players..\n";
        else if (matchState == MatchState.SELECTING_GOD_CARDS)
            outputMessage = "Make your choice \n";// + godCards.stream().forEach(god -> System.out.println(god));
        else if (matchState == MatchState.SELECTING_SPECIAL_COMMAND)
            outputMessage = "Select your special power \n"; // System.out.println(selectedGodCards);
        else if (matchState == MatchState.PLACING_WORKERS)
            outputMessage = "Choose your position with A,D,W,S button\n";// + System.out.println("Your position actual is X:"+ coloredPosition.getX() + ", Y: " + coloredPosition.getY()+ "\n");
        else if (matchState == MatchState.RUNNING && playerState == PlayerState.ACTIVE && TurnState.IDLE == turnState)
            outputMessage = "Choose a worker\n";
        else if (matchState == MatchState.RUNNING && playerState == PlayerState.ACTIVE && TurnState.MOVE == turnState)
            outputMessage = "Move your worker\n";
        else if (matchState == MatchState.RUNNING && playerState == PlayerState.ACTIVE && TurnState.BUILD == turnState)
            outputMessage = "Build with your worker\n";
        else if (matchState == MatchState.FINISHED) outputMessage = "The match is finished. \n";
        else if (playerState == PlayerState.LOST)
            outputMessage = " DAMN! YOU ARE A LOSER \n"; // OPPURE SCHERMATA GUI
        else if (playerState == PlayerState.WIN) outputMessage = "YOU WIN \n"; // OPPURE SCHERMATA GUI

    }
}

/*
    @Override
    public void run () {
        try {
            while (playerState != PlayerState.LOST || playerState != PlayerState.WIN || matchState != MatchState.FINISHED) {
                if (nickname == null) {
                    outputStream.println(outputMessage);
                    inputMessage = scanner.nextLine();
                    scanner.reset();
                    nickname = inputMessage;
                    notify(inputMessage);
                } else if (error == true) {
                    scanner = new Scanner(System.in);
                    outputStream.println(outputMessage);
                    inputMessage = scanner.nextLine();
                    scanner.reset();
                    nickname = inputMessage;
                    notify(inputMessage);
                    error = false;
                    scanner.close();
                } else if (playerState == null || playerState == PlayerState.INITIALIZED || (PlayerState.IDLE == playerState && MatchState.WAITING_FOR_PLAYERS == matchState)) {
                    if (dataInputStream == null)
                        dataInputStream = new DataInputStream(System.in);
                    outputStream.println(outputMessage);
                    inputCharacter = dataInputStream.readChar();
                    notify(inputCharacter);
                }
                else if(matchState == MatchState.WAITING_FOR_PLAYERS ){
                    outputStream.println(outputMessage);
                    inputCharacter = dataInputStream.readChar();
                    notify(inputCharacter);
                }
                else if(playerState == PlayerState.ACTIVE && matchState == MatchState.GETTING_PLAYERS_NUM){
                    if(coloredPlayersNum == null){
                        outputStream.println("Select the number\n");
                        playersNum = coloredPlayersNum.get(0);
                        update = true;
                    }
                    if(update)    {               //IDEA -> quando cambia coloredPlayersNum in A e D metto update a true
                        outputStream.println("Actual number : " + coloredPlayersNum);   update = false;}
                    inputCharacter = dataInputStream.readChar();
                    notify(inputCharacter);
                }
                else if(playerState == PlayerState.ACTIVE && matchState == MatchState.SELECTING_GOD_CARDS){
                    if(selectedGodCards.size() == 0){
                        outputStream.println("->" + godCards);
                        outputStream.println(outputMessage);
                    }
                    if(coloredGodCard == null){
                        setColoredGodCard(godCards.get(0));
                        update = true;
                    }
                    if(update=true) //IDEA -> quando cambia coloredGodCard in A, D metto update a true
                        outputStream.println("Actual  God is :" + coloredGodCard);
                    inputCharacter = dataInputStream.readChar();
                    notify(inputCharacter);
                }
                else if(playerState == PlayerState.ACTIVE && matchState == MatchState.SELECTING_SPECIAL_COMMAND){ //Idle
                    outputStream.println(outputMessage);
                    if(coloredGodCard == null){
                        setColoredGodCard(selectedGodCards.get(0));
                        outputStream.println("Choose a card from this deck " + selectedGodCards);
                        update = true;
                    }
                    if(update){ //IDEA -> quando cambia coloredGodCard in A, D metto update a true
                        outputStream.println("Actual God is " + coloredGodCard);
                    }
                    inputCharacter = dataInputStream.readChar();
                    notify(inputCharacter);
                    update = false;
                }
                try {
                    while (playerState != PlayerState.WIN || playerState != PlayerState.LOST || matchState != MatchState.FINISHED) {
                        // tipologia di inserimento richiesto       USELESS OR USEFUL ?
                        //outputStream.println();
                        inputCharacter = dataInputStream.readChar();
                        if (InsertCharacter.values().equals(inputCharacter)) {
                            notify(inputCharacter);
                        } else {
                            outputStream.println(" Carattere non disponibile ");
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }


            //DISCONNESSIONE DEL CLIENT

        } catch(NullPointerException ex)

    {
        ex.getMessage();
    } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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