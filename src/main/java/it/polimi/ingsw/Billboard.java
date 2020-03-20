package it.polimi.ingsw;

public class Billboard {
    private int [][] towerHeight = new int[5][5];
    /*[ 0 0 0 0 0
        0 0 0 0 0
        0 0 0 0 0
        0 0 0 0 0
        0 0 0 0 0  ]
    */
    private boolean [][] domes = new boolean[5][5];
    private boolean [][] players = new boolean[5][5];

    public void Billboard (){

    }

    private boolean checkSpace(int positionX, int positionY){
        //do something
        return false;
    }

    public boolean setPlayerPosition(int positionX, int positionY){
        if (checkSpace(positionX, positionY)) {
            //controllo sia libero
            players[positionX][positionY] = true;
            return true;
        }
        else return false;
    }

    public boolean movePlayer(int prevPosX, int prevPosY, int finalPosX, int finalPosY){
        //check worker move correctness
        return false;
    }

    public availableMovements(){

    }


}
