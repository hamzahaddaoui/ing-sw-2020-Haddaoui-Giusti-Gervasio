package it.polimi.ingsw.client;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.TurnState;
import it.polimi.ingsw.utilities.*;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class View extends Observable implements Runnable, Observer{
    private InputStream inputStream;
    private PrintStream outputStream;

    private static ArrayList<Integer> playersNumChoice;
    private static Integer matchID;
    private static Integer playerID;
    private static PlayerState playerState;
    private static MatchState matchState;
    private static TurnState turnState;
    private static ArrayList<String> selectedGodCards;
    private static ArrayList<String> godCards; //sono le carte che vengono inserite in fase di SelectingGodCardsStatus
    private static Integer playersNum;
    private static String godCard;
    private static Map<it.polimi.ingsw.utilities.Position, Cell> billboardStatus;
    private static Map<it.polimi.ingsw.utilities.Position, Set<it.polimi.ingsw.utilities.Position>> workersAvailableCells;
    private static Set<Position> placingAvailableCells;
    private static it.polimi.ingsw.utilities.Position startingPosition;
    private static it.polimi.ingsw.utilities.Position coloredPosition;
    private static String coloredGodCard;
    private Map<Integer, String> matchPlayers;
    private static boolean terminateTurnAvailable;
    private static boolean specialFunctionAvailable;


    public View() {
        inputStream = new DataInputStream(System.in);
        outputStream = new PrintStream(System.out);
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

    public static it.polimi.ingsw.utilities.Position getStartingPosition() {
        return startingPosition;
    }

    public static void setStartingPosition(it.polimi.ingsw.utilities.Position startingPosition) {
        View.startingPosition = startingPosition;
    }

    public static it.polimi.ingsw.utilities.Position getColoredPosition() {
        return coloredPosition;
    }

    public static void setColoredPosition(it.polimi.ingsw.utilities.Position coloredPosition) {
        View.coloredPosition = coloredPosition;
    }

    public static ArrayList<String> getSelectedGodCards() {
        return selectedGodCards;
    }

    @Override
    public void update(Object message){
        //se ricevo un messaggio dal model
        //aggiorna la scacchiera a video
    }

    @Override
    public void run(){
        /*VCEvent vcEvent = new VCEvent("nick", new Position(2,2));
        while(true) {

            int x, y;
            System.out.println("Insert position X");
            x = scanner.nextInt();
            System.out.println("Insert position Y");
            y = scanner.nextInt();

            notify(vcEvent);
        }*/

        playersNumChoice.add(2);
        playersNumChoice.add(3);
        playersNum = playersNumChoice.get(0);

     while(true) {
         outputStream.println("Insert a nickname: ");
         notify();
     }
    }

    public static PlayerState getPlayerState() {
        return playerState;
    }

    public static MatchState getMatchState() {
        return matchState;
    }

    public static int getPlayersNum() {
        return playersNum;
    }

    public static Map<it.polimi.ingsw.utilities.Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    public static Set<it.polimi.ingsw.utilities.Position> getWorkersAvailableCells(it.polimi.ingsw.utilities.Position position) {
        return workersAvailableCells.get(position);
    }

    public static Set<it.polimi.ingsw.utilities.Position> getWorkersPositions() { return workersAvailableCells.keySet();}

    public static boolean isWorkerPresent(it.polimi.ingsw.utilities.Position position) {
        return workersAvailableCells.containsKey(position);
    }

    public static void playersNumChoiceIncrement(){
        playersNum = playersNumChoice.get((playersNumChoice.indexOf(playersNum) + 1) % playersNumChoice.size());
    }

    public static Integer getPlayersNumChoice(){
        return playersNum;
    }

    public static void addGodCard(String godCard) {
        View.godCards.add(godCard);
    }

    public static boolean isTerminateTurnAvailable() {
        return terminateTurnAvailable;
    }

    public static boolean isSpecialFunctionAvailable() {
        return specialFunctionAvailable;
    }
}
