package it.polimi.ingsw.server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @Test
    void getMatchState(){
        System.out.println(GameModel.getMatchState(GameModel.createMatch()));
        assertTrue(true);
    }
}