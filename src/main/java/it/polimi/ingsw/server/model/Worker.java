package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

public class Worker{

    private Position position;
    private Color color;
    private boolean movedUp;

    public Worker(Position position){
        this.position = position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public boolean hasMovedUp(){ return movedUp;}

    public void setMovedUp(boolean movedUp) {
        this.movedUp = movedUp;
    }


    public Color getColor() {
        return color;
    }
}
