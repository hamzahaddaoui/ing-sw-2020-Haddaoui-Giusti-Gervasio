package it.polimi.ingsw.utilities;

import java.util.Map;
import java.util.Set;

public class MessageEvent {
    private String msgType;
    //CONTROLLER_CHANGE_VIEW
    //MODEL_UPDATE_VIEW
    //CLIENTCONTROL_NOTIFY_SERVERCONTROL


    private Integer playerID; //0 - BROADCAST
    private Integer matchID;  //0 - BROADCAST

    private String nickname;
    private int playersNum;

    //la view, leggendo questi due valori, sa cosa deve mostrare
    private String playerState;
    private String matchState;
    private boolean error;

    private String godCard;
    private Set<String> godCards;

    private Boolean endTurn;
    private Boolean specialFunction;

    private Position startPosition;
    private Position endPosition;

    private Map<Position, Cell> billboardStatus;
    private Map<Position, Set<Position>> workersAvailableCells;
    private Map<Integer, String> matchPlayers;

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
        this.playerState = playerState;
        this.matchState = matchState;
    }

    public MessageEvent(Integer playerID, Integer matchID, boolean error){
        this.playerID = playerID;
        this.matchID = matchID;
        this.error = error;
    }

    public MessageEvent(String msgType, Integer matchID, Map<Position, Cell> billboardStatus, Map<Position, Set<Position>> workersAvailableCells){
        this.msgType = msgType;
        this.matchID = matchID;
        this.billboardStatus = billboardStatus;
        this.workersAvailableCells = workersAvailableCells;
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
}
