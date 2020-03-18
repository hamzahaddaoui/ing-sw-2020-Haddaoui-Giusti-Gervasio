package it.polimi.ingsw;

public class Worker {
    private int positionX, positionY;
    private Billboard billboardID;
    private boolean placedWorker;

    public Worker(Billboard billboardID) {
        this.billboardID = billboardID;
    }


    public void setPosition(int positionX, int positionY) {
        if (!placedWorker) billboardID.setPlayerPosition(positionX,positionY);

        else billboardID.movePlayer(this.positionX,this.positionY, positionX, positionY);

        this.positionX = positionX;
        this.positionY = positionY;
    }


    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }





}
