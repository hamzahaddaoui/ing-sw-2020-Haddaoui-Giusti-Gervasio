package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.server.model.GameModel.*;
import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {


    @Test
    void initialSetup(){
        assertEquals(0 , getPlayersWaitingListSize());
        assertEquals(0 , getNotInitMatchesListSize());
        assertEquals(0 , getInitMatchesListSize());

        assertTrue(isNickAvailable("Hamza"));
    }


    @Test
    void matchCreation(){
        assertEquals(0,createMatch(3));
        int pid = createPlayer("Hamza");
        assertFalse(isNickAvailable("Hamza"));

        int pid2 = createPlayer("lol");
        assertFalse(isNickAvailable("lol"));

        int pid3 = createPlayer("pippo");
        assertFalse(isNickAvailable("pippo"));

        int mid = createMatch(pid);
        addPlayerToWaitingList(pid2);
        addPlayerToWaitingList(pid3);

        assertEquals(1,pid);
        assertEquals(1,mid);
        assertEquals(2 , getPlayersWaitingListSize());

        assertEquals(1 , getNotInitMatchesListSize());

        assertNull(getInitMatchID());

        setMatchPlayersNum(mid, 3);

        assertEquals(mid, getInitMatchID());

        assertEquals(0 , getNotInitMatchesListSize());
        assertEquals(1 , getInitMatchesListSize());

        assertEquals(pid2, unstackPlayer());
        addPlayerToMatch(mid, pid2);
        assertEquals(pid3, unstackPlayer());
        addPlayerToMatch(mid, pid3);

        assertEquals(0 , getPlayersWaitingListSize());

        assertTrue(isNumReached(mid));
        startMatch(mid);

        assertTrue(isNickAvailable("hamza"));
        assertTrue(isNickAvailable("lol"));
        assertTrue(isNickAvailable("pippo"));

    }
}