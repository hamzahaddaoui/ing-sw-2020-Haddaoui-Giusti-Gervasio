package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

public interface Commands {

    public void placeWorker(Worker worker, Position position, Billboard billboard);

    public void moveWorker(Worker worker, Position position, Billboard billboard);

    public void build(Worker worker, Position position, Billboard billboard);

    public void availableCells(Billboard billboard);


}
