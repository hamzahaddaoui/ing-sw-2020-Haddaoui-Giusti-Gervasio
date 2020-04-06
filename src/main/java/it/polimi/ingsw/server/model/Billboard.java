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
        try{
            return towerHeight.get(position);
        }
        catch (Exception e) {
            return 0;
        }

    }

    public void incrementTowerHeight(Position position) {
        try{
            int height = towerHeight.get(position);
            if (height < 3)
                towerHeight.replace(position, ++height);
            else
                setDome(position);
        }
        catch (Exception e) {
            return;
        }

    }

    public Map<Position, Boolean>  getDome() {
        return domePosition;
    }

    public boolean getDome(Position position) {
        try {
            return domePosition.get(position);
        }
        catch (Exception e) {
                return false;
        }
    }

    public void setDome(Position position){
        try {
            domePosition.replace(position, true);
        }
        catch (Exception e) {
                return;
        }

    }

    public Map<Position, Integer> getPlayer() {
        return playersPosition;
    }

    public int getPlayer(Position position) {
        try{
            return playersPosition.get(position);
        }
        catch (Exception e) {
            return -1;
        }
    }

    public void setPlayer(Position position, int player){
        try{
            playersPosition.replace(position, player);
        }
        catch (Exception e) {
            return;
        }
    }

    public void resetPlayer(Position position){
        try {
            playersPosition.replace(position, -1);
        }
        catch (Exception e) {
            return;
        }
    }

}
