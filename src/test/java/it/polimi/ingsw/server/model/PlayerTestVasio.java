package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.decorators.*;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerTestVasio {
    Player p1, p2, p3;
    Player p;
    Match match;

    @BeforeEach
    void setUp(){
        p1 = new Player(11,"pro");
        p2 = new Player(22,"pan");
        p3 = new Player(33,"min");
    }

    @Test
    void testInitialConfig(){
        assertEquals(11, p1.getID());
        assertEquals("pro", p1.toString());
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
        //assertTrue(p1.isSpecialFunctionAvailable().isEmpty());
    }

    @Test
    void testMatchCreator(){
        match = new Match(2, p1);
        match.setPlayersNum(3);
        match.nextState();

        assertEquals(match , p1.getMatch());
        assertEquals(PlayerState.ACTIVE, p1.getPlayerState());
        assertEquals(TurnState.IDLE, p1.getTurnState());
        match.addPlayer(p2);
        match.addPlayer(p3);
        match.nextState();
    }

    @Test
    void testSelectingCard(){
        testMatchCreator();
        Set<GodCards> cards = new HashSet<>();

        cards.add(GodCards.Prometheus);
        cards.add(GodCards.Pan);
        cards.add(GodCards.Minotaur);

        match.setCards(cards);
        match.nextState();

        p1.setCommands(GodCards.Prometheus);
        p2.setCommands(GodCards.Pan);
        p3.setCommands(GodCards.Minotaur);

        assertTrue(p1.getCommands() instanceof PrometheusDecorator);
        assertTrue(p2.getCommands() instanceof PanDecorator);
        assertTrue(p3.getCommands() instanceof MinotaurDecorator);

        assertTrue(p1.getWorkersAvailableCells().isEmpty());
        assertEquals(match.getBillboard().getCells().keySet(), p1.getPlacingAvailableCells());
        match.nextState();
    }

    @Test
    void testPlacing(){
        testSelectingCard();
        p1.setWorker(new Position(0,0));
        p1.setWorker(new Position(2,0));
        assertTrue(p1.hasPlacedWorkers());
        p2.setWorker(new Position(0,1));
        p2.setWorker(new Position(2,2));
        assertTrue(p2.hasPlacedWorkers());
        p3.setWorker(new Position(3,1));
        p3.setWorker(new Position(4,4));
        assertTrue(p3.hasPlacedWorkers());
        match.nextState();
    }

    @Test
    void testMatch(){
        Position worker, build;
        testPlacing();
        p1.setPlayerState();
        p = p1;
        System.out.println(getBillboardStat());
        worker = new Position(2, 0); p.setCurrentWorker(worker);
        p.setUnsetSpecialFunction(true);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        build = new Position(2, 1); p.playerAction(build);
        match.checkPlayers();
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(3, 0);
        p.playerAction(worker); match.checkPlayers();
        build = new Position(4,1); p.playerAction(build);
        match.checkPlayers();
        p = p2; System.out.println(getBillboardStat());
        worker = new Position(2,2); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(2,3); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(1,3); p.playerAction(build);
        match.checkPlayers();
        p = p3; System.out.println(getBillboardStat());
        worker = new Position(3,1); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(3,2); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(3,1); p.playerAction(build);
        match.checkPlayers();
        p = p1; System.out.println(getBillboardStat());
        worker = new Position(0,0); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        p.setUnsetSpecialFunction(true);
        build = new Position(1,0); p.playerAction(build);
        match.checkPlayers();
        worker = new Position(1,1); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(1,0); p.playerAction(build);
        match.checkPlayers();
        p = p2; System.out.println(getBillboardStat());
        worker = new Position(2,3); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(2,2); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(1,2); p.playerAction(build);
        match.checkPlayers();
        p = p3; System.out.println(getBillboardStat());
        worker = new Position(3,2); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(2,2); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(2,1); p.playerAction(build);
        match.checkPlayers();
        p = p1; System.out.println(getBillboardStat());
        worker = new Position(3,0); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(4,0); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(3,0); p.playerAction(worker);
        match.checkPlayers();
        p = p2; System.out.println(getBillboardStat());
        worker = new Position(0,1); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(0,0); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(0,1); p.playerAction(build);
        match.checkPlayers();
        p = p3; System.out.println(getBillboardStat());
        worker = new Position(2,2); p.setCurrentWorker(worker);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        worker = new Position(1,2); p.playerAction(worker);
        match.checkPlayers();
        build = new Position(2,2); p.playerAction(build);
        match.checkPlayers();
        p = p1; System.out.println(getBillboardStat());
        worker = new Position(1,1); p.setCurrentWorker(worker);
        p.setUnsetSpecialFunction(true);
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        build = new Position(1,0); p.playerAction(build);
        match.checkPlayers();
        worker = new Position(2,0); p.playerAction(worker);
        match.checkPlayers();
        System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
        /*assertTrue(match.getLosers().contains(p));
        p = p2; System.out.println(getBillboardStat());*/
    }

    String getBillboardStat(){
        StringBuilder output = new StringBuilder();
        match.getBillboard().getCells().keySet().stream().sorted().forEach(position -> output.append(match.getBillboard().getPlayer(position)==0 ? "[ ]": (match.getPlayerNick(match.getBillboard().getPlayer(position)))).append((position.getY()==4) ? "\n" : " "));
        output.append("\n");
        match.getBillboard().getCells().keySet().stream().sorted().forEach(position -> output.append(match.getBillboard().getDome(position) ? "[D]" : "["+match.getBillboard().getTowerHeight(position)+"]").append((position.getY()==4) ? "\n" : " "));
        return output.toString();
    }

    String getBillboardStat(Set<Position> cells){
        StringBuilder output = new StringBuilder();
        match.getBillboard().getCells().keySet().stream().sorted().forEach(position -> output.append(cells.contains(position) ? "\u2B1B" : "").append(!(p.getCurrentWorker().getPosition().equals(position)) && !cells.contains(position) ? "\u2B1C" : "").append((p.getCurrentWorker().getPosition().equals(position)) ? "\u2705" : "").append((position.getY()==4) ? "\n" : " "));
        return output.toString();
    }

}
