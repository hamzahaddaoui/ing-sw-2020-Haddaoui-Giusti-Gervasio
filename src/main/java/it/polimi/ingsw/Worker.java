package it.polimi.ingsw;

public class Worker {
    //Colori degli operai
    private Position position;
    private Billboard billboardID;
    private boolean placedWorker;

    public Worker(Billboard billboardID) {
        this.billboardID = billboardID;
    }

    public void setPosition(Position position) {
        if (placedWorker){
            billboardID.resetPlayerPosition(this.position);
        }
        else{
            placedWorker = true;
        }
        billboardID.setPlayerPosition(position);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
