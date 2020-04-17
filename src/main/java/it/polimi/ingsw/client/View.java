package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Position;


import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class View extends Observable implements Runnable, Observer{
    private Scanner scanner;
    private PrintStream outputStream;

    private static Integer matchID;
    private static Integer playerID;
    private static String playerState;
    private static String matchState;
    private static Set<String> godCards;
    private static Integer playersNum;
    private static String godCard;
    private static Map<Position, Cell> billboardStatus;
    private static Map<Position, Set<Position>> workersAvailableCells;
    private static Set<Position> placingAvailableCells;
    private Map<Integer, String> matchPlayers;
    private InputState state;
    private ViewControllerEvent event;

    public View() {
        scanner = new Scanner(System.in);
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

     while(true) {
         outputStream.println("Insert a nickname: ");
         String nickname = scanner.next();
         state = InputState.Nickname;
         event = new ViewControllerEvent(state, nickname);
         notify(event);
     }
    }

    public static String getPlayerState() {
        return playerState;
    }

    public static String getMatchState() {
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

    private void setInputState() {
    }
}
