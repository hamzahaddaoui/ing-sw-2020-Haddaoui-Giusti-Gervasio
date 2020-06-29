package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.State;
import it.polimi.ingsw.utilities.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class Database {
    static private Stage stage;
    static private Scene scene;

    static private NetworkHandler networkHandler;
    static private State currentState;
    static private IslandLoader islandLoader;


    //#############     PRELIMINARY DATA     ####################
    static private String IP;
    static private String nickname;

    static private int playerID;
    static private int matchID;

    static private int playersNum;
    static private List<String> selectedGodCards = new ArrayList<>();

    //#############     MATCH DATA     ####################
    static private ArrayList<String> matchCards;           //date dal Server, usate per la Selecting God Card
    static private MatchState matchState;
    static private Map<Integer, String> matchPlayers;
    static private Map<Integer, String>  matchColors;
    static private int currentPlayer;
    static private Map<Position, Cell> billboardStatus;

    //#############     PLAYER DATA     ####################
    static private String godCard;

    static private PlayerState playerState;
    static private TurnState turnState;

    static private boolean terminateTurnAvailable;
    static private Map<Position, Boolean> specialFunctionAvailable;

    static private Set<Position> initializedPositions = new HashSet<>();

    static private Set<Position> placingAvailableCells;

    static private Position startingPosition;
    static private Position endPosition;

    static private Map<Position, Set<Position>> workersAvailableCells;


    public static void wipeData (){
        playerID = 0;
        matchID = 0;
        networkHandler = null;
        currentState = null;
        IP = null;
        nickname = null;
        playersNum = 0;
        selectedGodCards = new ArrayList<>();
        initializedPositions = new HashSet<>();
        matchCards = null;
        matchState = null;
        matchPlayers = null;
        matchColors = null;
        currentPlayer = 0;
        billboardStatus = null;
        godCard = null;
        playerState = null;
        turnState = null;
        terminateTurnAvailable = false;
        specialFunctionAvailable = null;
        placingAvailableCells = null;
        startingPosition = null;
        workersAvailableCells = null;
    }

    public static Scene getScene(){
        return scene;
    }

    public static void setScene(Scene scene){
        Database.scene = scene;
    }

    public static int getPlayerID(){
        return playerID;
    }

    public static void setPlayerID(int playerID){
        Database.playerID = playerID;
    }

    public static int getMatchID(){
        return matchID;
    }

    public static void setMatchID(int matchID){
        Database.matchID = matchID;
    }

    public static NetworkHandler getNetworkHandler(){
        return networkHandler;
    }

    public static void setNetworkHandler(NetworkHandler networkHandler){
        Database.networkHandler = networkHandler;
    }

    public static IslandLoader getIslandLoader(){
        return islandLoader;
    }

    public static void setIslandLoader(IslandLoader islandLoader){
        Database.islandLoader = islandLoader;
    }

    public static void setSelectedGodCards(List<String> selectedGodCards){
        Database.selectedGodCards = selectedGodCards;
    }

    public static State getCurrentState(){
        return currentState;
    }

    public static void setCurrentState(State currentState){
        Database.currentState = currentState;
    }

    public static Stage getStage(){
        return stage;
    }

    public static void setStage(Stage stage){
        Database.stage = stage;
    }

    public static String getIP(){
        return IP;
    }

    public static void setIP(String IP){
        Database.IP = IP;
    }

    public static String getNickname(){
        return nickname;
    }

    public static void setNickname(String nickname){
        Database.nickname = nickname;
    }

    public static int getPlayersNum(){
        return playersNum;
    }

    public static void setPlayersNum(int playersNum){
        Database.playersNum = playersNum;
    }

    public static List<String> getSelectedGodCards(){
        return selectedGodCards;
    }

    public static void addSelectedGodCard(String selectedGodCard){
        Database.selectedGodCards.add(selectedGodCard);
    }

    public static void removeSelectedGodCard(String selectedGodCard){
        Database.selectedGodCards.remove(selectedGodCard);
    }

    public static String removeSelectedGodCard(){
        return selectedGodCards.remove(0);
    }

    public static ArrayList<String> getMatchCards(){
        return matchCards;
    }

    public static void setMatchCards(ArrayList<String> matchCards){
        Database.matchCards = matchCards;
    }

    public static MatchState getMatchState(){
        return matchState;
    }

    public static void setMatchState(MatchState matchState){
        Database.matchState = matchState;
    }

    public static Map<Integer, String> getMatchPlayers(){
        return matchPlayers;
    }

    public static void setMatchPlayers(Map<Integer, String> matchPlayers){
        Database.matchPlayers = matchPlayers;
    }

    public static Map<Integer, String> getMatchColors(){
        return matchColors;
    }

    public static void setMatchColors(Map<Integer, String> matchColors){
        Database.matchColors = matchColors;
    }

    public static int getCurrentPlayer(){
        return currentPlayer;
    }

    public static void setCurrentPlayer(int currentPlayer){
        Database.currentPlayer = currentPlayer;
    }

    public static Map<Position, Cell> getBillboardStatus(){
        return billboardStatus;
    }

    public static void setBillboardStatus(Map<Position, Cell> billboardStatus){
        Database.billboardStatus = billboardStatus;
    }

    public static String getGodCard(){
        return godCard;
    }

    public static void setGodCard(String godCard){
        Database.godCard = godCard;
    }

    public static PlayerState getPlayerState(){
        return playerState;
    }

    public static void setPlayerState(PlayerState playerState){
        Database.playerState = playerState;
    }

    public static TurnState getTurnState(){
        return turnState;
    }

    public static void setTurnState(TurnState turnState){
        Database.turnState = turnState;
    }

    public static boolean isTerminateTurnAvailable(){
        return terminateTurnAvailable;
    }

    public static void setTerminateTurnAvailable(boolean terminateTurnAvailable){
        Database.terminateTurnAvailable = terminateTurnAvailable;
    }

    public static Map<Position, Boolean> getSpecialFunctionAvailable(){
        return specialFunctionAvailable;
    }

    public static void setSpecialFunctionAvailable(Map<Position, Boolean> specialFunctionAvailable){
        Database.specialFunctionAvailable = specialFunctionAvailable;
    }

    public static Set<Position> getPlacingAvailableCells(){
        return placingAvailableCells;
    }

    public static void setPlacingAvailableCells(Set<Position> placingAvailableCells){
        Database.placingAvailableCells = placingAvailableCells;
    }

    public static Position getStartingPosition(){
        return startingPosition;
    }

    public static void setStartingPosition(Position startingPosition){
        Database.startingPosition = startingPosition;
    }

    public static Position getEndPosition(){
        return endPosition;
    }

    public static void setEndPosition(Position endPosition){
        Database.endPosition = endPosition;
    }

    public static Map<Position, Set<Position>> getWorkersAvailableCells(){
        return workersAvailableCells;
    }

    public static void setWorkersAvailableCells(Map<Position, Set<Position>> workersAvailableCells){
        Database.workersAvailableCells = workersAvailableCells;
    }

    public static Set<Position> getInitializedPositions(){
        return initializedPositions;
    }



    public static void addInitializedPosition(Position initializedPosition){
        Database.initializedPositions.add(initializedPosition);
    }

    //############################################################################################//
    //############################################################################################//
    //############################################################################################//
    //############################################################################################//

    /**
     * Updates the database from the message, reading the standard data
     * @param messageEvent the received message, from which to read the message.
     */
    public static void updateStandardData(MessageEvent messageEvent){
        if (messageEvent.getPlayerID() != playerID && messageEvent.getPlayerID() != 0)
            playerID = messageEvent.getPlayerID();
        if (messageEvent.getMatchID() != matchID && messageEvent.getMatchID() != 0)
            matchID = messageEvent.getMatchID();


        if (messageEvent.getMatchState() != matchState && messageEvent.getMatchState() != null)
            matchState = messageEvent.getMatchState();
        if (messageEvent.getPlayerState() != playerState && messageEvent.getPlayerState() != null)
            playerState = messageEvent.getPlayerState();
        if (messageEvent.getTurnState() != turnState && messageEvent.getTurnState() != null)
            turnState = messageEvent.getTurnState();
        if (messageEvent.getMatchPlayers() != matchPlayers && messageEvent.getMatchPlayers() != null)
            matchPlayers = messageEvent.getMatchPlayers();
        if (messageEvent.getMatchColors() != matchColors && messageEvent.getMatchColors() != null)
            matchColors = messageEvent.getMatchColors();

        currentPlayer = messageEvent.getCurrentPlayer();
    }
}
