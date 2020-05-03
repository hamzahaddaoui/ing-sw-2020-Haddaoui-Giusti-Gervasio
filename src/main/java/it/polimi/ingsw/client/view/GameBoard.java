package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.Cell;
import it.polimi.ingsw.utilities.Position;

import java.util.*;

public class GameBoard {

    private ArrayList<String> selectedGodCards ;    // usate per la Selection Special Command
    private ArrayList<String> matchCards;           //date dal Server, usate per la Selecting God Card

    private Map<Position, Cell> billboardStatus ;
    private Map<Position, Set<Position>> workersAvailableCells;
    private Set<Position> placingAvailableCells;

    private Position startingPosition;

    public GameBoard(){
        selectedGodCards = new ArrayList<>();
        matchCards = new ArrayList<>();
        billboardStatus = new HashMap<>();
        workersAvailableCells = new HashMap<>();
        placingAvailableCells = new HashSet<>();
        startingPosition = null;
    }

    public void setMatchCards(Set<String> godCards) {
        matchCards = new ArrayList<>(godCards);
    }

    public void setPlacingAvailableCells(Set<Position> newPlacingAvailableCells) {
        placingAvailableCells = newPlacingAvailableCells;
    }

    public Map<Position, Set<Position>> getWorkersAvailableCells() {
        return workersAvailableCells;
    }

    public void setWorkersAvailableCells(Map<Position, Set<Position>> newWorkersAvailableCells) {
        workersAvailableCells = newWorkersAvailableCells;
    }

    public ArrayList<String> getMatchCards() {
        return matchCards;
    }

    public Set<Position> getPlacingAvailableCells() {
        return placingAvailableCells;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public ArrayList<String> getSelectedGodCards() {
        return selectedGodCards;
    }

    public void setBillboardStatus(Map<Position, Cell> newBillboardStatus) {
        billboardStatus = newBillboardStatus;
    }

    public void setStartingPosition(Position position) {
        startingPosition = position;
    }

    public Map<Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    public Set<Position> getWorkersAvailableCells(Position position) {
        return workersAvailableCells.get(position);
    }

    public Set<Position> getWorkersPositions() {
            return workersAvailableCells.keySet();
    }

    public boolean isWorkerPresent(Position position) {
        return workersAvailableCells.containsKey(position);
    }

    public void setSelectedGodCards (Set<String> godCards) {
        selectedGodCards = new ArrayList<String>(godCards);
    }

}
