package it.polimi.ingsw.client;

// REALIZZARE NOTIFY
// REALIZZARE UPDATE

import it.polimi.ingsw.client.controller.state.InsertCharacter;
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

    public View() {
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);

    }


    public void viewSetUp() {
        coloredPlayersNum = new ArrayList<>();
        View.coloredPlayersNum.add(2);
        View.coloredPlayersNum.add(3);
        View.playersNum = View.coloredPlayersNum.get(0);
    }

    @Override
    public void update(MessageEvent message) {
        //se ricevo un messaggio dal model
        fetchingMessage(message); //analisi del messaggio e reset dei dati del client

        //aggiorna la scacchiera a video -> interazione con la CLI/GUI

        if (playerState == PlayerState.LOST) {
            outputStream.println(" DAMN! YOU ARE A LOSER "); // OPPURE SCHERMATA GUI
        } else if (playerState == PlayerState.WIN) {
            outputStream.println(" YOU WIN "); // OPPURE SCHERMATA GUI
        }

        // DISCONNESSIONE DEL CLIENT
    }

    @Override
    public void run () {
        try {
            while (true) {
                if(nickname == null){
                outputStream.println("Insert a nickname: \n");
                inputMessage = scanner.nextLine();
                scanner.reset();
                nickname = inputMessage;
                notify(inputMessage);
                }
                else if(error == true){
                    outputStream.println("Your Nickname is already used! \n\nInsert a new nickname: \n");
                    inputMessage = scanner.nextLine();
                    scanner.reset();
                    nickname = inputMessage;
                    notify(inputMessage);
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
        if(message.getMatchID() != null)
            setMatchID(message.getMatchID());
        if(message.getPlayerID() != null)
            setPlayerID(message.getPlayerID());
        if(message.getMatchState() != null)
            setMatchState(message.getMatchState());
        if(message.getPlayerState() != null)
            setPlayerState(message.getPlayerState());
        if(message.getTurnState() != null)
            setTurnState(message.getTurnState());
        if(message.getPlayersNum() != null)
            setPlayersNum(message.getPlayersNum());
        if(message.getGodCards() != null)
            setGodCards(message.getGodCards());
        if(message.getMatchCards() != null && View.getMatchState() == MatchState.SELECTING_GOD_CARDS)
            setGodCards(message.getMatchCards());
        setError(message.getError());
        if(message.getGodCard() != null)
            setColoredGodCard(message.getGodCard());
        if(message.getStartPosition() != null)
            setStartingPosition(message.getStartPosition());
        if(message.getSpecialFunctionAvailable() != null)
            setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
        if(message.getMatchCards() != null && View.getMatchState() == MatchState.SELECTING_GOD_CARDS)
            setGodCards(message.getMatchCards());
        if(message.getAvailablePlacingCells() != null)
            setPlacingAvailableCells(message.getAvailablePlacingCells());
        if(message.getBillboardStatus() != null)
            setBillboardStatus(message.getBillboardStatus());
        if(message.getWorkersAvailableCells() != null)
            setWorkersAvailableCells(message.getWorkersAvailableCells());
        if(message.getTerminateTurnAvailable() != null)
            setTerminateTurnAvailable(message.getTerminateTurnAvailable());
        if(message.getMatchPlayers() != null)
            setMatchPlayers(message.getMatchPlayers());
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
