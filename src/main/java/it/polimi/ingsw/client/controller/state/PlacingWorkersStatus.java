package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import java.security.Policy;
import java.util.HashSet;
import java.util.Set;

public class PlacingWorkersStatus extends ControlState {

    Set<Position> initializedPositions = new HashSet<>();
    Position position;

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException {

        if (checkMessage(viewObject)) {

                int x = Character.getNumericValue(viewObject.charAt(0))-1;
                int y = Character.getNumericValue(viewObject.charAt(1))-1;

                if (x <= 4 && x >= 0 && y <= 4 && y >= 0)
                    position = new Position(x, y);
                else {
                    System.out.println("posizione non valida!");
                    return false;
                }

                if (View.getGameBoard().getPlacingAvailableCells().contains(position))
                    initializedPositions.add(position);
                else {
                    System.out.println("posizzione non disponibile");
                    return false;
                }

                if (initializedPositions.size()==View.getPlayer().getPlayerNumber()) {
                    Controller.getMessage().setInitializedPositions(initializedPositions);
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean checkMessage(String viewObject) {
        if (super.checkMessage(viewObject)) {
            if (viewObject.length() != 2) {
                System.out.println("input errato");
                return false;
            } else return true;
        }
        return false;
    }

}