package it.polimi.ingsw;

public class Worker extends AbstractObject{
    //Colori degli operai
    private Position position;
    private boolean movedUp;

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public boolean hasMovedUp(){ return movedUp;}
}
