package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {

    private Integer matchID;
    private Integer playerID;
    private String nickname;
    private PlayerState playerState=null;
    private MatchState matchState=null;
    private TurnState turnState=null;

    private ArrayList<Integer> coloredPlayersNum = new ArrayList<>();
    private Integer playersNum = null;

    private Map<Integer, String> matchPlayers = new HashMap<>();
    private boolean terminateTurnAvailable = false;
    private Map<Position, Boolean> specialFunctionAvailable = new HashMap<>();

    private boolean error = false;

    public void setError(boolean newError) {
        error = newError;
    }

    public void setMatchID(Integer newMatchID) {
        matchID = newMatchID;
    }

    public void setPlayerID(Integer newPlayerID) {
        playerID = newPlayerID;
    }

    public void setPlayerState(PlayerState newPlayerState) {
        playerState = newPlayerState;
    }

    public void setMatchState(MatchState newMatchState) {
        matchState = newMatchState;
    }

    public TurnState getTurnState() {
        return turnState;
    }

    public void setTurnState(TurnState newTurnState) {
        turnState = newTurnState;
    }

    public void setMatchPlayers(Map<Integer, String> newMatchPlayers) {
        matchPlayers = newMatchPlayers;
    }

    public void setTerminateTurnAvailable(boolean newTerminateTurnAvailable) {
        terminateTurnAvailable = newTerminateTurnAvailable;
    }

    public Integer getMatchID() {
        return matchID;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public ArrayList<Integer> getColoredPlayersNum() {
        return coloredPlayersNum;
    }

    public void setColoredPlayersNum(ArrayList<Integer> newColoredPlayersNum) {
        coloredPlayersNum = newColoredPlayersNum;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public Integer getPlayersNum() {
        return playersNum;
    }

    public boolean isTerminateTurnAvailable() {
        return terminateTurnAvailable;
    }

    public boolean isSpecialFunctionAvailable(Position position) {
        return specialFunctionAvailable.get(position);
    }

    public Map<Position, Boolean> getSpecialFunctionAvailable(){
        return specialFunctionAvailable;
    }

    public void setPlayersNum (int selectedPlayersNum) {
        playersNum = selectedPlayersNum;
    }

    public void setSpecialFunctionAvailable(Map<Position,Boolean> modelMap) {
        specialFunctionAvailable = modelMap;
    }

    public void setNickname(String newNickname) {
        nickname = newNickname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String newIp) {
        ip = newIp;
    }

    public String getNickname() {
        return nickname;
    }

    public Map<Integer, String> getMatchPlayers() {
        return matchPlayers;
    }
}
