package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.InsertCharacter;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class ControllerTest {

    Controller controller = new Controller();
    View view = new View();
    Set<Position> pos = new HashSet<Position>();
    String stringMessage;
    InsertCharacter insertCharacter;

    void setCells() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                pos.add(new Position(x,y));
    }

    @BeforeEach
    void initView() {
        View.setColoredPlayersNum(new ArrayList<Integer>());
        setCells();
        View.setPlacingAvailableCells(pos);
    }



    @Test
    void update_Turn() {
        stringMessage = "LEO";
        AssertEq();
        controller.update(stringMessage);

    }
}