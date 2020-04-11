package it.polimi.ingsw.utilities;

public class Cell {
    private int towerHeight;
    private boolean dome;
    private Integer playerID;

    public int getTowerHeight(){
        return towerHeight;
    }

    public boolean isDome(){
        return dome;
    }

    public Integer getPlayerID(){
        return playerID;
    }

    public void setTowerHeight(int towerHeight){
        this.towerHeight = towerHeight;
    }

    public void setDome(boolean dome){
        this.dome = dome;
    }

    public void setPlayerID(Integer playerID){
        this.playerID = playerID;
    }
}
