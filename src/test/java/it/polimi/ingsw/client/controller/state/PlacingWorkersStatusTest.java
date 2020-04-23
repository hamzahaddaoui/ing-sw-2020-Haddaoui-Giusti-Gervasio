package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    CommandCharacter commandCharacter;
    View view;
    Controller controller = new Controller();
    ControlState ctrlStatus = new SelectionNumberStatus();
    Set<Position> placingAvailableCells = new HashSet<>();

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

        controller = new Controller();
        ctrlStatus = new PlacingWorkersStatus();
        controller.setState(ctrlStatus);
        View.setPlacingAvailableCells(placingAvailableCells);
    }


    @Test
    void processingMessageA() {
        commandCharacter = inputA.apply();

        if(View.getColoredPosition() == null){
            View.setColoredPosition(position00);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position40);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position30);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position20);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position10);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position00);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position40);
        View.getPlacingAvailableCells().remove(position30);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position20);
        assertTrue(View.getWorkersPositions() == null );

    }

    @Test
    void processingMessageD() {
        commandCharacter = inputD.apply();

        if(View.getColoredPosition() == null){
            View.setColoredPosition(position40);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position00);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position10);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position20);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position30);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position40);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position00);
        View.getPlacingAvailableCells().remove(position10);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position20);
        assertTrue(View.getWorkersPositions() == null );

    }

    @Test
    void processingMessageS() {
        commandCharacter = inputS.apply();

        if(View.getColoredPosition() == null){
            View.setColoredPosition(position00);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position04);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position03);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position02);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position01);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position00);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position04);
        View.getPlacingAvailableCells().remove(position03);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position02);
        assertTrue(View.getWorkersPositions() == null );

    }

    @Test
    void processingMessageW() {
        commandCharacter = inputW.apply();

        if(View.getColoredPosition() == null){
            View.setColoredPosition(position04);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position00);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position01);
        assertTrue(View.getWorkersPositions() == null );

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position02);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position03);
        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position04);
        assertTrue(View.getWorkersPositions() == null );

        View.getPlacingAvailableCells().remove(position00);
        View.getPlacingAvailableCells().remove(position01);

        assertTrue(View.getPlacingAvailableCells().contains(View.getColoredPosition()));
        assertEquals(View.getColoredPosition(), position02);
        assertTrue(View.getWorkersPositions() == null );

    }

    @Test
    void processingMessageEnter() {
        commandCharacter = inputEnter.apply();

        if(View.getColoredPosition() == null){
            View.setColoredPosition(position04);
        }

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getWorkersPositions().size() == 1 );
        assertTrue(!View.getPlacingAvailableCells().contains(View.getWorkersPositions().stream().findAny().get()));
        assertTrue(View.getPlacingAvailableCells().size() + View.getWorkersPositions().size() == 25);
        assertTrue( View.getPlacingAvailableCells().contains(View.getColoredPosition()) );
        assertTrue(controller.isMessageReady() == false);


        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(View.getWorkersPositions().size() == 2 );
        assertTrue(View.getWorkersPositions().stream().filter(position -> View.getPlacingAvailableCells().contains(position)).count() == 0);
        assertTrue(View.getPlacingAvailableCells().size() + View.getWorkersPositions().size() == 25);
        assertTrue(controller.isMessageReady() == true);

    }

    @Test
    void processingMessageE() {
        commandCharacter = inputE.apply();

        assertTrue(controller.isMessageReady() == false);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(controller.isMessageReady() == false);

    }

    @Test
    void processingMessageQ() {
        commandCharacter = inputQ.apply();

        assertTrue(controller.isMessageReady() == false);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(controller.isMessageReady() == false);

    }

    @Test
    void processingMessageF() {
        commandCharacter = inputF.apply();

        assertTrue(controller.isMessageReady() == false);

        ctrlStatus.processingMessage(commandCharacter);

        assertTrue(controller.isMessageReady() == false);

    }

    @Test
    void nextState() {
        controller.setPlayerState(PlayerState.ACTIVE);
        controller.setMatchState(MatchState.RUNNING);
        controller.nextState();

        assertEquals( controller.getControlState().getClass() , new RunningStatus().getClass() );

        controller.setPlayerState(PlayerState.IDLE);
        controller.setMatchState(MatchState.RUNNING);
        controller.nextState();

        assertEquals( controller.getControlState().getClass(), new WaitingStatus().getClass() );
    }

}