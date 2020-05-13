package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;



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
