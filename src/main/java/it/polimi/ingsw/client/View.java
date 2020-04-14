package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Position;


import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class View extends Observable implements Runnable, Observer{
    Scanner scanner = new Scanner(System.in);

    private static String playerState;
    private static String matchState;
    private static Set<String> godCards;
    private static int playersNum;
    private static String godCard;
    private static Map<Position, Cell> billboardStatus;
    private static Map<Position, Set<Position>> workersAvailableCells;
    private Map<Integer, String> matchPlayers;

    public static Set<String> getGodCards() {
        return godCards;
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
}
