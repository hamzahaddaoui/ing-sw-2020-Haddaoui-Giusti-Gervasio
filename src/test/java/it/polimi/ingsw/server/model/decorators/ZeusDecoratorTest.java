package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.Commands;
import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ZeusDecoratorTest {
    Commands commands1,commands2;
    Player player1 = new Player(1,"leo");
    Match match = new Match(1, player1);
    Player player2 = new Player(2,"dario");
    Set<Position> positions = new HashSet<Position>();
    Set<GodCards> godCards = new HashSet<GodCards>();

    Position position00=new Position(0,0);
    Position position01=new Position(0,1);
    Position position02=new Position(0,2);
    Position position03=new Position(0,3);
    Position position04=new Position(0,4);
    Position position10=new Position(1,0);
    Position position11=new Position(1,1);
    Position position12=new Position(1,2);
    Position position13=new Position(1,3);
    Position position14=new Position(1,4);
    Position position20=new Position(2,0);
    Position position21=new Position(2,1);
    Position position22=new Position(2,2);
    Position position23=new Position(2,3);
    Position position24=new Position(2,4);
    Position position30=new Position(3,0);
    Position position31=new Position(3,1);
    Position position32=new Position(3,2);
    Position position33=new Position(3,3);
    Position position34=new Position(3,4);
    Position position40=new Position(4,0);
    Position position41=new Position(4,1);
    Position position42=new Position(4,2);
    Position position43=new Position(4,3);
    Position position44=new Position(4,4);

    @BeforeEach
    void setUp() {
        match.addPlayer(player2);
        match.setPlayersNum(2);
        godCards.add(GodCards.Zeus);
        godCards.add(GodCards.Artemis);
        match.setCards(godCards);
        player1.setCommands(GodCards.Zeus);
        player2.setCommands(GodCards.Artemis);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
    }

    @Test
    void mainTurn() {
        assertFalse(commands1.winningCondition(player1));
        player1.setWorker(position12);
        player1.setWorker(position00);
        player2.setWorker(position13);
        player2.setWorker(position41);
        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.BUILD);

        player1.setCurrentWorker(position12);  //level 0
        Set<Position> SetCheck=commands1.computeAvailableBuildings(player1,player1.getCurrentWorker());

        assertTrue(SetCheck.contains(position12));

        player1.getCommands().build(position12,player1);

        assertTrue(player1.getCurrentWorkerPosition().getZ()==1);
        assertTrue(player1.getMatch().getBillboard().getTowerHeight(position12) == 1);
        assertFalse(player1.getCommands().winningCondition(player1));

        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.BUILD);

        //level 1
        SetCheck=commands1.computeAvailableBuildings(player1,player1.getCurrentWorker());

        assertTrue(SetCheck.contains(position12));

        player1.getCommands().build(position12,player1);

        assertTrue(player1.getCurrentWorkerPosition().getZ()==2);
        assertTrue(player1.getMatch().getBillboard().getTowerHeight(position12) ==2);
        assertFalse(player1.getCommands().winningCondition(player1));

        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.BUILD);

        //level 2
        SetCheck=commands1.computeAvailableBuildings(player1,player1.getCurrentWorker());

        assertTrue(SetCheck.contains(position12));

        player1.getCommands().build(position12,player1);

        assertTrue(player1.getCurrentWorkerPosition().getZ()==3);
        assertTrue(player1.getMatch().getBillboard().getTowerHeight(position12) == 3);
        assertFalse(player1.getCommands().winningCondition(player1));

        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.BUILD);

        //level 3
        SetCheck=commands1.computeAvailableBuildings(player1,player1.getCurrentWorker());

        assertFalse(SetCheck.contains(position12));

        assertTrue(player1.getCurrentWorkerPosition().getZ()==3);
        assertTrue(player1.getMatch().getBillboard().getTowerHeight(position12) == 3);

        //end turn
        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.moveWorker(position11,player1);
        commands1.nextState(player1);
        commands1.build(position12,player1);

        assertTrue(player1.getMatch().getBillboard().getDome(position12));
        assertTrue(player1.getTurnState() == TurnState.BUILD);

        commands1.nextState(player1);

        assertSame(player1.getTurnState(), TurnState.IDLE);
        assertSame(player1.getPlayerState(), PlayerState.IDLE);
    }
}