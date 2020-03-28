package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

public interface Commands {
    public void placeWorker(Position position);

    public void moveWorker(Position position);

    public void build(Position position);
}
