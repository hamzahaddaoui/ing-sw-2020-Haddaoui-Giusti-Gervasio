package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;

public class PlacingWorkersStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) {

        if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();

        if(View.getGameBoard().getPlacingAvailableCells() == null)
            throw new IllegalArgumentException(" PlacingAvailableCell is empty ");

        if(View.getGameBoard().getColoredPosition() == null)
            View.getGameBoard().setColoredPosition( View.getGameBoard().getPlacingAvailableCells().stream().findAny().get());

        if(!View.getGameBoard().getPlacingAvailableCells().contains(View.getGameBoard().getColoredPosition()))
            throw new IllegalArgumentException(" Colored Position is not in Placing Available Cells ");

        return commandCharacter.executePlacingWorkerStatus();

    }

    @Override
    public void nextState(Controller ctrl) {

        if (ctrl.getPlayerState()== null)
            throw new IllegalArgumentException(" PlayersState is null ");
        if (ctrl.getMatchState()== null)
            throw new IllegalArgumentException(" MatchState is null ");

        if (ctrl.getMatchState() == MatchState.RUNNING && ctrl.getPlayerState() == PlayerState.ACTIVE)
            ctrl.setState(new RunningStatus());
        else ctrl.setState(new WaitingStatus());
    }
}
