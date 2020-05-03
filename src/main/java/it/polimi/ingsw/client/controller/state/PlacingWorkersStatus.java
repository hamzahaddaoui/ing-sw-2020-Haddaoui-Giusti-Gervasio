package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;

public class PlacingWorkersStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException {

        super.checkMessage(viewObject);

        /*if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");*/
        if (viewObject.length() != 4)
            throw new IllegalArgumentException();

        Set<Position> initializedPositions = new HashSet<>();
        Position position;

        for (int i=0;i<2;i++) {
            int x = Character.getNumericValue(viewObject.charAt(i * 2));
            int y = Character.getNumericValue(viewObject.charAt(i * 2 + 1));

            if (x<=4 && x>=0 && y<=4 && y>=0)
                position = new Position(x, y);
            else throw new IllegalArgumentException("\nposizione " + (i + 1) + " non valida");

            if (View.getGameBoard().getPlacingAvailableCells().contains(position))
                initializedPositions.add(position);
            else throw new IllegalArgumentException("\nposizzione " + (i + 1) + " non disponibile");
        }

        Controller.getMessage().setInitializedPositions(initializedPositions);
        return true;


        /*InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getGameBoard().getPlacingAvailableCells() == null)
            throw new IllegalArgumentException(" PlacingAvailableCell is empty ");

        if(View.getGameBoard().getColoredPosition() == null)
            View.getGameBoard().setColoredPosition( View.getGameBoard().getPlacingAvailableCells().stream().findAny().get());

        if(!View.getGameBoard().getPlacingAvailableCells().contains(View.getGameBoard().getColoredPosition()))
            throw new IllegalArgumentException(" Colored Position is not in Placing Available Cells ");

        return commandCharacter.executePlacingWorkerStatus();*/
    }

}