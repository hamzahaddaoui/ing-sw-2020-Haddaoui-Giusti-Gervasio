package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;
import java.util.HashMap;
import java.util.Map;

public class Billboard {

    static final int ROWS = 5;
    static final int COLOUMNS = 5;

    private Map<Position, Integer> towerHeight = new HashMap<>();
    //è inutile tenere la lista di tutte le celle... basta tenere solo la lista delle posizioni dove c'è una cupola
    private Map<Position, Boolean> domePosition = new HashMap<>();
    //stessa cosa qui! tengo solo le posizioni dove c'è un utente
    private Map<Position, Integer> playersPosition = new HashMap<>();

    public Billboard() {
        int i, j;
        Position position;
        for(i=0; i<ROWS; i++){
            for(j=0; j<COLOUMNS; j++){
                position = new Position(i,j);
                towerHeight.put(position, 0);
                domePosition.put(position, false);
                playersPosition.put(position, -1);
                Position position1 = new Position(i,j);
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
        if (height < 3)
            towerHeight.replace(position, ++height);
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

    public void setPlayer(Position position, int player){
        playersPosition.replace(position, player);
    }

    public void resetPlayer(Position position) {
        playersPosition.replace(position, - 1);
    }
}
