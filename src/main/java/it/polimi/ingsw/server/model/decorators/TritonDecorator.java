package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TritonDecorator extends CommandsDecorator {

    static final GodCards card = GodCards.Triton;

    public TritonDecorator(Commands commands) {this.commands=commands;}

    private Set<Position> avoidPositions;

    //TODO PROBLEMA DI SCONFITTA IN CASO DI GIRO COMPLETO

    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                avoidPositions = new HashSet<>();
                player.setTurnState(TurnState.MOVE);
                break;
            case MOVE:
                Worker currentWorker = player.getCurrentWorker();
                if (player.hasSpecialFunction() && computeAvailableMovements(player,currentWorker).size()!=0 && isPerimeterSpace(currentWorker.getPosition()))
                    player.setTurnState(TurnState.MOVE);
                else player.setTurnState(TurnState.BUILD);
                break;
            case BUILD:
                player.setHasFinished();
                break;
        }
    }

    @Override
    public void moveWorker(Position position, Player player) {

        super.moveWorker(position, player);

        if (isPerimeterSpace(position)) {
            avoidPositions.add(position);
                Map<Position, Boolean> specialFunctionAvailable = new HashMap<>();
                specialFunctionAvailable.put(position, true);
                player.setUnsetSpecialFunctionAvailable(specialFunctionAvailable);
        }
        else player.setUnsetSpecialFunctionAvailable(null);

    }

    @Override
    public void notifySpecialFunction(Player player) {
        if (player.hasSpecialFunction()) {
            Worker currentWorker = player.getCurrentWorker();
            player.setTurnState(TurnState.MOVE);
            currentWorker.setAvailableCells(TurnState.MOVE,computeAvailableMovements(player,currentWorker));
        }
        else player.setTurnState(TurnState.BUILD);
    }

    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        if (avoidPositions.size()!=0) {
            return super.computeAvailableMovements(player, worker).stream().filter(position -> !avoidPositions.contains(position)).collect(Collectors.toSet()); }
        return super.computeAvailableMovements(player, worker);
    }

    private boolean isPerimeterSpace(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x == 0 || x == 4 || y == 0 || y == 4;
    }
}
