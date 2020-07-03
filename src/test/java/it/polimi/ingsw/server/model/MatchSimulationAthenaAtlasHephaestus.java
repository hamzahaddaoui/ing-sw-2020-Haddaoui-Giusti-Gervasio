package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.decorators.AthenaDecorator;
import it.polimi.ingsw.server.model.decorators.AtlasDecorator;
import it.polimi.ingsw.server.model.decorators.HephaestusDecorator;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/*
 * @author: hamzahaddaoui
 *
 * Test class of a match, from the beginning to the winning of a player, using three cards:
 * Apollo, Demeter and Hephaestus.
 */

class MatchSimulationAthenaAtlasHephaestus {


    Player p1, p2, p3;
    Player p;
    Match match;

    @BeforeEach
    void setUp(){
        p1 = new Player(11,"ath");
        p2 = new Player(22,"atl");
        p3 = new Player(33,"eph");
    }
    @Test
    void testInitialConfig(){
        assertEquals(11, p1.getID());
        assertEquals("ath", p1.toString());
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
        //assertFalse(p1.isSpecialFunctionAvailable());
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

        cards.add(GodCards.Athena);
        cards.add(GodCards.Atlas);
        cards.add(GodCards.Hephaestus);

        match.setCards(cards);
        match.nextState();

        p1.setCommands(GodCards.Athena);
        p2.setCommands(GodCards.Atlas);
        p3.setCommands(GodCards.Hephaestus);

        assertTrue(p1.getCommands() instanceof AthenaDecorator);
        assertTrue(p2.getCommands() instanceof AtlasDecorator);
        assertTrue(p3.getCommands() instanceof HephaestusDecorator);

        assertEquals(Collections.emptyMap(), p1.getWorkersAvailableCells());
        assertEquals(match.getBillboard().getCells().keySet(), p1.getPlacingAvailableCells());
        match.nextState();
    }
    @Test
    void testPlacing(){
        testSelectingCard();

        p1.setWorker(new Position(0,0));
        p1.setWorker(new Position(0,1));
        assertTrue(p1.hasPlacedWorkers());
        p2.setWorker(new Position(2,3));
        p2.setWorker(new Position(2,4));
        assertTrue(p2.hasPlacedWorkers());
        p3.setWorker(new Position(4,3));
        p3.setWorker(new Position(4,4));
        assertTrue(p3.hasPlacedWorkers());
        match.nextState();
    }

