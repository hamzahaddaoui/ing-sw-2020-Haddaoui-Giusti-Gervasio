package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

public class Worker{
    //Colori degli operai
    private Position position;

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
}
