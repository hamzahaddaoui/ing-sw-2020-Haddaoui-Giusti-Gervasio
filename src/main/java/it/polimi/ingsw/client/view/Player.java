package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player{

    private ControlState controlState = new NotInitialized();

    private String nickname;
    private PlayerState playerState;
    private MatchState matchState;
    private TurnState turnState;

    private int playerNumber;
    private String godCard;

    private Map<Integer, String> matchPlayers;
    private int currentPlayer;
    private boolean terminateTurnAvailable;
    private Map<Position, Boolean> specialFunctionAvailable;

    public Player(){
        playerState = null;
        matchState = null;
        turnState = null;
        godCard = null;
        playerNumber = 0;
        matchPlayers = new HashMap<>();
        specialFunctionAvailable = new HashMap<>();
    }

    public void updateCurrentState(){
        if (getNickname() == null && controlState.getClass() != NotInitialized.class){
            setControlState(new NotInitialized());
        }
        switch (getMatchState()){
            case GETTING_PLAYERS_NUM:
                if (controlState.getClass() != GettingPlayersNum.class)
                    setControlState( new GettingPlayersNum());
            case WAITING_FOR_PLAYERS:
                if (controlState.getClass() != WaitingForPlayers.class)
                    setControlState( new WaitingForPlayers());
            case SELECTING_GOD_CARDS:
                if (controlState.getClass() != SelectingGodCards.class)
                    setControlState( new SelectingGodCards());
            case SELECTING_SPECIAL_COMMAND:
                if (controlState.getClass() != SelectingSpecialCommand.class)
                    setControlState( new SelectingSpecialCommand());
            case PLACING_WORKERS:
                if (controlState.getClass() != PlacingWorkers.class)
                    setControlState( new PlacingWorkers());
            case RUNNING:
                if (controlState.getClass() != Running.class)
                    setControlState( new Running());
            default:
                setControlState( new WaitingList());
        }
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

    public int getPlayerNumber() {
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

    public ControlState getControlState() {return controlState;}

    public void setControlState(ControlState state) {controlState = state;}

    public void setCurrentPlayer(int player) {currentPlayer = player;}

    public String getGodCard() {
        return godCard;
    }

    public void setGodCard(String godCard) {
        this.godCard = godCard;
    }
}