    @Test
    void testMatch(){
        Position worker, build;
        testPlacing();
        //p1.setPlayerState();

        System.out.println("\n-----------------------------------------------------------------------------" +
                           "\n-----------------------------first ROUND------------------------------------" +
                           "\n-----------------------------------------------------------------------------");
        {
            //---------turno di athena------------------------------------------------------
            p = p1;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(0, 0);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - moves from x=0 y=0 to x=1 y=1\n\n");
            worker = new Position(1, 1);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(0, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - builds a block in x=0 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE


            //---------turno di atlas------------------------------------------------------
            p = p2;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - moves from x=2 y=3 to x=2 y=2\n\n");
            worker = new Position(2, 2);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(2, 1);
            p.setUnsetSpecialFunction(true);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - builds a dome in x=2 y=1\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di efesto------------------------------------------------------
            p = p3;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(4, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - moves from x=4 y=4 to x=3 y=4\n\n");
            worker = new Position(3, 4);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - builds a block in x=3 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();

            build = new Position(3, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - builds a block in x=3 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();


            //FINE
        }

        System.out.println("\n-----------------------------------------------------------------------------" +
                           "\n-----------------------------second ROUND------------------------------------" +
                           "\n-----------------------------------------------------------------------------");
        {
            //---------turno di athena------------------------------------------------------
            p = p1;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(1, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - moves from x=1 y=1 to x=0 y=0 (moved up)\n\n");
            worker = new Position(0, 0);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - builds a block in x=1 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di atlas------------------------------------------------------
            p = p2;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 2);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("(move up not active!)\nAtlas - moves from x=2 y=2 to x=3 y=2\n\n");
            worker = new Position(3, 2);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(2, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - builds a block in x=2 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di efesto------------------------------------------------------
            p = p3;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(3, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("(move up not active!)\nHephaestus - moves from x=3 y=4 to x=2 y=3\n\n");
            worker = new Position(2, 3);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - builds a block in x=1 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();

            p.setHasFinished();
            match.checkPlayers();
            //FINE
        }
        System.out.println("\n-----------------------------------------------------------------------------" +
                           "\n-----------------------------third ROUND------------------------------------" +
                           "\n-----------------------------------------------------------------------------");
        {
            //---------turno di athena------------------------------------------------------
            p = p1;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(0, 1);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - moves from x=0 y=1 to x=1 y=1\n\n");
            worker = new Position(1, 1);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - builds a block in x=1 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di atlas------------------------------------------------------
            p = p2;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - moves from x=2 y=4 to x=1 y=4\n\n");
            worker = new Position(1,4);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(1,3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - builds a block in x=1 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di efesto------------------------------------------------------
            p = p3;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - moves from x=2 y=3 to x=1 y=3\n\n");
            worker = new Position(1, 3);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(0, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - builds a block in x=0 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(0, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - builds a block in x=0 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();
            match.checkPlayers();
            //FINE
        }

        System.out.println("\n-----------------------------------------------------------------------------" +
                           "\n-----------------------------fourth ROUND------------------------------------" +
                           "\n-----------------------------------------------------------------------------");
        {
            //---------turno di athena------------------------------------------------------
            p = p1;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(0, 0);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - moves from x=0 y=0 to x=1 y=0\n\n");
            worker = new Position(1, 0);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(2, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - builds a block in x=2 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di atlas------------------------------------------------------
            p = p2;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(1, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - moves from x=1 y=4 to x=2 y=4\n\n");
            worker = new Position(2,4);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(2,3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - builds a block in x=2 y=3\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di efesto------------------------------------------------------
            p = p3;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(1, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - moves from x=1 y=3 to x=2 y=3\n\n");
            worker = new Position(2, 3);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(3, 3);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - builds a block in x=3 y=3\n\n");
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
            //---------turno di athena------------------------------------------------------
            p = p1;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(1, 0);
            p.setCurrentWorker(worker);

            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - moves from x=1 y=0 to x=2 y=0\n\n");
            worker = new Position(2, 0);
            p.playerAction(worker);
            match.checkPlayers();

            p.getCurrentWorker().setAvailableCells(TurnState.BUILD, Collections.EMPTY_SET);
            p.getCommands().losingCondition(p);
            //COSTRUISCO
            build = new Position(3, 0);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Athena - builds a block in x=3 y=0\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di atlas------------------------------------------------------
            p = p2;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 4);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - moves from x=2 y=4 to x=3 y=4\n\n");
            worker = new Position(3,4);
            p.playerAction(worker);
            match.checkPlayers();

            //COSTRUISCO
            build = new Position(4,4);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Atlas - builds a block in x=4 y=4\n\n");
            p.playerAction(build);
            match.checkPlayers();
            //FINE

            //---------turno di efesto------------------------------------------------------
            p = p3;
            //INIZIO TURNO - MI MUOVO
            worker = new Position(2, 3);
            p.setCurrentWorker(worker);
            System.out.println(getBillboardStat(p.getWorkersAvailableCells().get(worker)));
            System.out.println("Hephaestus - moves from x=2 y=3 to x=3 y=3\n\n");
            worker = new Position(3, 3);
            p.playerAction(worker);
            GameModel.getMatchWinner(match.getID());
            if (match.checkPlayers()){
                System.out.println("MATCH FINISHED - WINNER IS "+p.toString());
            }
            match.nextState();
            GameModel.getMatchWinner(match.getID());
        }
    }





    String getBillboardStat(Set<Position> cells){
        StringBuilder outputA = new StringBuilder();
        StringBuilder outputB = new StringBuilder();
        StringBuilder outputC = new StringBuilder();
        StringBuilder output = new StringBuilder();
        StringBuilder playerslist = new StringBuilder();


        List<Integer> players =match.getPlayers().stream().map(player -> player.getID()).collect(Collectors.toList());

        match.getBillboard()
                .getCells()
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputA
                        .append(match.getBillboard().getPlayer(position)==0 ? "⬜️": "")
                        .append(players.indexOf(match.getBillboard().getPlayer(position)) == 0 ? "🟥":"" )
                        .append(players.indexOf(match.getBillboard().getPlayer(position)) == 1 ? "🟩":"" )
                        .append(players.indexOf(match.getBillboard().getPlayer(position)) == 2 ? "🟦":"" )
                        .append((position.getY()==4) ? "\n" : " "));

        outputA.append("\n");

        match.getBillboard()
                .getCells()
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputB
                        .append(match.getBillboard().getDome(position) ? "⏺" : "")
                        .append(!match.getBillboard().getDome(position) && match.getBillboard().getTowerHeight(position) == 0 ? "0️⃣": "")
                        .append(!match.getBillboard().getDome(position) && match.getBillboard().getTowerHeight(position) == 1 ? "1️⃣": "")
                        .append(!match.getBillboard().getDome(position) && match.getBillboard().getTowerHeight(position) == 2 ? "2️⃣": "")
                        .append(!match.getBillboard().getDome(position) && match.getBillboard().getTowerHeight(position) == 3 ? "3️⃣": "")
                        .append((position.getY()==4) ? "\n" : " "));

        match.getBillboard()
                .getCells()
                .keySet()
                .stream()
                .sorted()
                .forEach(position -> outputC
                        .append(cells.contains(position) ? "\u2B1B" : "")
                        .append(!(p.getCurrentWorker().getPosition().equals(position)) && !cells.contains(position) ? "\u2B1C" : "")
                        .append((p.getCurrentWorker().getPosition().equals(position)) ? "\uD83D\uDC77\uD83C\uDFFB" : "")
                        .append((position.getY()==4) ? "\n" : " "));


        int q, w;
        int j,k;
        int c,v;
        int i;


        for(i=0,q=0,j=0, c=0, w=outputA.indexOf("\n",0), k =outputB.indexOf("\n",0), v = outputC.indexOf("\n",0);
            i<5;
            i++,w=outputA.indexOf("\n",q), k = outputB.indexOf("\n",j), v =outputC.indexOf("\n",c) ){

            output.append(outputA,q,w);
            output.append("\t");
            output.append(outputB,j,k);
            output.append("\t");
            output.append(outputC,c,v);
            output.append("\n");
            q=++w;
            j=++k;
            c=++v;
        }
        return output.toString();
    }

}