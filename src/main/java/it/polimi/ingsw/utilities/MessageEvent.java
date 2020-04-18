package it.polimi.ingsw.utilities;

import it.polimi.ingsw.server.ClientHandler;

import java.util.Map;
import java.util.Set;

public class MessageEvent {
    private transient ClientHandler clientHandler;

    private String msgType;
    //CONTROLLER_TO_CONTROLLER
    //CONTROLLER_TO_VIEW

    private Integer matchID;  //0 - BROADCAST
    private Integer playerID; //0 - BROADCAST


    //user to controller
    private String nickname;
    private Integer playersNum;
    private String godCard;
    private Set<String> godCards;

    private Set<Position> initializedPositions;
    private Position startPosition;
    private Position endPosition;

    private Boolean endTurn;
    private Boolean specialFunction;

    //controller to view
    private MatchState matchState;
    private PlayerState playerState;
    private TurnState turnState;
    private Boolean error;


    //model to view
    private Map<Position, Cell> billboardStatus;
    private Set<String> matchCards;

    private Map<Position, Set<Position>> workersAvailableCells;
    private Set<Position> availablePlacingCells;

    private Boolean terminateTurnAvailable;
    private Boolean specialFunctionAvailable;

    private Map<Integer, String> matchPlayers;
    private int activeMatches;
    private int playersConnected;


    //(billboardStatus, matchCards, matchPlayers, ac)


    public MessageEvent(Integer matchID, Integer playerID, MatchState matchState, PlayerState playerState, TurnState turnState, Set<String> matchCards, Map<Integer, String> matchPlayers, int activeMatches, int playersConnected){
        this.matchID = matchID;
        this.playerID = playerID;
        this.matchState = matchState;
        this.playerState = playerState;
        this.turnState = turnState;
        this.matchCards = matchCards;
        this.matchPlayers = matchPlayers;
        this.activeMatches = activeMatches;
        this.playersConnected = playersConnected;
    }

    public MessageEvent(Integer matchID, Integer playerID, MatchState matchState, PlayerState playerState, TurnState turnState, Map<Position, Cell> billboardStatus, Set<Position> availablePlacingCells, Map<Integer, String> matchPlayers, int activeMatches, int playersConnected){
        this.matchID = matchID;
        this.playerID = playerID;
        this.matchState = matchState;
        this.playerState = playerState;
        this.turnState = turnState;
        this.billboardStatus = billboardStatus;
        this.availablePlacingCells = availablePlacingCells;
        this.matchPlayers = matchPlayers;
        this.activeMatches = activeMatches;
        this.playersConnected = playersConnected;
    }

    public MessageEvent(Integer matchID, Integer playerID, MatchState matchState, PlayerState playerState, TurnState turnState, Map<Position, Cell> billboardStatus, Map<Position, Set<Position>> workersAvailableCells, Boolean terminateTurnAvailable, Boolean specialFunctionAvailable, Map<Integer, String> matchPlayers, int activeMatches, int playersConnected){
        this.matchID = matchID;
        this.playerID = playerID;
        this.matchState = matchState;
        this.playerState = playerState;
        this.turnState = turnState;
        this.billboardStatus = billboardStatus;
        this.workersAvailableCells = workersAvailableCells;
        this.terminateTurnAvailable = terminateTurnAvailable;
        this.specialFunctionAvailable = specialFunctionAvailable;
        this.matchPlayers = matchPlayers;
        this.activeMatches = activeMatches;
        this.playersConnected = playersConnected;
    }










    public MessageEvent(Integer matchID, Map<Position, Cell> billboardStatus, Set<String> matchCards, Map<Integer, String> matchPlayers, int activeMatches, int playersConnected){
        this.matchID = matchID;
        this.billboardStatus = billboardStatus;
        this.matchCards = matchCards;
        this.matchPlayers = matchPlayers;
        this.activeMatches = activeMatches;
        this.playersConnected = playersConnected;
    }

