package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.utilities.*;

import java.util.*;

public class DataBase {

    static private ControlState controlState = new NotInitialized();
    static private boolean activeInput = true;
    static private boolean messageReady;

    static private String nickname;
    static private PlayerState playerState;
    static private MatchState matchState;
    static private TurnState turnState;

    static private int playerNumber;
    static private String godCard;

    static private Map<Integer, String> matchPlayers;
    static private int currentPlayer;
    static private boolean terminateTurnAvailable;
    static private Map<Position, Boolean> specialFunctionAvailable;

    static private HashSet<String> selectedGodCards = new HashSet<>();    // usate per la Selection Special Command
    static private ArrayList<String> matchCards;           //date dal Server, usate per la Selecting God Card

    static private Map<Position, Cell> billboardStatus;
    static private Map<Position, Set<Position>> workersAvailableCells;
    static private Set<Position> placingAvailableCells;

    static private Position startingPosition;

    static public void resetDataBase() {
        selectedGodCards = new HashSet<>();
        matchCards = new ArrayList<>();
        billboardStatus = new HashMap<>();
        workersAvailableCells = new HashMap<>();
        placingAvailableCells = new HashSet<>();
        startingPosition = null;
        playerState = null;
        matchState = null;
        turnState = null;
        controlState = new NotInitialized();
        godCard = null;
        playerNumber = 0;
        matchPlayers = new HashMap<>();
        specialFunctionAvailable = new HashMap<>();
        activeInput = true;
        controlState = new NotInitialized();
    }

    public static void setActiveInput(boolean actIn) {
        activeInput = actIn;
    }

    public static void setMessageReady(boolean msgR) {
        messageReady = msgR;
    }

    public static boolean isActiveInput() {return activeInput;}

    public static boolean isMessageReady() {return messageReady;}

    static public void setPlayerState(PlayerState newPlayerState) {
        playerState = newPlayerState;
    }

    static public void setMatchState(MatchState newMatchState) {
        matchState = newMatchState;
    }

    static public TurnState getTurnState() {
        return turnState;
    }

    static public void setTurnState(TurnState newTurnState) {
        turnState = newTurnState;
    }

    static public void setMatchPlayers(Map<Integer, String> newMatchPlayers) {
        matchPlayers = newMatchPlayers;
    }

    static public void setTerminateTurnAvailable(boolean newTerminateTurnAvailable) {
        terminateTurnAvailable = newTerminateTurnAvailable;
    }

    static public PlayerState getPlayerState() {
        return playerState;
    }

    static public MatchState getMatchState() {
        return matchState;
    }

    static public int getPlayerNumber() {
        return playerNumber;
    }

    static public boolean isTerminateTurnAvailable() {
        return terminateTurnAvailable;
    }

    static public boolean isSpecialFunctionAvailable(Position position) {
        return specialFunctionAvailable.get(position);
    }

    static public Map<Position, Boolean> getSpecialFunctionAvailable() {
        return specialFunctionAvailable;
    }

    static public void setPlayerNumber(int selectedPlayersNum) {
        playerNumber = selectedPlayersNum;
    }

    static public void setSpecialFunctionAvailable(Map<Position, Boolean> modelMap) {
        specialFunctionAvailable = modelMap;
    }

    static public void setNickname(String newNickname) {
        nickname = newNickname;
    }

    static public String getNickname() {
        return nickname;
    }

    static public Map<Integer, String> getMatchPlayers() {
        return matchPlayers;
    }

    static public int getPlayer() {
        return currentPlayer;
    }

    static public void setPlayer(int player) {
        currentPlayer = player;
    }

    static public ControlState getControlState() {
        return controlState;
    }

    static public void setControlState(ControlState state) {
        controlState = state;
    }

    static public void setCurrentPlayer(int player) {
        currentPlayer = player;
    }

    static public String getGodCard() {
        return godCard;
    }

