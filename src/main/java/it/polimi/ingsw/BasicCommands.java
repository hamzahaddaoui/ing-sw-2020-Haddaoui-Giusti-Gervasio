package it.polimi.ingsw;


import java.util.HashSet;

public class BasicCommands extends AbstractObject {
    protected AbstractObject worker;
    public int movesBeforeBuild = 1;
    public int numOfBuilds = 1;
    public int movesAfterBuild = 0;
    public boolean doneStandardMoves = false;

    public void placeWorkers() {

    }


    public void moveWorkers() {

    }


    public void buildBlock() {

    }


    public void buildDome() {

    }


    public HashSet<Position> checkAvailableMovements() {
        return null;
    }


    public HashSet<Position> checkAvailableBuildsBlocks() {
        return null;
    }


    public HashSet<Position> checkAvailableBuildsDomes() {
        return null;
    }


    public boolean hasWon() {
        return false;
    }


    public boolean hasLost() {
        return false;
    }


    public boolean hasMoved() {
        return false;
    }


    public boolean hasBuilt() {
        return false;
    }


    public boolean hasDoneStandardMoves() {
        return false;
    }

}
