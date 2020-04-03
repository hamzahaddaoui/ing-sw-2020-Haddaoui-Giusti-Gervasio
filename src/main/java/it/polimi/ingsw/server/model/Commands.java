package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.List;

public interface Commands {

    public void placeWorker(Position position, Player player);

    public void moveWorker(Position position, Player player);

    public void build(Position position, Player player);

    public void build(Position position, Player player, boolean forceDome);

    public Set<Position> getAvailableCells(Player player);

    public Set<Position> computeAvailablePlacing(Player player);

    public Set<Position> computeAvailableMovements(Player player);

    public Set<Position> computeAvailableBuildings(Player player);

    public void specialFunctionSetUnset();

    public void reset(Player player);

    public void winningCondition(Position startingPosition, Player player);

    public void losingCondition(Player player);

}
