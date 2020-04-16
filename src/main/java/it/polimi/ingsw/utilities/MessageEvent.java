package it.polimi.ingsw.utilities;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.ClientHandler;

import java.util.Map;
import java.util.Set;

public class MessageEvent {
    private transient ClientHandler clientHandler;
    private String msgType;
    //CONTROLLER_TO_CONTROLLER
    //MODEL_TO_VIEW
    //VIEW_TO_MODEL
    //CONTROLLER_TO_VIEW


    private Integer playerID; //0 - BROADCAST
    private Integer matchID;  //0 - BROADCAST

    //user to controller
    private String nickname;
    private Integer playersNum;
    private String godCard;
    private Set<String> godCards;
    private Boolean endTurn;
    private Boolean specialFunction;
    private Position startPosition;
    private Position endPosition;

    //controller to view
    private String matchState;
    private String playerState;
    private String turnState;
    private Boolean error;


    //view to model
    private Boolean requestUpdate;

    //model to view
    private Map<Position, Cell> billboardStatus;
    private Map<Integer, String> matchPlayers;
    private Set<String> matchCards;
    private Map<Position, Set<Position>> workersAvailableCells;
    private Set<Position> availablePlacingCells;


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

    public MessageEvent(String msgType, Integer playerID, Integer matchID, String playerState, String matchState){
        this.msgType = msgType;
        this.playerID = playerID;
        this.matchID = matchID;
        this.playerState = playerState;
        this.matchState = matchState;
    }

    public MessageEvent(String msgType, Integer matchID, String matchState){
        this.msgType = msgType;
        this.matchID = matchID;
        this.matchState = matchState;
    }

    public MessageEvent(Integer playerID, boolean error){
        this.playerID = playerID;
        this.error = error;
    }


    public ClientHandler getClientHandler(){
        return clientHandler;
    }

    public void setClientHandler(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
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

    public String getPlayerState(){
        return playerState;
    }

    public String getMatchState(){
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
