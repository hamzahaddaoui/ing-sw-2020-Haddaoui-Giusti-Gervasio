package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {

    private String nickname;
    private PlayerState playerState=null;
    private MatchState matchState=null;
    private TurnState turnState=null;

    private ArrayList<Integer> playersNum = new ArrayList<>();
    private int playerNumber;

    private Map<Integer, String> matchPlayers = new HashMap<>();
    private int currentPlayer;
    private boolean terminateTurnAvailable = false;
    private Map<Position, Boolean> specialFunctionAvailable = new HashMap<>();

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

    public ArrayList<Integer> getPlayersNum() {
        return playersNum;
    }

    public void setPlayersNum(ArrayList<Integer> newColoredPlayersNum) {
        playersNum = newColoredPlayersNum;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public Integer getPlayerNumber() {
        return playerNumber;
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

    public void setPlayerNumber (int selectedPlayersNum) {
        playerNumber = selectedPlayersNum;
    }

    public void setSpecialFunctionAvailable(Map<Position,Boolean> modelMap) {
        specialFunctionAvailable = modelMap;
    }

    public void setNickname(String newNickname) {
        nickname = newNickname;
    }

    public String getNickname() {
        return nickname;
    }

    public Map<Integer, String> getMatchPlayers() {
        return matchPlayers;
    }

    public int getPlayer() {
        return currentPlayer;
    }

    public void setPlayer(int player) {
        currentPlayer = player;
    }
}
