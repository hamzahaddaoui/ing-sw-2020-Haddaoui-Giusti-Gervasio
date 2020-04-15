package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.Set;

public interface Commands {

    TurnState nextState(Player player);

    void placeWorker(Position position, Player player);

    void moveWorker(Position position, Player player);

    void build(Position position, Player player);

    Set<Position> computeAvailablePlacing(Player player);

    Set<Position> computeAvailableMovements(Player player, Worker worker);

    Set<Position> computeAvailableBuildings(Player player, Worker worker);

    boolean winningCondition(Player player);

    boolean losingCondition(Player player);



}
