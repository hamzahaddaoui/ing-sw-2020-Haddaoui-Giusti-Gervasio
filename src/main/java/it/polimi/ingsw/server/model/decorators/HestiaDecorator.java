package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.MOVE;

public class HestiaDecorator extends CommandsDecorator {

    static final GodCards card = GodCards.Hestia;

    public HestiaDecorator(Commands commands) {this.commands=commands;}

    private boolean secondBuildDone;
    private Position firstBuildPosition;

    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                firstBuildPosition = null;
                secondBuildDone = false;
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setTurnState(BUILD);
                break;
            case BUILD:
                if (losingCondition(player) || secondBuildDone || player.getCurrentWorker().getAvailableCells(BUILD).stream().allMatch(this::isPerimeterSpace))
                    player.setHasFinished();
                else
                    player.setTerminateTurnAvailable();
                break;
        }
    }

    @Override
    public void build(Position position, Player player) {
        if (firstBuildPosition == null) {

            firstBuildPosition = position;

            super.build(position,player);

            if (player.getMatch().getBillboard().getDome(firstBuildPosition))
                player.getCurrentWorker().getAvailableCells(BUILD).remove(firstBuildPosition);
        }
        else {
            secondBuildDone = true;
            super.build(position,player);
        }
    }

    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        if (firstBuildPosition != null) {
            return super.computeAvailableBuildings(player, worker).stream().filter(position -> !isPerimeterSpace(position)).collect(Collectors.toSet());
        }
        else return super.computeAvailableBuildings(player, worker);
    }

    private boolean isPerimeterSpace(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x == 0 || x == 4 || y == 0 || y == 4;
    }
}
