package it.polimi.ingsw.client;

import it.polimi.ingsw.utilities.Cell;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GameBoard {
    //sono le carte che vengono inserite in fase di SelectedSpecialCommandsStatus
    private static ArrayList<String> selectedGodCards;
    //sono le carte che vengono inserite in fase di SelectingGodCardsStatus, inizialmente prese dal Server
    private static ArrayList<String> godCards;

    private static String coloredGodCard;

    private static Map<Position, Cell> billboardStatus;
    private static Map<Position, Set<Position>> workersAvailableCells;
    private static Set<Position> placingAvailableCells;

    private static Position startingPosition;
    private static Position coloredPosition;

    public static void setGodCards(ArrayList<String> godCards) {
        GameBoard.godCards = godCards;
    }

    public static void setPlacingAvailableCells(Set<Position> placingAvailableCells) {
        GameBoard.placingAvailableCells = placingAvailableCells;
    }

    public static Map<Position, Set<Position>> getWorkersAvailableCells() {
        return workersAvailableCells;
    }

    public static void setWorkersAvailableCells(Map workersAvailableCells) {
        GameBoard.workersAvailableCells = workersAvailableCells;
    }

    public static ArrayList<String> getGodCards() {
        return godCards;
    }

    public static Set<Position> getPlacingAvailableCells() {
        return placingAvailableCells;
    }

    public static String getColoredGodCard() {
        return coloredGodCard;
    }

    public static Position getStartingPosition() {
        return startingPosition;
    }

    public static Position getColoredPosition() {
        return coloredPosition;
    }

    public static void setColoredPosition(Position position) {
        coloredPosition = position;
    }

    public static ArrayList<String> getSelectedGodCards() {
        return selectedGodCards;
    }

    public static void setBillboardStatus(Map<Position, Cell> billboardStatus) {
        GameBoard.billboardStatus = billboardStatus;
    }

    public static void setColoredGodCard(String coloredGodCard) {
        GameBoard.coloredGodCard = coloredGodCard;
    }

    public static void setStartingPosition(Position startingPosition) {
        GameBoard.startingPosition = startingPosition;
    }

    public static void setSelectedGodCards(ArrayList<String> selectedGodCards) {
        GameBoard.selectedGodCards = selectedGodCards;
    }

    public static Map<Position, Cell> getBillboardStatus() {
        return billboardStatus;
    }

    public static Set<Position> getWorkersAvailableCells(Position position) {
        return workersAvailableCells.get(position);
    }

    public static Set<Position> getWorkersPositions() { return workersAvailableCells.keySet();}

    public static boolean isWorkerPresent(Position position) {
        return workersAvailableCells.containsKey(position);
    }

    public static void setGodCards (Set<String> godCards) {
        selectedGodCards = new ArrayList<String>(godCards);
    }

}
