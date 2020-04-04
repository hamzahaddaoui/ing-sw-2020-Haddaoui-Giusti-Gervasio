package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class Worker{
    private Position position;
    private Color color;

    private int heightVariation;
    private Map<TurnState, Set<Position>> availableCells = new HashMap<>();

    public void setPosition(Position position) {
        this.heightVariation = position.getZ() - this.position.getZ();
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getHeightVariation() {
        return heightVariation;
    }

    public boolean isMovable() {
        return !(this.availableCells.get(TurnState.MOVE).isEmpty());
    }

    public boolean isAbleToBuild() {
        return !(this.availableCells.get(TurnState.BUILD).isEmpty());
    }

    public Set<Position> getAvailableCells(TurnState state){
        return availableCells.get(state);
    }

    public void setAvailableCells(TurnState state, Set<Position> availableCells){
        this.availableCells.put(state, availableCells);
    }

}
