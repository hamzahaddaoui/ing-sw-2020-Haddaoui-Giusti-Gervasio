package it.polimi.ingsw.client;

// REALIZZARE NOTIFY
// REALIZZARE UPDATE

import it.polimi.ingsw.client.controller.state.InsertCharacter;
import it.polimi.ingsw.server.model.Player;
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
import java.util.stream.Collectors;

public class View extends Observable<Object> implements Runnable, Observer<MessageEvent> {


    private Scanner scanner;
    private DataInputStream dataInputStream;
    private PrintStream outputStream;

    private static boolean error = false;
    private static boolean update = false;

    private static Integer matchID;
    private static Integer playerID;
    private static String nickname;
    private static PlayerState playerState;
    private static MatchState matchState;
    private static TurnState turnState;

    private static ArrayList<Integer> coloredPlayersNum;
    private static Integer playersNum;
    //sono le carte che vengono inserite in fase di SelectedSpecialCommandsStatus
    private static ArrayList<String> selectedGodCards;
    //sono le carte che vengono inserite in fase di SelectingGodCardsStatus, inizialmente prese dal Server
    private static ArrayList<String> godCards;
    private static String coloredGodCard;

    private static Map<Position, Cell> billboardStatus;
    private static Map<Position, Set<Position>> workersAvailableCells;
    private static Set<Position> placingAvailableCells;
    private static Position startingPosition;
    private static Position coloredPosition;

    private static Map<Integer, String> matchPlayers;
    private static boolean terminateTurnAvailable;
    private static Map<Position, Boolean> specialFunctionAvailable;

    private static String inputMessage;
    private static char inputCharacter;
    private static String outputMessage;

    public View() {
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
        outputMessage = "Insert your Nickname :\n";
    }


    public void viewSetUp() {
        coloredPlayersNum = new ArrayList<>();
        View.coloredPlayersNum.add(2);
        View.coloredPlayersNum.add(3);
        View.playersNum = View.coloredPlayersNum.get(0);
    }

    @Override
    public void update(MessageEvent message) {
        fetchingMessage(message); //analisi del messaggio e reset dei dati del client

        processingOutputMessage(); //preparo il messaggio che poi verrà mandato tramite run()

        // DISCONNESSIONE DEL CLIENT effettuata da run
    }

