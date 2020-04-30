package it.polimi.ingsw.utilities;

public class Cell {
    private int towerHeight;
    private boolean dome;
    private int playerID;

    public Cell() {
        towerHeight = 0;
        dome = false;
        playerID = 0;
    }

    public int getTowerHeight(){
        return towerHeight;
    }

    public boolean isDome(){
        return dome;
    }

    public int getPlayerID(){
        return playerID;
    }

    public void setTowerHeight(int towerHeight){
        this.towerHeight = towerHeight;
    }

    public void setDome(boolean dome){
        this.dome = dome;
    }

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }
}
