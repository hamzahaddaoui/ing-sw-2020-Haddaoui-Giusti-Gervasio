package it.polimi.ingsw;


import java.util.HashSet;

public class WorkerDecorator extends Worker {
    protected Worker worker;
    public int movesBeforeBuild = 1;
    public int numOfBuilds = 1;
    public int movesAfterBuild = 0;
    public boolean doneStandard = false;

    public void placeWorker() {
        return;
    }


    public void moveWorker() {

    }


    public void buildBlock() {

    }


    public void buildDome() {

    }


    public HashSet<Position> checkAvailableMovements() {
        return null;
    }


    public HashSet<Position> checkAvailableBuildBlocks() {
        return null;
    }


    public HashSet<Position> checkAvailableBuildDomes() {
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


    public boolean hasDoneStandard() {
        return false;
    }

}
