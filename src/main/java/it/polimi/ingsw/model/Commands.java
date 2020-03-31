package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

import java.util.List;

public interface Commands {

    public void placeWorker(Worker worker, Position position, Billboard billboard);

    public void moveWorker(Worker worker, Position position, Billboard billboard);

    public void build(Worker worker, Position position, Billboard billboard);

    public List<Position> getAvailableCells(Worker worker, Billboard billboard);


}