    @Override
    public void run () {
        try {
            while (playerState != PlayerState.LOST || playerState != PlayerState.WIN || matchState != MatchState.FINISHED) {
                if(nickname == null){
                    outputStream.println(outputMessage);
                    inputMessage = scanner.nextLine();
                    scanner.reset();
                    nickname = inputMessage;
                    notify(inputMessage);
                }
                else if(error == true){
                    scanner = new Scanner(System.in);
                    outputStream.println(outputMessage);
                    inputMessage = scanner.nextLine();
                    scanner.reset();
                    nickname = inputMessage;
                    notify(inputMessage);
                    error = false;
                    scanner.close();
                }
                else if(playerState == null ||  playerState == PlayerState.INITIALIZED || (PlayerState.IDLE==playerState && MatchState.WAITING_FOR_PLAYERS==matchState)){
                    if(dataInputStream == null)
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
    }

    public void fetchingMessage(MessageEvent message){
        if(message.getMatchID() != null)  setMatchID(message.getMatchID());
        if(message.getPlayerID() != null) setPlayerID(message.getPlayerID());
        if(message.getMatchState() != null){setMatchState(message.getMatchState()); update=true;}
        if(message.getPlayerState() != null){setPlayerState(message.getPlayerState()); update=true;}
        if(message.getTurnState() != null){ setTurnState(message.getTurnState()); update=true;}
        if(message.getPlayersNum() != null){ setPlayersNum(message.getPlayersNum()); update=true;}
        if(message.getGodCards() != null){ setGodCards(message.getGodCards()); update=true;}
        if(message.getMatchCards() != null && View.getMatchState() == MatchState.SELECTING_GOD_CARDS){setGodCards(message.getMatchCards()); update=true;}
        setError(message.getError());
        if(message.getGodCard() != null) setColoredGodCard(message.getGodCard());
        if(message.getStartPosition() != null){setStartingPosition(message.getStartPosition()); update=true;}
        if(message.getSpecialFunctionAvailable() != null){setSpecialFunctionAvailable(message.getSpecialFunctionAvailable()); update=true;}
        if(message.getMatchCards() != null && View.getMatchState() == MatchState.SELECTING_GOD_CARDS){setGodCards(message.getMatchCards()); update=true;}
        if(message.getAvailablePlacingCells() != null){setPlacingAvailableCells(message.getAvailablePlacingCells()); update=true;}
        if(message.getBillboardStatus() != null){setBillboardStatus(message.getBillboardStatus()); update=true;}
        if(message.getWorkersAvailableCells() != null){setWorkersAvailableCells(message.getWorkersAvailableCells()); update=true;}
        if(message.getTerminateTurnAvailable() != null){setTerminateTurnAvailable(message.getTerminateTurnAvailable()); update=true;}
        if(message.getMatchPlayers() != null){setMatchPlayers(message.getMatchPlayers()); update=true;}
    }

    public void processingOutputMessage(){
        if(error) outputMessage = "Your nickname is already used. Change it :\n";
        else if(playerState == null || playerState == PlayerState.IDLE || playerState == PlayerState.INITIALIZED) outputMessage = "Wait for yor turn :\n";
        else if(matchState == MatchState.GETTING_PLAYERS_NUM)  outputMessage = "Insert num of player between 2 or 3 :\n";
        else if(matchState == MatchState.WAITING_FOR_PLAYERS) outputMessage = "Waiting for players..\n";
        else if(matchState == MatchState.SELECTING_GOD_CARDS) outputMessage = "Make your choice \n";// + godCards.stream().forEach(god -> System.out.println(god));
        else if(matchState == MatchState.SELECTING_SPECIAL_COMMAND) outputMessage = "Select your special power \n"; // System.out.println(selectedGodCards);
        else if(matchState == MatchState.PLACING_WORKERS) outputMessage = "Choose your position with A,D,W,S button\n";// + System.out.println("Your position actual is X:"+ coloredPosition.getX() + ", Y: " + coloredPosition.getY()+ "\n");
        else if(matchState == MatchState.RUNNING && playerState== PlayerState.ACTIVE && TurnState.IDLE== turnState) outputMessage = "Choose a worker\n";
        else if(matchState == MatchState.RUNNING && playerState== PlayerState.ACTIVE && TurnState.MOVE== turnState) outputMessage = "Move your worker\n";
        else if(matchState == MatchState.RUNNING && playerState== PlayerState.ACTIVE && TurnState.BUILD== turnState) outputMessage = "Build with your worker\n";
        else if(matchState == MatchState.FINISHED) outputMessage = "The match is finished. \n";
        else if (playerState == PlayerState.LOST) outputMessage= " DAMN! YOU ARE A LOSER \n"; // OPPURE SCHERMATA GUI
        else if (playerState == PlayerState.WIN) outputMessage= "YOU WIN \n"; // OPPURE SCHERMATA GUI

    }

    public static void setError(boolean error) {
        View.error = error;
    }

    public static void setGodCards(ArrayList<String> godCards) {
        View.godCards = godCards;
    }

    public static void setPlacingAvailableCells(Set<Position> placingAvailableCells) {
        View.placingAvailableCells = placingAvailableCells;
    }

    public static Map<Position, Set<Position>> getWorkersAvailableCells() {
        return workersAvailableCells;
    }

    public static void setWorkersAvailableCells(Map workersAvailableCells) {
        View.workersAvailableCells = workersAvailableCells;
    }

    public static void setMatchID(Integer matchID) {
        View.matchID = matchID;
    }

    public static void setPlayerID(Integer playerID) {
        View.playerID = playerID;
    }

    public static void setPlayerState(PlayerState playerState) {
        View.playerState = playerState;
    }

    public static void setMatchState(MatchState matchState) {
        View.matchState = matchState;
    }

    public static TurnState getTurnState() {
        return turnState;
    }

    public static void setTurnState(TurnState turnState) {
        View.turnState = turnState;
    }

    public static void setBillboardStatus(Map<Position, Cell> billboardStatus) {
        View.billboardStatus = billboardStatus;
    }

    public static void setMatchPlayers(Map<Integer, String> matchPlayers) {
        View.matchPlayers = matchPlayers;
    }

    public static void setTerminateTurnAvailable(boolean terminateTurnAvailable) {
        View.terminateTurnAvailable = terminateTurnAvailable;
    }

    public static ArrayList<String> getGodCards() {
        return godCards;
    }

    public static Integer getMatchID() {
        return matchID;
    }

    public static Integer getPlayerID() {
        return playerID;
    }

    public static Set<Position> getPlacingAvailableCells() {
        return placingAvailableCells;
    }

    public static String getColoredGodCard() {
        return coloredGodCard;
    }

    public static void setColoredGodCard(String coloredGodCard) {
        View.coloredGodCard = coloredGodCard;
    }

    public static Position getStartingPosition() {
        return startingPosition;
    }

    public static void setStartingPosition(Position startingPosition) {
        View.startingPosition = startingPosition;
    }

    public static Position getColoredPosition() {
        return coloredPosition;
    }

    public static void setColoredPosition(Position position) {
        coloredPosition = position;
    }

    public static void setSelectedGodCards(ArrayList<String> selectedGodCards) {
        View.selectedGodCards = selectedGodCards;
    }

    public static ArrayList<String> getSelectedGodCards() {
        return selectedGodCards;
    }

    public static ArrayList<Integer> getColoredPlayersNum() {
        return coloredPlayersNum;
    }

    public static void setColoredPlayersNum(ArrayList<Integer> coloredPlayersNum) {
        View.coloredPlayersNum = coloredPlayersNum;
    }

    public static PlayerState getPlayerState() {
        return playerState;
    }

    public static MatchState getMatchState() {
        return matchState;
    }

    public static Integer getPlayersNum() {
        return playersNum;
    }

    public static Map<Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    public static Set<Position> getWorkersAvailableCells(Position position) {
        return workersAvailableCells.get(position);
    }

    public static Set<Position> getWorkersPositions() { return workersAvailableCells.keySet();}

    public static boolean isWorkerPresent(Position position) {
        return workersAvailableCells.containsKey(position);
    }

    public static boolean isTerminateTurnAvailable() {
        return terminateTurnAvailable;
    }

    public static boolean isSpecialFunctionAvailable(Position position) {
        return specialFunctionAvailable.get(position);
    }

    /*
    -------------------------------------------------------------------------------
    -----------------------METODI UTILI NEI TEST-----------------------------------
    -------------------------------------------------------------------------------
     */

    public static void setGodCards (Set<String> godCards) {
        selectedGodCards = new ArrayList<String>(godCards);
    }

    public static void setPlayersNum (int selectedPlayersNum) {
        playersNum = selectedPlayersNum;
    }

    public static void setSpecialFunctionAvailable(Map<Position,Boolean> modelMap) {
        specialFunctionAvailable = modelMap;
    }
}