    static public void setGodCard(String card) {
        godCard = card;
    }

    static public void setMatchCards(Set<String> godCards) {
        matchCards = new ArrayList<>(godCards);
    }

    static public void setPlacingAvailableCells(Set<Position> newPlacingAvailableCells) {
        placingAvailableCells = newPlacingAvailableCells;
    }

    static public Map<Position, Set<Position>> getWorkersAvailableCells() {
        return workersAvailableCells;
    }

    static public void setWorkersAvailableCells(Map<Position, Set<Position>> newWorkersAvailableCells) {
        workersAvailableCells = newWorkersAvailableCells;
    }

    static public ArrayList<String> getMatchCards() {
        return matchCards;
    }

    static public Set<Position> getPlacingAvailableCells() {
        return placingAvailableCells;
    }

    static public Position getStartingPosition() {
        return startingPosition;
    }

    static public HashSet<String> getSelectedGodCards() {
        return selectedGodCards;
    }

    static public void setBillboardStatus(Map<Position, Cell> newBillboardStatus) {
        billboardStatus = newBillboardStatus;
    }

    static public void setStartingPosition(Position position) {
        startingPosition = position;
    }

    static public Map<Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    static public Set<Position> getWorkersAvailableCells(Position position) {
        return workersAvailableCells.get(position);
    }

    static public Set<Position> getWorkersPositions() {
        return workersAvailableCells.keySet();
    }

    static public boolean isWorkerPresent(Position position) {
        return workersAvailableCells.containsKey(position);
    }

    static public void setSelectedGodCards(Set<String> godCards) {
        selectedGodCards = new HashSet<>(godCards);
    }

    /**
     * It fetches the dates that are always sent from the Message Event
     *
     * @param messageEvent  is the message from the NetWorkHandler
     */
    static public void updateStandardData(MessageEvent messageEvent) {
        if (messageEvent.getMatchState() != matchState && messageEvent.getMatchState() != null)
            matchState = messageEvent.getMatchState();
        if (messageEvent.getPlayerState() != playerState && messageEvent.getPlayerState() != null)
            playerState = messageEvent.getPlayerState();
        if (messageEvent.getTurnState() != turnState && messageEvent.getTurnState() != null)
            turnState = messageEvent.getTurnState();
        if (messageEvent.getMatchPlayers() != matchPlayers && messageEvent.getMatchPlayers() != null)
            matchPlayers = messageEvent.getMatchPlayers();
        if ((MatchState.SELECTING_SPECIAL_COMMAND != matchState || currentPlayer == 0))
            currentPlayer = messageEvent.getCurrentPlayer();
    }

    /**
     * Depending on Database dates, it compute the next Control state
     */
    static public void updateControllerState() {
        if (nickname == null && controlState.getClass() != NotInitialized.class) {
            controlState = new NotInitialized();
        }
        switch (matchState) {
            case GETTING_PLAYERS_NUM:
                if (controlState.getClass() != GettingPlayersNum.class)
                    controlState = new GettingPlayersNum();
                break;
            case WAITING_FOR_PLAYERS:
                if (controlState.getClass() != WaitingForPlayers.class)
                    controlState = new WaitingForPlayers();
                break;
            case SELECTING_GOD_CARDS:
                if (controlState.getClass() != SelectingGodCards.class)
                    controlState = new SelectingGodCards();
                break;
            case SELECTING_SPECIAL_COMMAND:
                if (controlState.getClass() != SelectingSpecialCommand.class)
                    controlState = new SelectingSpecialCommand();
                break;
            case PLACING_WORKERS:
                if (controlState.getClass() != PlacingWorkers.class)
                    controlState = new PlacingWorkers();
                break;
            case RUNNING:
                if (controlState.getClass() != Running.class)
                    controlState = new Running();
                break;
            case FINISHED:
                controlState = new NotInitialized();
                break;
            default:
                controlState = new WaitingList();
        }
    }

}