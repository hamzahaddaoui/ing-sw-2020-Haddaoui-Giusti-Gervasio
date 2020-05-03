package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player{

    private String nickname;
    private PlayerState playerState;
    private MatchState matchState;
    private TurnState turnState;

    private int playerNumber;
    private String GodCard;

    private Map<Integer, String> matchPlayers;
    private int currentPlayer;
    private boolean terminateTurnAvailable;
    private Map<Position, Boolean> specialFunctionAvailable;

    public Player(){
        playerState = null;
        matchState = null;
        turnState = null;
        matchPlayers = new HashMap<>();
        specialFunctionAvailable = new HashMap<>();
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
