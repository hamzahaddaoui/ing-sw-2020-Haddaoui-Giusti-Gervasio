package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

import java.util.List;

public interface Commands {

    public void placeWorker(Position position, Player player);

    public void moveWorker(Position position, Player player);

    public void build(Position position, Player player);

    public List<Position> getAvailableCells(Player player);


}
