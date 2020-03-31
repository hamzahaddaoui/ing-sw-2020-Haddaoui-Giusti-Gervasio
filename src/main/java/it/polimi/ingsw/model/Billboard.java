package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

public class Billboard {

    static final int ROWS = 5;
    static final int COLOUMNS = 5;

    private int [][] towerHeight;
    /*
    0 if the space is empty
    1 if the space is on the first floor
    2 if the space is on the second floor
    3 if the space is on the third floor
     */

    private boolean [][] domePosition ;
    /*
    false if in the space there isn't the dome
    true if in the space there is the dome
     */
    private  String[][] playerColor;

    public Billboard() {
        this.towerHeight = new int[ROWS][COLOUMNS];
        this.domePosition = new boolean[ROWS][COLOUMNS];
        this.playerColor = new String[ROWS][COLOUMNS];
    }


    public int[][] getTowerHeight() {
        return towerHeight;
    }

    public int getTowerHeight(Position position) {
        return towerHeight[position.getX()][position.getY()];
    }

    public void setTowerHeight(Position position, int level) {
        towerHeight[position.getX()][position.getY()] = level;
    }

    public boolean[][] getDome() {
        return domePosition;
    }

    public boolean getDome(Position position) {
        return domePosition[position.getX()][position.getY()];
    }

    public void setDome(Position position){
        domePosition[position.getX()][position.getY()] = true;
    }

    public String[][] getPlayerColor() {
        return playerColor;
    }

    public String getPlayerColor(Position position) {
        if((position.getX()>=0)&&(position.getY()>=0))
            return playerColor[position.getX()][position.getY()];
        else throw new IllegalArgumentException("Error");
    }

    public void setPlayerColor(Position position, Worker worker){
        playerColor[position.getX()][position.getY()] = worker.getColor();
    }

    public void resetPlayerColor(Position position){
        playerColor[position.getX()][position.getY()] = null;
    }

}
