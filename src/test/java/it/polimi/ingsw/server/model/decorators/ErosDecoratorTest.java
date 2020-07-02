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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ErosDecoratorTest {
    Commands commands1,commands2, commands3;
    Player player1 = new Player(1,"leo");
    Match match = new Match(1, player1);
    Player player2 = new Player(2,"dario");
    Player player3 = new Player(3,"hamza");
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

    }

    /**
     * @return  the list of cells that are along the boarder of the Billboard
     */
    private List<Position> positionList (){
        List <Position> positions = new ArrayList<>();
        for(int i=0; 0<= i && i< 5; i++ )
            for(int j=0; 0<= j && j< 5; j++ )
                if((i== 0 || i== 4)||(j==0 || j==4))
                    positions.add(new Position(i,j));
        return positions;
    }


    /**
     * This method tests the compute available cells
     */
    @Test
    void placing(){
        assertThrows( Exception.class, ()-> commands1.computeAvailablePlacing(player1));
        match.addPlayer(player2);
        match.setPlayersNum(2);
        godCards.add(GodCards.Zeus);
        godCards.add(GodCards.Eros);
        match.setCards(godCards);
        player1.setCommands(GodCards.Eros);
        player2.setCommands(GodCards.Zeus);
        commands1=player1.getCommands();
        commands2=player2.getCommands();

        commands1.computeAvailablePlacing(player1);
        List<Position> positions = positionList();
        assertTrue(player1.getPlacingAvailableCells().containsAll(positions));
        assertTrue(positions.containsAll(player1.getPlacingAvailableCells()));

    }


    @Test
    void winningMatch2Players(){
        match.addPlayer(player2);
        match.setPlayersNum(2);
        godCards.add(GodCards.Zeus);
        godCards.add(GodCards.Eros);
        match.setCards(godCards);
        player1.setCommands(GodCards.Eros);
        player2.setCommands(GodCards.Zeus);
        commands1=player1.getCommands();
        commands2=player2.getCommands();

        player1.setWorker(position12);
        player1.setWorker(position01);
        player2.setWorker(position13);
        player2.setWorker(position41);
        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.BUILD);

        player1.setCurrentWorker(position12);
        commands1.build(position11,player1);

        assertFalse(player1.getCommands().winningCondition(player1));

        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.moveWorker(position11,player1);
        assertFalse(player1.getCommands().winningCondition(player1));

        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.BUILD);

        player1.setHasFinished();
        player1.setPlayerState();
        player1.setTurnState(TurnState.IDLE);

        player1.setCurrentWorker(position01);

        commands1.build(position00,player1);

        assertFalse(player1.getCommands().winningCondition(player1));
        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        commands1.moveWorker(position00,player1);
        assertTrue(player1.getCommands().winningCondition(player1));

    }

    @Test
    void winningMatch3Players(){
        match.addPlayer(player2);
        match.addPlayer(player3);
        match.setPlayersNum(3);
        godCards.add(GodCards.Zeus);
        godCards.add(GodCards.Eros);
        godCards.add(GodCards.Artemis);
        match.setCards(godCards);
        player1.setCommands(GodCards.Eros);
        player2.setCommands(GodCards.Zeus);
        player3.setCommands(GodCards.Artemis);
        commands1=player1.getCommands();
        commands2=player2.getCommands();
        commands3=player3.getCommands();

        commands1.computeAvailablePlacing(player1);
        List<Position> positions = positionList();
        assertTrue(player1.getPlacingAvailableCells().containsAll(positions));
        assertTrue(positions.containsAll(player1.getPlacingAvailableCells()));

        player1.setWorker(position12);
        player1.setWorker(position33);
        player2.setWorker(position13);
        player2.setWorker(position41);
        player3.setWorker(position14);
        player3.setWorker(position00);
        player1.setTurnState(TurnState.IDLE);
        commands1.nextState(player1);

        assertTrue(player1.getTurnState() == TurnState.MOVE);

        player1.setCurrentWorker(position33);
        commands1.moveWorker(position22,player1);

        assertTrue(player1.getCommands().winningCondition(player1));
    }


}