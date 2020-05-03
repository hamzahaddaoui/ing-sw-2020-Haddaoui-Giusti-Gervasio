package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

public class PlacingWorkersStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) throws IllegalArgumentException{

        if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getGameBoard().getPlacingAvailableCells() == null || View.getGameBoard().getPlacingAvailableCells().size() == 0)
            throw new IllegalArgumentException(" PlacingAvailableCell is empty ");

        if(View.getGameBoard().getColoredPosition() == null)
            View.getGameBoard().setColoredPosition( View.getGameBoard().getPlacingAvailableCells().stream().findAny().get());

        if(!View.getGameBoard().getPlacingAvailableCells().contains(View.getGameBoard().getColoredPosition()))
            throw new IllegalArgumentException(" Colored Position is not in Placing Available Cells ");

        return commandCharacter.executePlacingWorkerStatus();

    }

}
