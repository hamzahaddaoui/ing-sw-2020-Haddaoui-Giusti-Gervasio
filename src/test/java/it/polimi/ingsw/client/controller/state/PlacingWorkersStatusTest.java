package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlacingWorkersStatusTest {

    InsertCharacter inputA = InsertCharacter.A;
    InsertCharacter inputD = InsertCharacter.D;
    InsertCharacter inputW = InsertCharacter.W;
    InsertCharacter inputE = InsertCharacter.E;
    InsertCharacter inputF = InsertCharacter.F;
    InsertCharacter inputQ = InsertCharacter.Q;
    InsertCharacter inputS = InsertCharacter.S;
    InsertCharacter inputEnter = InsertCharacter.ENTER;
    InsertCharacter commandCharacter;

    View view;
    Controller controller = new Controller();
    ControlState ctrlStatus = new PlacingWorkersStatus();
    GameBoard gameBoard ;
    Player player ;
    Set<Position> placingAvailableCells ;
    Map<Position, Set<Position>> workersAvailableCells = new HashMap();

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
    void setUp(){
        view = new View();
        gameBoard = view.getGameBoard();
        player = view.getPlayer();

        placingAvailableCells = new HashSet<>();
        placingAvailableCells.add(position00);
        placingAvailableCells.add(position01);
        placingAvailableCells.add(position02);
        placingAvailableCells.add(position03);
        placingAvailableCells.add(position04);
        placingAvailableCells.add(position10);
        placingAvailableCells.add(position11);
        placingAvailableCells.add(position12);
        placingAvailableCells.add(position13);
        placingAvailableCells.add(position14);
        placingAvailableCells.add(position20);
        placingAvailableCells.add(position21);
        placingAvailableCells.add(position22);
        placingAvailableCells.add(position23);
        placingAvailableCells.add(position24);
        placingAvailableCells.add(position30);
        placingAvailableCells.add(position31);
        placingAvailableCells.add(position32);
        placingAvailableCells.add(position33);
        placingAvailableCells.add(position34);
        placingAvailableCells.add(position40);
        placingAvailableCells.add(position41);
        placingAvailableCells.add(position42);
        placingAvailableCells.add(position43);
        placingAvailableCells.add(position44);

        controller.setState(ctrlStatus);
        gameBoard.setPlacingAvailableCells(new HashSet<>(placingAvailableCells));
        gameBoard.setWorkersAvailableCells(new HashMap(workersAvailableCells));
    }


    @Test // decrement coordinate X
    void processingMessageW() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputW;

        if(gameBoard.getColoredPosition() == null)
            gameBoard.setColoredPosition(position00);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position40);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position30);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position20);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position10);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position00);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position40);
        gameBoard.getPlacingAvailableCells().remove(position30);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position20);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );
    }

    @Test // increase coordinate X
    void processingMessageS() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputS;

        if(gameBoard.getColoredPosition() == null){
            gameBoard.setColoredPosition(position40);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position00);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position10);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position20);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position30);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position40);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position00);
        gameBoard.getPlacingAvailableCells().remove(position10);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position20);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

    }

    @Test // decrement coordinate Y
    void processingMessageA() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputA;

        if(gameBoard.getColoredPosition() == null){
            gameBoard.setColoredPosition(position00);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position04);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position03);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position02);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position01);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position00);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position04);
        gameBoard.getPlacingAvailableCells().remove(position03);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position02);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

    }

    @Test // increase coordinate Y
    void processingMessageD() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputD;

        if(gameBoard.getColoredPosition() == null){
            gameBoard.setColoredPosition(position04);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position00);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position01);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position02);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position03);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position04);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

        gameBoard.getPlacingAvailableCells().remove(position00);
        gameBoard.getPlacingAvailableCells().remove(position01);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()));
        assertEquals(gameBoard.getColoredPosition(), position02);
        assertTrue(gameBoard.getWorkersPositions().size() == 0 );

    }

    @Test
    void processingMessageEnter() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputEnter;

        if(gameBoard.getColoredPosition() == null){
            gameBoard.setColoredPosition(position04);
        }

        ctrlStatus.processingMessage(commandCharacter);

        System.out.println(gameBoard.getWorkersAvailableCells().keySet().stream().count());

        assertTrue(gameBoard.getWorkersAvailableCells().keySet().size() == 1 );
        assertTrue(!gameBoard.getPlacingAvailableCells().contains(gameBoard.getWorkersPositions().stream().findAny().get()));
        assertTrue(gameBoard.getPlacingAvailableCells().size() + gameBoard.getWorkersAvailableCells().keySet().size() == 25);
        assertTrue( gameBoard.getPlacingAvailableCells().contains(gameBoard.getColoredPosition()) );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(gameBoard.getWorkersPositions().size() == 2 );
        assertTrue(gameBoard.getWorkersPositions().stream().filter(position -> gameBoard.getPlacingAvailableCells().contains(position)).count() == 0);
        assertTrue(gameBoard.getPlacingAvailableCells().size() + gameBoard.getWorkersPositions().size() == 25);

    }

    @Test
    void processingMessageE() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputE;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

    }

    @Test
    void processingMessageQ() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputQ;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));
    }

    @Test
    void processingMessageF() {
        gameBoard.setColoredPosition(null);
        commandCharacter = inputF;

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue( !ctrlStatus.processingMessage(commandCharacter));



    }

    @Test
    void processingClass(){
        gameBoard.setColoredPosition(null);
        commandCharacter = inputF;
        Position positionError = new Position(5,5);
        char character = 'x';

        assertThrows(IllegalArgumentException.class,()->ctrlStatus.processingMessage(character));

        gameBoard.setColoredPosition(positionError);

        assertThrows(IllegalArgumentException.class,()->ctrlStatus.processingMessage(commandCharacter) );

        gameBoard.setColoredPosition(null);
        gameBoard.setPlacingAvailableCells(null);

        assertThrows(IllegalArgumentException.class,()->ctrlStatus.processingMessage(commandCharacter) );
    }


}