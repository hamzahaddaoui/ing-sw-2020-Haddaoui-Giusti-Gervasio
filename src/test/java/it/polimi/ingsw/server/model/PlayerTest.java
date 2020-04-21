package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player p1, p2, p3;
    Match match;

    @BeforeEach
    void setUp(){
        p1 = new Player(1,"Pippo");
        p2 = new Player(2,"Pluto");
        p3 = new Player(3,"Minnie");
    }

    @Test
    void testInitialConfig(){
        assertEquals(1, p1.getID());
        assertEquals("Pippo", p1.getNickname());
        assertNull(p1.getMatch());
        assertNull(p1.getCommands());
        assertEquals(Collections.emptySet(), p1.getWorkers());
        assertNull(p1.getCurrentWorker());
        assertEquals(PlayerState.INITIALIZED, p1.getPlayerState());
        assertEquals(TurnState.IDLE, p1.getTurnState());
        assertFalse(p1.hasSelectedCard());
        assertFalse(p1.hasPlacedWorkers());
        assertFalse(p1.hasSpecialFunction());
        assertFalse(p1.hasFinished());
        assertFalse(p1.isTerminateTurnAvailable());
        assertFalse(p1.isSpecialFunctionAvailable());
    }


    @Test
    void testMatchCreator(){
        match = new Match(2,p1);
        assertEquals(match ,p1.getMatch());
        assertEquals(PlayerState.ACTIVE, p1.getPlayerState());
        assertEquals(TurnState.IDLE, p1.getTurnState());
    }

    @Test
    void testSelectingCard(){
        testMatchCreator();
        Set<GodCards> cards = new HashSet<GodCards>();
        cards.add(GodCards.Apollo);
        cards.add(GodCards.Artemis);
        match.setCards(cards);
        p1.setCommands(GodCards.Apollo);

    }
}