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

    static final private GodCards card = GodCards.Triton;

    public TritonDecorator(Commands commands) {this.commands=commands;}

    private Set<Position> avoidPositions;

    /**
     * Method that sets the next state of the player.
     * <p>
     * After the first move, if the player is in a perimeter space and he has set the special function,
     * he continues to move until he ends in a non perimeter space.
     *
     * @param player  the player who makes the turn, not null
     */
    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                avoidPositions = new HashSet<>();
                player.setTurnState(TurnState.MOVE);
                break;
            case MOVE:
                Worker currentWorker = player.getCurrentWorker();
                if (player.hasSpecialFunction() && computeAvailableMovements(player,currentWorker).size()!=0 && isPerimeterSpace(player.getCurrentWorkerPosition()))
                    player.setTurnState(TurnState.MOVE);
                else
                    player.setTurnState(TurnState.BUILD);
                break;
            case BUILD:
                player.setHasFinished();
                break;
        }
    }

    /**
     * Method that allows the specific moving action of Triton.
     * <p>
     * If the player ends in a perimeter position, the method saves the position in
     * the avoid positions set and decide if the special function can be available or not.
     *
     * @param position  is the position that player have inserted, not null
     * @param player    it the player who makes the move, not null
     */
    @Override
    public void moveWorker(Position position, Player player) {

        super.moveWorker(position, player);

        if (isPerimeterSpace(position)) {
            avoidPositions.add(position);
            Map<Position, Boolean> specialFunctionAvailable = new HashMap<>();
            specialFunctionAvailable.put(position, true);
            player.setUnsetSpecialFunctionAvailable(specialFunctionAvailable);
            if (computeAvailableMovements(player,player.getCurrentWorker()).size()==0)
                player.setUnsetSpecialFunctionAvailable(null);
        }
        else player.setUnsetSpecialFunctionAvailable(null);

    }

    /**
     * Method that modifies the standard turn after the special function activation.
     * <p>
     * If the special function has been activate, the method sets the turn state at MOVE
     * and calculate the new available cells.
     *
     * @param player the current player of the match, not null
     */
    @Override
    public void notifySpecialFunction(Player player) {
        if (player.hasSpecialFunction()) {
            Worker currentWorker = player.getCurrentWorker();
            player.setTurnState(TurnState.MOVE);
            currentWorker.setAvailableCells(TurnState.MOVE,computeAvailableMovements(player,currentWorker));
        }
        else player.setTurnState(TurnState.BUILD);
    }

    /**
     * Returns the spaces that are available after a check in the billboard.
     * <p>
     * If the player has already done at least one move, the method removes all the avoid positions
     * from the available movements cells.
     *
     * @param player  the player who makes the move, not null
     * @param worker  the current worker of the player, not null
     * @return        the spaces which are available
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        if (avoidPositions.size()!=0) {
            return super.computeAvailableMovements(player, worker).stream().filter(position -> !avoidPositions.contains(position)).collect(Collectors.toSet()); }
        return super.computeAvailableMovements(player, worker);
    }

    /**
     * Method that defines if a space is a perimeter one.
     * <p>
     * If x or y are between 0 and 4, the method return true; else otherwise.
     *
     * @param position  the position you want to define, not nul
     * @return  true if it's a perimeter one, false otherwise
     */
    private boolean isPerimeterSpace(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x == 0 || x == 4 || y == 0 || y == 4;
    }
}
