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

    private Boolean endTurn;
    private Boolean specialFunction;

    private ArrayList<Integer> position;

    private Map<Position, Cell> billboardStatus;
    private Map<Position, Set<Position>> workersAvailableCells;

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

    public String getGodCard(){
        return godCard;
    }

    public Boolean getEndTurn(){
        return endTurn;
    }

    public Boolean getSpecialFunction(){
        return specialFunction;
    }

    public ArrayList<Integer> getPosition(){
        return position;
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
