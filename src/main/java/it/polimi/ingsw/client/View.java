package it.polimi.ingsw.client;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.TurnState;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Position;

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
    private static Set<String> godCards; //sono le carte che vengono inserite in fase di SelectingGodCardsStatus
    private static Integer playersNum;
    private static String godCard;
    private static Map<Position, Cell> billboardStatus;
    private static Map<Position, Set<Position>> workersAvailableCells;
    private static Set<Position> placingAvailableCells;
    private static Position coloredPosition;
    private Map<Integer, String> matchPlayers;
    private ViewControllerEvent event;

    public View() {
        inputStream = new DataInputStream(System.in);
        outputStream = new PrintStream(System.out);
    }


    public static Set<String> getGodCards() {
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

    public static Map<Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    public static Set<Position> getWorkersAvailableCells(Position position) {
        return workersAvailableCells.get(position);
    }

    public static boolean isWorkerPresent(Position position) {
        return workersAvailableCells.containsKey(position);
    }

    public static Integer setPlayersNumChoice(int value){
        playersNum = playersNumChoice.get(value);
        return playersNum;
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
}
