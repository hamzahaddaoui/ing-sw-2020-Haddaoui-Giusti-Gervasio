package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Cell;
import it.polimi.ingsw.utilities.Position;
import java.util.HashMap;
import java.util.Map;

public class Billboard {

    static final int ROWS = 5;
    static final int COLOUMNS = 5;

    private final Map<Position, Cell> cellSet = new HashMap<>();

    public Billboard() {
        int x, y;
        for(x=0; x<ROWS; x++){
            for(y=0; y<COLOUMNS; y++){
                cellSet.put(new Position(x,y), new Cell());
            }
        }
    }

    public Map<Position, Cell> getCells() {
        return cellSet;
    }

    public int getTowerHeight(Position position) {
        return cellSet.get(position).getTowerHeight();
    }

    public void incrementTowerHeight(Position position) {
        int height = cellSet.get(position).getTowerHeight();
        if (height < 3){
            height ++;
            cellSet.get(position).setTowerHeight(height);
        }
        else
            setDome(position);
    }

    public boolean getDome(Position position) {
        return cellSet.get(position).isDome();
    }

    public void setDome(Position position){
        cellSet.get(position).setDome(true);
    }

    public int getPlayer(Position position) {
        return cellSet.get(position).getPlayerID();
    }

    public void setPlayer(Position position, int player){
        cellSet.get(position).setPlayerID(player);
    }

    public void resetPlayer(Position position) {
        cellSet.get(position).setPlayerID(0);
    }
}