    public MessageEvent(Integer matchID, Integer playerID, MatchState matchState, PlayerState playerState, TurnState turnState, Map<Integer, String> matchPlayers){
        this.playerID = playerID;
        this.matchID = matchID;
        this.matchState = matchState;
        this.playerState = playerState;
        this.turnState = turnState;
        this.matchPlayers = matchPlayers;
    }

    public MessageEvent(Integer playerID, PlayerState playerState){
        this.playerID = playerID;
        this.playerState = playerState;
    }

    public MessageEvent(Integer matchID, Integer playerID, MatchState matchState, PlayerState playerState, TurnState turnState, Set<String> matchCards){
        this.playerID = playerID;
        this.matchID = matchID;
        this.matchState = matchState;
        this.playerState = playerState;
        this.turnState = turnState;
        this.matchCards = matchCards;
    }

    public MessageEvent(String msgType, Integer playerID, Integer matchID, Set<Position> availablePlacingCells, Map<Position, Set<Position>> workersAvailableCells){
        this.msgType = msgType;
        this.playerID = playerID;
        this.matchID = matchID;
        this.workersAvailableCells = workersAvailableCells;
        this.availablePlacingCells = availablePlacingCells;
    }

    public MessageEvent(String msgType, Integer matchID, Map<Position, Cell> billboardStatus, Map<Integer, String> matchPlayers, Set<String> matchCards){
        this.msgType = msgType;
        this.matchID = matchID;
        this.billboardStatus = billboardStatus;
        this.matchPlayers = matchPlayers;
        this.matchCards = matchCards;
    }

    public MessageEvent(String msgType, Integer playerID, Integer matchID, PlayerState playerState, MatchState matchState){
        this.msgType = msgType;
        this.playerID = playerID;
        this.matchID = matchID;
        this.playerState = playerState;
        this.matchState = matchState;
    }

    public MessageEvent(String msgType, Integer matchID, MatchState matchState){
        this.msgType = msgType;
        this.matchID = matchID;
        this.matchState = matchState;
    }

    public MessageEvent(Integer playerID, boolean error){
        this.playerID = playerID;
        this.error = error;
    }





    public void setClientHandler(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }





    public ClientHandler getClientHandler(){
        return clientHandler;
    }

    public void setPlayerID(Integer playerID){
        this.playerID = playerID;
    }

    public void setMatchID(Integer matchID){
        this.matchID = matchID;
    }

    public String getMsgType(){
        return msgType;
    }

    public Integer getPlayerID(){
        return playerID;
    }

    public Integer getMatchID(){
        return matchID;
    }

    public String getNickname(){
        return nickname;
    }

    public int getPlayersNum(){
        return playersNum;
    }

    public PlayerState getPlayerState(){
        return playerState;
    }

    public MatchState getMatchState(){
        return matchState;
    }

    public boolean isError(){
        return error;
    }

    public String getGodCard(){
        return godCard;
    }

    public Set<String> getGodCards(){
        return godCards;
    }

    public Boolean getEndTurn(){
        return endTurn;
    }

    public Boolean getSpecialFunction(){
        return specialFunction;
    }

    public Position getStartPosition(){
        return startPosition;
    }

    public Position getEndPosition(){
        return endPosition;
    }

    public Map<Position, Cell> getBillboardStatus(){
        return billboardStatus;
    }

    public Map<Position, Set<Position>> getWorkersAvailableCells(){
        return workersAvailableCells;
    }

    public Map<Integer, String> getMatchPlayers(){
        return matchPlayers;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition=startPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    public void setEndTurn(Boolean endTurn) {
        this.endTurn = endTurn;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSpecialFunction(Boolean specialFunction) {
        this.specialFunction =specialFunction;
    }

    public void setPlayersNum(Integer playersNum) {
        this.playersNum = playersNum;
    }

    public void setGodCards(Set<String> godCards) {
        this.godCards = godCards;
    }

    public void setGodCard(String godCard) {
        this.godCard = godCard;
    }
}
