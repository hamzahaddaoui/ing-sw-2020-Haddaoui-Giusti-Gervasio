package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.model.GameModel.*;
import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {


    @Test
    void initialSetup(){
        assertEquals(0 , getPlayersWaitingListSize());
        assertEquals(0 , getNotInitMatchesListSize());
        assertEquals(0 , getInitMatchesListSize());

        //assertTrue(isNickAvailable("Hamza"));
    }


    @Test
    void matchCreation(){
        assertEquals(Set.of("Atlas", "Zeus", "Apollo", "Hephaestus", "Triton", "Prometheus", "Charon", "Eros", "Hestia", "Minotaur", "Demeter", "Artemis", "Pan", "Athena"), getGameCards());

        assertEquals(MatchState.NONE, getMatchState(3));

        assertEquals(0,createMatch(3));
        removePlayerWaitingList(4);
        removePlayerWaitingList(5);

        int pid = createPlayer("Hamza");


        int pid2 = createPlayer("pippo");
        addPlayerToWaitingList(pid2);
        assertEquals(0, createPlayer("pipPo"));

        removePlayerWaitingList(pid2);

        pid2 = createPlayer("lol");
        addPlayerToWaitingList(pid2);
        int pid3 = createPlayer("pippo");
        addPlayerToWaitingList(pid3);
        assertEquals(1,pid);

        assertEquals(0 , getInitMatchID());
        assertEquals(0 , getNotInitMatchesListSize());


        int mid = createMatch(pid);

        assertEquals(MatchState.GETTING_PLAYERS_NUM, getMatchState(mid));

        setMatchPlayersNum(mid, 3);

        assertEquals(mid, getInitMatchID());

        nextMatchState(mid);
        assertEquals(MatchState.WAITING_FOR_PLAYERS, getMatchState(mid));

        assertEquals(0 , getNotInitMatchesListSize());
        assertEquals(1 , getInitMatchesListSize());

        assertEquals(pid2, unstackPlayer());
        addPlayerToMatch(mid, pid2);
        assertEquals(pid3, unstackPlayer());
        addPlayerToMatch(mid, pid3);

        assertEquals(0 , getPlayersWaitingListSize());

        assertTrue(isNumReached(mid));
        startMatch(mid);

        nextMatchState(mid);
        assertEquals(MatchState.SELECTING_GOD_CARDS, getMatchState(mid));

        assertEquals(PlayerState.INITIALIZED, getPlayerState(mid, pid2));
        assertEquals(PlayerState.INITIALIZED, getPlayerState(mid, pid3));
        assertEquals(null, getPlayerState(2, 1));

        assertEquals(TurnState.IDLE, getPlayerTurn(mid, pid2));
        assertEquals(TurnState.IDLE, getPlayerTurn(mid, pid3));
        assertEquals(null, getPlayerTurn(2, 1));



        setMatchCards(mid, Set.of("Atlas","Pan", "Charon"));
        nextMatchState(mid);
        nextMatchTurn(mid);
        selectPlayerCard(mid, "Atlas");
        hasSelectedCard(mid);
        nextMatchTurn(mid);
        selectPlayerCard(mid, "Pan");
        hasSelectedCard(mid);
        nextMatchTurn(mid);
        selectPlayerCard(mid, "Charon");
        hasSelectedCard(mid);
        nextMatchState(mid);
        nextMatchTurn(mid);

        getMatchPlayers(mid);
        getMatchCards(mid);
        getMatchLosers(mid);
        getMatchColors(mid);
        getBillboardStatus(mid);


        getPlacingAvailableCells(mid);
        placeWorker(mid, new Position(0,0));
        placeWorker(mid, new Position(0,1));
        assertTrue(hasPlacedWorkers(mid));
        nextMatchTurn(mid);
        placeWorker(mid, new Position(1,0));
        placeWorker(mid, new Position(1,1));
        assertTrue(hasPlacedWorkers(mid));
        nextMatchTurn(mid);
        placeWorker(mid, new Position(2,0));
        placeWorker(mid, new Position(2,1));
        nextMatchTurn(mid);
        nextMatchState(mid);

        assertEquals(pid2, getCurrentPlayer(mid));
        isTerminateTurnAvailable(mid);
        isSpecialFunctionAvailable(mid);
        assertFalse(hasSpecialFunction(mid));
        getWorkersAvailableCells(mid);
        setUnsetSpecialFunction(mid, false);
        playerTurn(mid, new Position(0,1), new Position(0,2));

        setHasFinished(mid);

        getMatchInfo(mid);


        removeLosers(mid);
        deleteMatch(mid);






    }
}