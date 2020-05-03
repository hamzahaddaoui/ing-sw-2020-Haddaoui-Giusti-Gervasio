package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.utilities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {
    View view ;
    Player player ;
    GameBoard gameBoard;

    MessageEvent messageEvent;
    Map<Integer, String> newMatchPlayers;
    Set<String> godCards;
    Set<String> selectedGodCards;
    Set<Position> placingAvailablePosition;
    Map<Position, Cell> billboardStatus ;
    Map<Position, Set<Position>> workersAvailableCells;
    Map<Position, Boolean> specialFunctionAvailable;

    @BeforeEach
    void setUp(){
        String ip = "127.0.0.1";
        view = new View();
        player = View.getPlayer();
        gameBoard = View.getGameBoard();
        messageEvent = new MessageEvent();
        godCards = new HashSet<>();
        selectedGodCards = new HashSet<>();
        placingAvailablePosition = new HashSet<>();
        billboardStatus = new HashMap<>();
        workersAvailableCells = new HashMap<Position, Set<Position>>();
        specialFunctionAvailable = new HashMap<>();

        godCards.add("APOLLO");
        godCards.add("PAN");
        godCards.add("ARTEMIS");
        godCards.add("PROMETHEUS");
        godCards.add("DEMETER");
        godCards.add("HEPHAESTUS");
        godCards.add("MINOTAUR");
        godCards.add("ATLAS");
        godCards.add("ATHENA");

        selectedGodCards.add("APOLLO");
        selectedGodCards.add("PAN");
        selectedGodCards.add("ATLAS");
    }

    @Test
    void updateData() {
        //NICKNAME
/*
        String nickname = "leo";
        player.setNickname(nickname);
        setMessageConfigNickError(messageEvent);
        view.updateData(messageEvent);
        View.setActive(false);

        assertDoesNotThrow(()-> view.fetching(messageEvent));

        //WAITING

        setMessageConfigWaiting(messageEvent);
        view.updateData(messageEvent);
        View.setActive(false);

        assertEquals(PlayerState.INITIALIZED,player.getPlayerState());
        assertEquals(MatchState.WAITING_FOR_PLAYERS,player.getMatchState());

        //GETTING_PLAYERS_NUM
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.GETTING_PLAYERS_NUM);
        view.updateData(messageEvent);
        View.setActive(false);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.GETTING_PLAYERS_NUM,player.getMatchState());

        messageEvent.setError(true);
        view.updateData(messageEvent);
        View.setActive(false);*/
    }

    @Test
    void fetchingAndMatchStateNone() {
        newMatchPlayers = new HashMap<>();
        newMatchPlayers.put(1,"SARA");
        newMatchPlayers.put(2,"KARL");
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.NONE);
        messageEvent.setTurnState(TurnState.IDLE);
        messageEvent.setMatchPlayers(newMatchPlayers);
        messageEvent.setCurrentPlayer(newMatchPlayers.keySet().stream().findFirst().get());

        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.NONE,player.getMatchState());
        assertEquals(TurnState.IDLE,player.getTurnState());
        assertTrue(player.getMatchPlayers() != null);
        assertTrue(player.getPlayer() != 0);
    }

    @Test
    void fetchingFinishedState() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.FINISHED);
        messageEvent.setTurnState(TurnState.IDLE);

        assertTrue(null == player.getPlayerState());
        assertTrue(null ==  player.getMatchState());
        assertTrue(null ==  player.getTurnState());

        assertThrows(NullPointerException.class,()->view.updateData(messageEvent));

    }

    @Test
    void fetchingWINoRLOSTState() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.WIN,MatchState.RUNNING);
        messageEvent.setTurnState(TurnState.IDLE);

        assertTrue(null == player.getPlayerState());
        assertTrue(null ==  player.getMatchState());
        assertTrue(null ==  player.getTurnState());

        assertThrows(NullPointerException.class,()->view.updateData(messageEvent));
    }

    @Test
    void fetchingAndInitSelectingGodCards() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.SELECTING_GOD_CARDS);
        messageEvent.setTurnState(TurnState.IDLE);
        messageEvent.setMatchCards(godCards);

        assertTrue(null == player.getPlayerState());
        assertTrue(null ==  player.getMatchState());
        assertTrue(null ==  player.getTurnState());
        assertTrue(gameBoard.getMatchCards().size() == 0);

        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.SELECTING_GOD_CARDS,player.getMatchState());
        assertEquals(TurnState.IDLE,player.getTurnState());
        assertTrue(gameBoard.getMatchCards().size() == 9);
    }

    @Test
    void fetchingAndInitSelectingSpecialCommands() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.SELECTING_SPECIAL_COMMAND);
        messageEvent.setTurnState(TurnState.IDLE);
        messageEvent.setMatchCards(selectedGodCards);

        assertTrue(null == player.getPlayerState());
        assertTrue(null ==  player.getMatchState());
        assertTrue(null ==  player.getTurnState());
        assertTrue(gameBoard.getMatchCards().size() == 0);

        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.SELECTING_SPECIAL_COMMAND,player.getMatchState());
        assertEquals(TurnState.IDLE,player.getTurnState());
        assertTrue(gameBoard.getSelectedGodCards().size() == 3);
    }

    private void setCells() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                placingAvailablePosition.add(new Position(x,y));
    }

    private void setBillboardStatus() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                billboardStatus.put(new Position(x,y),new Cell());
    }

    private  void setWorkersAvailableCells(){
        Set<Position> worker1Cells = new HashSet<>();
        worker1Cells.add(new Position(0,0));
        worker1Cells.add(new Position(1,0));
        worker1Cells.add(new Position(2,0));
        worker1Cells.add(new Position(0,1));
        worker1Cells.add(new Position(2,1));
        worker1Cells.add(new Position(1,2));
        worker1Cells.add(new Position(0,2));
        worker1Cells.add(new Position(2,2));

        workersAvailableCells.put(new Position(1,1),worker1Cells);

        Set<Position> worker2Cells = new HashSet<>();
        worker2Cells.add(new Position(2,2));
        worker2Cells.add(new Position(3,2));
        worker2Cells.add(new Position(4,2));
        worker2Cells.add(new Position(2,3));
        worker2Cells.add(new Position(4,3));
        worker2Cells.add(new Position(2,4));
        worker2Cells.add(new Position(3,4));
        worker2Cells.add(new Position(4,4));

        workersAvailableCells.put(new Position(3,3),worker2Cells);
    }

    private  void setSpecialFunction(){
        specialFunctionAvailable
                .put(workersAvailableCells
                        .keySet()
                        .stream()
                        .filter(w -> !specialFunctionAvailable.keySet().contains(w))
                        .findAny()
                        .get(),false);

        specialFunctionAvailable
                .put(workersAvailableCells
                        .keySet()
                        .stream()
                        .filter(w -> !specialFunctionAvailable.keySet().contains(w))
                        .findAny()
                        .get(),true);
    }

    @Test
    void fetchingPlacingStateAndInitPlacingState() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.PLACING_WORKERS);
        messageEvent.setTurnState(TurnState.IDLE);
        setBillboardStatus();
        messageEvent.setBillboardStatus(billboardStatus);
        setCells();
        messageEvent.setAvailablePlacingCells(placingAvailablePosition);

        assertTrue(null == player.getPlayerState());
        assertTrue(null ==  player.getMatchState());
        assertTrue(null ==  player.getTurnState());
        assertTrue(gameBoard.getPlacingAvailableCells().size() == 0);
        assertTrue(gameBoard.getBillboardStatus().size() == 0);

        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.PLACING_WORKERS,player.getMatchState());
        assertEquals(TurnState.IDLE,player.getTurnState());
        assertTrue(gameBoard.getPlacingAvailableCells().size() != 0);
        assertTrue(gameBoard.getBillboardStatus().size() != 0);

    }

    @Test
    void fetchingRunningAndInitRunning() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.RUNNING);
        messageEvent.setTurnState(TurnState.IDLE);
        setWorkersAvailableCells();
        messageEvent.setWorkersAvailableCells(workersAvailableCells);
        setBillboardStatus();
        setSpecialFunction();
        messageEvent.setBillboardStatus(billboardStatus);
        messageEvent.setTerminateTurnAvailable(true);
        messageEvent.setSpecialFunctionAvailable(specialFunctionAvailable);

        assertNull(player.getPlayerState());
        assertNull(player.getMatchState());
        assertNull(player.getTurnState());
        assertEquals(0, gameBoard.getWorkersAvailableCells().size());
        assertEquals(0, gameBoard.getWorkersPositions().size());
        assertEquals(0, gameBoard.getBillboardStatus().size());
        assertEquals(0, player.getSpecialFunctionAvailable().size());
        assertFalse(player.isTerminateTurnAvailable());


        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.RUNNING,player.getMatchState());
        assertEquals(TurnState.IDLE,player.getTurnState());
        assertEquals(2, gameBoard.getWorkersAvailableCells().size());
        assertEquals(2, gameBoard.getWorkersPositions().size());
        assertTrue(0 != gameBoard.getBillboardStatus().size());
        assertEquals(2, player.getSpecialFunctionAvailable().size());
        assertTrue(player.isTerminateTurnAvailable());

        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.RUNNING);
        messageEvent.setTurnState(TurnState.MOVE);
        gameBoard.setStartingPosition(workersAvailableCells.keySet().stream().findFirst().get());
        setWorkersAvailableCells();

        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.RUNNING,player.getMatchState());
        assertEquals(TurnState.MOVE,player.getTurnState());
        assertEquals(2, gameBoard.getWorkersAvailableCells().size());
        assertEquals(2, gameBoard.getWorkersPositions().size());
        assertTrue(0 != gameBoard.getBillboardStatus().size());
    }

    @Test
    void fetchingGettingPlayersNumInitGettingPlayersNum() {
        View.setActive(true);
        setMessageConfigDataUpdate(messageEvent, PlayerState.ACTIVE,MatchState.GETTING_PLAYERS_NUM);
        messageEvent.setTurnState(TurnState.IDLE);

        assertTrue(null == player.getPlayerState());
        assertTrue(null ==  player.getMatchState());
        assertTrue(null ==  player.getTurnState());
        assertTrue(player.getPlayerNumber() == 0);

        view.updateData(messageEvent);

        assertEquals(PlayerState.ACTIVE,player.getPlayerState());
        assertEquals(MatchState.GETTING_PLAYERS_NUM,player.getMatchState());
        assertEquals(TurnState.IDLE,player.getTurnState());
        assertEquals(true, View.isActive());
    }

    private void setMessageConfigNickError(MessageEvent messageEvent){
        messageEvent.setInfo("Nickname not available.");
    }

    private void setMessageConfigDataUpdate(MessageEvent messageEvent, PlayerState playerState, MatchState matchState){
        messageEvent.setInfo("Match data update");
        messageEvent.setPlayerState(playerState);
        messageEvent.setMatchState(matchState);
    }

    private void setMessageConfigWaiting(MessageEvent messageEvent){
        messageEvent.setInfo("Wait for a match to start...");
        messageEvent.setPlayerState(PlayerState.INITIALIZED);
        messageEvent.setMatchState(MatchState.WAITING_FOR_PLAYERS);
    }

}