package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;
import java.util.Set;

public interface Commands {
    public void placeWorker(Position position, Player player);

    public void moveWorker(Position position, Player player);

    public void build(Position position, Player player);

    public Set<Position> getAvailableCells(Player player);

    public void specialFunctionSetUnset(Player player);
}
