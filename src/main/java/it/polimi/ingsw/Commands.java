package it.polimi.ingsw;

import java.util.HashSet;

public interface Commands {
    public void placeWorkers();
    public void moveWorkers();
    public void buildBlock();
    public void buildDome();
    public HashSet<Position> checkAvailableMovements();
    public HashSet<Position> checkAvailableBuilds();
}
