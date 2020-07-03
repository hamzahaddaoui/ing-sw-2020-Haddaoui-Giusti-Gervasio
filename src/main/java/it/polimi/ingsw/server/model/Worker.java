package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: hamzahaddaoui
 *
 * Class that defines a worker, and all of its properties.
 * The worker has a position, has its own available cells, which it can move or build.
 * It has also a height variation defined by the last move made.
 */

public class Worker{
    private Position position;

    private int heightVariation;
    private final Map<TurnState, Set<Position>> availableCells = new HashMap<>();

    public Worker(Position position) {
        this.position = position;
    }

    public void setPosition(Position position) {
        this.heightVariation = position.getZ() - this.position.getZ();
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public int getHeightVariation() {
        return heightVariation;
    }

    public boolean canDoSomething(TurnState turnState) {
        return !(availableCells.get(turnState) != null && availableCells.get(turnState).isEmpty());
    }

    public Set<Position> getAvailableCells(TurnState state){
        return availableCells.get(state);
    }

    public void setAvailableCells(TurnState state, Set<Position> availableCells){
        this.availableCells.put(state, availableCells);
    }

}
