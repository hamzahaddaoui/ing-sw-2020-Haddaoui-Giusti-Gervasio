package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Billboard {

    static final int ROWS = 5;
    static final int COLOUMNS = 5;

    private Map<Position, Integer> towerHeight;
    private Map<Position, Boolean> domePosition ;
    private Map<Position, Integer> playersPosition;

    public Billboard() {
        int i, j;
        Position position;
        this.towerHeight = new HashMap<>();
        this.domePosition = new HashMap<>();
        this.playersPosition = new HashMap<>();
        for(i=0; i<ROWS; i++){
            for(j=0; j<COLOUMNS; j++){
                position = new Position(i,j);
                towerHeight.put(position, 0);
                domePosition.put(position, false);
                playersPosition.put(position, -1);
            }
        }
    }

    public Map<Position, Integer> getTowerHeight() {
        return towerHeight;
    }

    public int getTowerHeight(Position position) {
        return towerHeight.get(position);
    }

    public void incrementTowerHeight(Position position) {
        int height = towerHeight.get(position);
        if (height  < 3)
            towerHeight.replace(position,height++);
        else
            setDome(position);
    }

    public Map<Position, Boolean>  getDome() {
        return domePosition;
    }

    public boolean getDome(Position position) {
        return domePosition.get(position);
    }

    public void setDome(Position position){
        domePosition.replace(position, true);
    }

    public Map<Position, Integer> getPlayer() {
        return playersPosition;
    }

    public int getPlayer(Position position) {
        return playersPosition.get(position);
    }

    public void setPlayer(Position position, Player player){
        playersPosition.replace(position, player.getID());
    }

    public void resetPlayer(Position position){
        playersPosition.replace(position, -1);
    }

}
