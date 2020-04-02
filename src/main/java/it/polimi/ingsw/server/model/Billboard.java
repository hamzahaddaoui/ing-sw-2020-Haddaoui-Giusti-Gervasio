package it.polimi.ingsw.server.model;

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
    private  Color[][] playerColor;

    public Billboard() {
        this.towerHeight = new int[ROWS][COLOUMNS];
        this.domePosition = new boolean[ROWS][COLOUMNS];
        this.playerColor = new Color[ROWS][COLOUMNS];
    }


    public int[][] getTowerHeight() {
        return towerHeight;
    }

    public int getTowerHeight(Position position) {
        return towerHeight[position.getX()][position.getY()];
    }

    public void incrementTowerHeight(Position position) {
        if (towerHeight[position.getX()][position.getY()] < 3)
            towerHeight[position.getX()][position.getY()] ++;
        else
            setDome(position);
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

    public Color[][] getPlayer() {
        return playerColor;
    }

    public Color getPlayer(Position position) {
        return playerColor[position.getX()][position.getY()];
    }

    public void setPlayer(Position position, Worker worker){
        playerColor[position.getX()][position.getY()] = worker.getColor();
    }

    public void resetPlayer(Position position){
        playerColor[position.getX()][position.getY()] = null;
    }

}
