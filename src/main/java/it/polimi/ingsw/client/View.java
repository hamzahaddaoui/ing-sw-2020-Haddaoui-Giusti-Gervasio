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

    private static Integer matchID;
    private static Integer playerID;
    private static PlayerState playerState;
    private static MatchState matchState;
    private static TurnState turnState;

    private static ArrayList<Integer> coloredPlayersNum;
    private static Integer playersNum;

    private static ArrayList<String> selectedGodCards; //sono le carte che vengono inserite in fase di SelectedSpecialCommandsStatus
    private static ArrayList<String> godCards; //sono le carte che vengono inserite in fase di SelectingGodCardsStatus
    private static String coloredGodCard;

    private static Map<Position, Cell> billboardStatus;
    private static Map<Position, Set<Position>> workersAvailableCells;
    private static Set<Position> placingAvailableCells;
    private static Position startingPosition;
    private static Position coloredPosition;

    private static Map<Integer, String> matchPlayers;
    private static boolean terminateTurnAvailable;
    private static boolean specialFunctionAvailable;


    public View() {
        inputStream = new DataInputStream(System.in);
        outputStream = new PrintStream(System.out);
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

        coloredPlayersNum.add(2);
        coloredPlayersNum.add(3);
        playersNum = coloredPlayersNum.get(0);
        setColoredPosition(getPlacingAvailableCells().stream().findFirst().get());

     while(true) {
         outputStream.println("Insert a nickname: ");
         notify();
     }
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

    public static boolean isSpecialFunctionAvailable() {
        return specialFunctionAvailable;
    }

}
