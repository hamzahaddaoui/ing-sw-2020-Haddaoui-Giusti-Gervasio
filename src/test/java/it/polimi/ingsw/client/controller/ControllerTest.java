package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.InsertCharacter;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class ControllerTest {

    Controller controller = new Controller();
    View view = View.constructor();
    Set<Position> pos = new HashSet<Position>();
    String stringMessage;
    InsertCharacter insertCharacter;
    Player player = View.getPlayer();
    GameBoard gameBoard = View.getGameBoard();

    void setCells() {
        for (int x = 0; x<5; x++)
            for (int y = 0; y<5; y++)
                pos.add(new Position(x,y));
    }

    @BeforeEach
    void initView() {
        player.setColoredPlayersNum(new ArrayList<Integer>());
        setCells();
        gameBoard.setPlacingAvailableCells(pos);
    }



    @Test
    void update_Turn() {
        stringMessage = "LEO";
        controller.update(stringMessage);
    }
}