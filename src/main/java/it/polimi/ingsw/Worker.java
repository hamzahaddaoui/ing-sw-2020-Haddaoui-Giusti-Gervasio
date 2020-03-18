package it.polimi.ingsw;

public class Worker {
    private int positionX, positionY;
    private Billboard billboardID;

    public Worker(Billboard billboardID) {
        this.billboardID = billboardID;
    }


    public void setPosition(int positionX, int positionY) {
        billboardID.setPlayerPosition(positionX,positionY);
        this.positionX = positionX;
        this.positionY = positionY;
    }


    public int getPositionX() {
        return positionX;
    }

    public int getPositionY(int positionY) {
        return positionY;
    }





}
