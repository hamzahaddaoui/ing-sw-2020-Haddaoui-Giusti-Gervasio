package it.polimi.ingsw.utilities;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MessageEvent {
    private MessageType msgType; //vc, cv, mv

    private Integer playerID;
    private Integer matchID;

    private String nickname;
    private int playersNum;

    //la view, leggendo questi due valori, sa cosa deve mostrare
    private String playerState;
    private String matchState;
    //private boolean error;
    //private String errorType;

    private String godCard;
    private Set<String> godCards;

    private Boolean endTurn;
    private Boolean specialFunction;

    private Position startPosition;
    private Position endPosition;

    private Map<Position, Cell> billboardStatus;
    private Map<Position, Set<Position>> workersAvailableCells;
    private Map<Integer, String> matchPlayers;

    public MessageEvent(MessageType msgType, Integer playerID, Integer matchID, String playerState, String matchState){
        this.msgType = msgType;
        this.playerID = playerID;
        this.matchID = matchID;
        this.playerState = playerState;
        this.matchState = matchState;
    }

    public MessageEvent(MessageType msgType, Integer matchID, Map<Position, Cell> billboardStatus, Map<Position, Set<Position>> workersAvailableCells){
        this.msgType = msgType;
        this.matchID = matchID;
        this.billboardStatus = billboardStatus;
        this.workersAvailableCells = workersAvailableCells;
    }


    public MessageType getMsgType(){
        return msgType;
    }

    public Integer getPlayerID(){
        return playerID;
    }

    public Integer getMatchID(){
        return matchID;
    }

    public void setPlayerID(Integer playerID){
        this.playerID = playerID;
    }

    public void setMatchID(Integer matchID){
        this.matchID = matchID;
    }

    public String getNickname(){
        return nickname;
    }

    public String getPlayerState(){
        return playerState;
    }

    public String getMatchState(){
        return matchState;
    }

    public Boolean getEndTurn(){
        return endTurn;
    }

    public String getGodCard(){
        return godCard;
    }

    public Set<String> getGodCards(){
        return godCards;
    }

    public Boolean getSpecialFunction(){
        return specialFunction;
    }

    public Map<Position, Cell> getBillboardStatus(){
        return billboardStatus;
    }

    public Map<Position, Set<Position>> getWorkersAvailableCells(){
        return workersAvailableCells;
    }

    public int getPlayersNum(){
        return playersNum;
    }
}
