package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.state.ControlState;
import it.polimi.ingsw.client.controller.state.NotInitialized;
import it.polimi.ingsw.utilities.*;

import java.util.*;

public class DataBase {

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

    private HashSet<String> selectedGodCards ;    // usate per la Selection Special Command
    private ArrayList<String> matchCards;           //date dal Server, usate per la Selecting God Card

    private Map<Position, Cell> billboardStatus ;
    private Map<Position, Set<Position>> workersAvailableCells;
    private Set<Position> placingAvailableCells;

    private Position startingPosition;

    public DataBase(){
        selectedGodCards = new HashSet<>();
        matchCards = new ArrayList<>();
        billboardStatus = new HashMap<>();
        workersAvailableCells = new HashMap<>();
        placingAvailableCells = new HashSet<>();
        startingPosition = null;
        playerState = null;
        matchState = null;
        turnState = null;
        godCard = null;
        playerNumber = 0;
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

    public void setMatchCards(Set<String> godCards) {
        matchCards = new ArrayList<>(godCards);
    }

    public void setPlacingAvailableCells(Set<Position> newPlacingAvailableCells) {
        placingAvailableCells = newPlacingAvailableCells;
    }

    public Map<Position, Set<Position>> getWorkersAvailableCells() {
        return workersAvailableCells;
    }

    public void setWorkersAvailableCells(Map<Position, Set<Position>> newWorkersAvailableCells) {
        workersAvailableCells = newWorkersAvailableCells;
    }

    public ArrayList<String> getMatchCards() {
        return matchCards;
    }

    public Set<Position> getPlacingAvailableCells() {
        return placingAvailableCells;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public HashSet<String> getSelectedGodCards() {
        return selectedGodCards;
    }

    public void setBillboardStatus(Map<Position, Cell> newBillboardStatus) {
        billboardStatus = newBillboardStatus;
    }

    public void setStartingPosition(Position position) {
        startingPosition = position;
    }

    public Map<Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    public Set<Position> getWorkersAvailableCells(Position position) {
        return workersAvailableCells.get(position);
    }

    public Set<Position> getWorkersPositions() {
        return workersAvailableCells.keySet();
    }

    public boolean isWorkerPresent(Position position) {
        return workersAvailableCells.containsKey(position);
    }

    public void setSelectedGodCards (Set<String> godCards) {
        selectedGodCards = new HashSet<>(godCards);
    }

}
