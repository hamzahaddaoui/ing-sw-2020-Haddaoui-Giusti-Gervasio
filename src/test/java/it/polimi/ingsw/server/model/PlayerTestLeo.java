package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.decorators.*;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.descriptor.FileSystemSource;

import java.awt.*;
import java.net.SocketOption;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTestLeo {
    Player p1, p2, p3;
    Player p;
    Match match;

    @BeforeEach
    void setUp(){
        p1 = new Player(11,"apo");
        p2 = new Player(22,"art");
        p3 = new Player(33,"dem");
    }
    
    @Test
    void testInitialConfig(){
        assertEquals(11, p1.getID());
        assertEquals("apo", p1.toString());
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

        cards.add(GodCards.Apollo);
        cards.add(GodCards.Artemis);
        cards.add(GodCards.Demeter);

        match.setCards(cards);
        match.nextState();

        p1.setCommands(GodCards.Apollo);
        p2.setCommands(GodCards.Artemis);
        p3.setCommands(GodCards.Demeter);

        assertTrue(p1.getCommands() instanceof ApolloDecorator);
        assertTrue(p2.getCommands() instanceof ArtemisDecorator);
        assertTrue(p3.getCommands() instanceof DemeterDecorator);

        assertEquals(Collections.emptyMap(), p1.getWorkersAvailableCells());
        assertEquals(match.getBillboard().getCells().keySet(), p1.getPlacingAvailableCells());
        match.nextState();
    }
    
    @Test
    void testPlacing(){
        testSelectingCard();
        //APOLLO -> EXCHANGE POSITION
        p1.setWorker(new Position(0,0));
        p1.setWorker(new Position(0,1));
        assertTrue(p1.hasPlacedWorkers());
        //ARTEMIS -> MOVE A SECOND TIME
        p2.setWorker(new Position(2,3));
        p2.setWorker(new Position(2,4));
        assertTrue(p2.hasPlacedWorkers());
        //DEMETER -> BUILD A SECOND TIME
        p3.setWorker(new Position(4,3));
        p3.setWorker(new Position(4,4));
        assertTrue(p3.hasPlacedWorkers());
        match.nextState();
    }

    @Test  //simulation of match (6 rounds)
    void testMatch(){
        Position worker, build;
        testPlacing();
        p1.setPlayerState();

        System.out.println("\n-----------------------------------------------------------------------------" +
                "\n-----------------------------first ROUND------------------------------------" +
                "\n-----------------------------------------------------------------------------");
        {
            //---------turno di APOLLO------------------------------------------------------
            p = p1;
            System.out.println("TURNO DI " + p.getCommands().getClass() +"\n\n");
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(0, 0);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - moves from x=0 y=0 to x=1 y=1\n\n");
            worker = new Position(1, 1);
            p.playerAction(worker);
            match.checkPlayers();

            showAvailablePositionsMove(p);

            //COSTRUISCO
            build = new Position(0, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - builds a block in x=0 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));

            showAvailablePositionsBuild(p);

            //---------turno di ARTEMIS------------------------------------------------------
            p = p2;
            System.out.println("TURNO DI " + p.getCommands().getClass() +"\n\n");
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 3);
            p.setCurrentWorker(worker);
            showAvailablePositionsMove(p);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=2 y=3 to x=2 y=2\n\n");
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            worker = new Position(2, 2);
            p.playerAction(worker);
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            match.checkPlayers();
            showAvailablePositionsMove(p);

            //COSTRUISCO
            build = new Position(2, 1);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - builds a block in x=2 y=1\n\n");
            showAvailablePositionsBuild(p);
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));

            //---------turno di DEMETER------------------------------------------------------
            p = p3;
            System.out.println("TURNO DI " + p.getCommands().getClass() +"\n\n");
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(4, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - moves from x=4 y=4 to x=3 y=4\n\n");
            showAvailablePositionsMove(p);
            worker = new Position(3, 4);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=3\n\n");
            showAvailablePositionsBuild(p);
            p.playerAction(build);
            match.checkPlayers();
            showAvailablePositionsBuild(p);

            build = new Position(2, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=3\n\n");
            showAvailablePositionsBuild(p);
            p.playerAction(build);
            match.checkPlayers();
            showAvailablePositionsBuild(p);

            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));

            //FINE
        }

        System.out.println("\n-----------------------------------------------------------------------------" +
                "\n-----------------------------second ROUND------------------------------------" +
                "\n-----------------------------------------------------------------------------");
        {
            //---------turno di APOLLO------------------------------------------------------
            p = p1;
            System.out.println("TURNO DI " + p.getCommands().getClass() +"\n\n");
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(1, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - moves from x=1 y=1 to x=2 y=2 (EXCHANGE POSITION)\n\n");
            showAvailablePositionsMove(p);
            worker = new Position(2, 2);
            p.playerAction(worker);
            showAvailablePositionsMove(p);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 1);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - builds a block in x=3 y=1\n\n");
            showAvailablePositionsBuild(p);
            p.playerAction(build);
            match.checkPlayers();
            showAvailablePositionsBuild(p);
            //FINE
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));

            //---------turno di ARTEMIS------------------------------------------------------
            p = p2;
            System.out.println("TURNO DI " + p.getCommands().getClass() +"\n\n");
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(1, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=1 y=1 to x=2 y=1\n\n");
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            showAvailablePositionsMove(p);
            worker = new Position(2, 1);
            System.out.println("TURNO : " + p.getTurnState() + "\n\n");
            p.playerAction(worker);
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            showAvailablePositionsMove(p);
            match.checkPlayers();
            System.out.println("TURNO : " + p.getTurnState() + "\n\n");

            //MI MUOVO
            p.setUnsetSpecialFunction(true);
            showAvailablePositionsMove(p);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=2 y=1 to x=3 y=1\n\n");
            worker = new Position(3, 1);
            p.playerAction(worker);
            System.out.println("TURNO : " + p.getTurnState() + "\n\n");
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            match.checkPlayers();
            showAvailablePositionsMove(p);

            showAvailablePositionsBuild(p);
            //COSTRUISCO
            build = new Position(2, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - builds a block in x=2 y=1\n\n");
            p.playerAction(build);
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            match.checkPlayers();
            //FINE

            //---------turno di DEMETER------------------------------------------------------
            p = p3;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(3, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - moves from x=3 y=4 to x=2 y=3\n\n");
            worker = new Position(2, 3);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 4);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=4\n\n");
            p.playerAction(build);
            match.checkPlayers();

            build = new Position(3, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();

            //FINE
        }
        System.out.println("\n-----------------------------------------------------------------------------" +
                "\n-----------------------------third ROUND------------------------------------" +
                "\n-----------------------------------------------------------------------------");
        {
            //---------turno di APOLLO------------------------------------------------------
            p = p1;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 2);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - moves from x=2 y=2 to x=3 y=1\n\n");
            worker = new Position(3, 1);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(2, 1);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - builds a block in x=2 y=1\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di ARTEMIS------------------------------------------------------
            p = p2;
            System.out.println(getBillboardStat());
            System.out.println(p.getTurnState());
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            showAvailablePositionsMove(p);
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=2 y=4 to x=3 y=4\n\n");
            worker = new Position(3,4);
            p.playerAction(worker);
            System.out.println(p.getTurnState());
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            showAvailablePositionsMove(p);
            match.checkPlayers();

            //MI MUOVO
            p.setUnsetSpecialFunction(true);
            System.out.println(p.getTurnState());
            //p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=2 y=1 to x=3 y=3\n\n");
            worker = new Position(3, 3);
            p.playerAction(worker);
            match.checkPlayers();
            p.getWorkers().stream().forEach(worker1 -> System.out.println(worker1.getPosition()));

            //COSTRUISCO
            build = new Position(3,2);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - builds a block in x=3 y=2\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di DEMETER------------------------------------------------------
            p = p3;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(4, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - moves from x=4 y=3 to x=4 y=4\n\n");
            worker = new Position(4, 4);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 4);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=4\n\n");
            p.playerAction(build);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(4, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=4 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();

            //FINE
        }

        System.out.println("\n-----------------------------------------------------------------------------" +
                "\n-----------------------------fourth ROUND------------------------------------" +
                "\n-----------------------------------------------------------------------------");
        {
            //---------turno di APOLLO------------------------------------------------------
            p = p1;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(0, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - moves from x=0 y=1 to x=0 y=0\n\n");
            worker = new Position(0, 0);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - builds a block in x=1 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di ARTEMIS------------------------------------------------------
            p = p2;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 2);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=2 y=2 to x=1 y=3\n\n");
            worker = new Position(1,3);
            p.playerAction(worker);
            match.checkPlayers();

            //MI MUOVO
            p.setUnsetSpecialFunction(true);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=2 y=1 to x=2 y=4\n\n");
            worker = new Position(2, 4);
            p.playerAction(worker);

            //COSTRUISCO
            build = new Position(3,4);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - builds a block in x=3 y=4\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di DEMETER------------------------------------------------------
            p = p3;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(4, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - moves from x=4 y=4 to x=4 y=3\n\n");
            worker = new Position(4, 3);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 4);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=4\n\n");
            p.playerAction(build);
            match.checkPlayers();

            p.setHasFinished();
            match.checkPlayers();
            //FINE
        }

        System.out.println("\n-----------------------------------------------------------------------------" +
                "\n-----------------------------fifth ROUND------------------------------------" +
                "\n-----------------------------------------------------------------------------");
        {
            //---------turno di APOLLO------------------------------------------------------
            p = p1;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(0, 0);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - moves from x=0 y=0 to x=1 y=0\n\n");
            worker = new Position(1, 0);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(0, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - builds a block in x=0 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di ARTEMIS------------------------------------------------------
            p = p2;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(3, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=3 y=3 to x=2 y=2\n\n");
            worker = new Position(2, 2);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1, 2);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - builds a block in x=1 y=2\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di DEMETER------------------------------------------------------
            p = p3;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(4, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - moves from x=4 y=3 to x=4 y=2\n\n");
            worker = new Position(4, 2);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 2);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=2\n\n");
            p.playerAction(build);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("DEMETER - builds a block in x=3 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();

            //FINE
        }

        System.out.println("\n-----------------------------------------------------------------------------" +
                "\n-----------------------------sixth ROUND------------------------------------" +
                "\n-----------------------------------------------------------------------------");
        {
            //---------turno di APOLLO------------------------------------------------------
            p = p1;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(3, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - moves from x=3 y=1 to x=2 y=2\n\n");
            worker = new Position(2, 2);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1, 2);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("APOLLO - builds a block in x=1 y=2\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di ARTEMIS------------------------------------------------------
            p = p2;
            System.out.println(getBillboardStat());
            //INIZIO TURNO - MI MUOVO
            worker = new Position(3, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=3 y=1 to x=3 y=2\n\n");
            worker = new Position(3,2);
            p.playerAction(worker);
            match.checkPlayers();

            //MI MUOVO
            p.setUnsetSpecialFunction(true);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("ARTEMIS - moves from x=3 y=2 to x=3 y=3\n\n");
            worker = new Position(3, 3);
            p.playerAction(worker);

            if (match.checkPlayers()){
                System.out.println("MATCH FINISHED - WINNER IS "+p.toString());
            }
        }

    }
    
    String getBillboardStat(){
        StringBuilder output = new StringBuilder();
        match.getBillboard()
                .getCells()
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> output
                        .append(match.getBillboard().getPlayer(position)==0 ? "[ ]": (match.getPlayerNick(match.getBillboard().getPlayer(position))))
                        .append((position.getY()==4) ? "\n" : " "));

        output.append("\n");

        match.getBillboard()
                .getCells()
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> output
                        .append(match.getBillboard().getDome(position) ? "[D]" : "["+match.getBillboard().getTowerHeight(position)+"]")
                        .append((position.getY()==4) ? "\n" : " "));
        return output.toString();
    }

    String getBillboardStat(Set<Position> cells){
        StringBuilder output = new StringBuilder();
        match.getBillboard()
                .getCells()
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> output
                        .append(cells.contains(position) ? "\u2B1B" : "")
                        .append(!(p.getCurrentWorker().getPosition().equals(position)) && !cells.contains(position) ? "\u2B1C" : "")
                        .append((p.getCurrentWorker().getPosition().equals(position)) ? "\u2705" : "")
                        .append((position.getY()==4) ? "\n" : " "));

        return output.toString();
    }

    private void showAvailablePositionsMove(Player p){
        p.getWorkers().stream().forEach(worker -> System.out.println(" *"+ worker.getAvailableCells(TurnState.MOVE) +"* "));
    }

    private void showAvailablePositionsBuild(Player p){
        p.getWorkers().stream().forEach(worker -> System.out.println(" *"+ worker.getAvailableCells(TurnState.BUILD) +"* "));
    }

}
    
