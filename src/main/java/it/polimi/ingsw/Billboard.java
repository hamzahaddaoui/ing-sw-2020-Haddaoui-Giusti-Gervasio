package it.polimi.ingsw;

public class Billboard {
    static final int ROWS = 5;
    static final int COLOUMNS = 5;

    private int [][] towerHeight = new int[ROWS][COLOUMNS];
    private boolean [][] domePosition = new boolean[ROWS][COLOUMNS];
    private boolean [][] playerPosition = new boolean[ROWS][COLOUMNS];

    public int[][] getTowerHeight() {
        return towerHeight;
    }

    public int getTowerHeight(Position position) {
        return towerHeight[position.getX()][position.getY()];
    }

    public boolean[][] getDome() {
        return domePosition;
    }

    public boolean getDome(Position position) {
        return domePosition[position.getX()][position.getY()];
    }


    public boolean[][] getPlayerPosition() {
        return playerPosition;
    }

    public boolean getPlayerPosition(Position position) {
        return playerPosition[position.getX()][position.getY()];
    }


    public void setTowerHeight(Position position, int level) {
        towerHeight[position.getX()][position.getY()] = level;
    }

    public void setDome(Position position){
        domePosition[position.getX()][position.getY()] = true;
    }

    public void setPlayerPosition(Position position){
        playerPosition[position.getX()][position.getY()] = true;
    }

    public void resetPlayerPosition(Position position){
        playerPosition[position.getX()][position.getY()] = false;
    }
}
