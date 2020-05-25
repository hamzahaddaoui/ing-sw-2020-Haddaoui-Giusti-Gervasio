package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

/**
 * @author Vasio1298
 */

public class PrometheusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Prometheus;
    public PrometheusDecorator(Commands commands){
        this.commands=commands;
    }

    private static boolean hasBuiltBeforeMoving;

    /**
     * Method that sets the next state of the player.
     * <p>
     * If the player sets the special function, from WAIT the turn shifts to BUILD
     * then if the player has done just his first building move, the turn shifts to MOVE
     * otherwise, the player has finished his turn, sets the boolean and the turn shifts to WAIT.
     * Else, the turn follows his standard shifting.
     *
     * @param player  the player who makes the turn, not null
     */
    @Override
    public void nextState(Player player) {
        switch(player.getTurnState()){
            case IDLE:
                hasBuiltBeforeMoving = false;
                player.setUnsetSpecialFunctionAvailable(canBuildBeforeMove(player));
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setTurnState(BUILD);
                player.setUnsetSpecialFunctionAvailable(null);
                break;
            case BUILD:
                if (player.hasSpecialFunction() && hasBuiltBeforeMoving) {
                    player.setTurnState(MOVE);
                    player.setUnsetSpecialFunctionAvailable(null);
                }
                else player.setHasFinished();
                break;
        }
    }

    /**
     * Method that allows the specific building block action of Prometheus.
     * <p>
     * If the player sets the special function and it's his first building move, after the standard move,
     * the method sets the boolean at true.
     * In all the other case, the player does the standard building move
     * and the method sets the boolean at false.
     *
     * @param player     the player who makes the building move, not null
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Position position, Player player) {
        if (player.hasSpecialFunction() && !hasBuiltBeforeMoving) {
            super.build(position,player);
            hasBuiltBeforeMoving = true;
        }
        else {
            super.build(position, player);
            hasBuiltBeforeMoving = false;
        }
    }


    /**
     * Returns the spaces that are available after a check in the billboard.
     * <p>
     * If the player set the boolean special function, ha can't move up this turn so
     * this method checks if the space is free, if has height less than or equal to the current space
     * and if there isn't a dome in it.
     * Else, it's not a special function and the method returns the basic cells available for movements.
     *
     * @param player  the player who makes the move, not null
     * @return        the spaces which are available
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {

        if (!player.hasSpecialFunction())
            return super.computeAvailableMovements(player, worker);
        else {
            Billboard billboard = player.getMatch().getBillboard();
            Position currentPosition = player.getCurrentWorker().getPosition();

            return currentPosition
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == 0)
                    .filter(position -> (billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition)))
                    .filter(position -> !billboard.getDome(position))
                    .collect(Collectors.toSet());
        }
    }

    /**
     * Method that modifies the standard turn after the special function activation.
     * <p>
     * If the special function has been activate, the method checks for both worker if the check cells method returns true.
     * If it's true, the method remove the specific cell from the available building cells of the specific worker.
     * After that the method does the check for both workers, this sets the turn state at BUILD.
     *
     * @param player the current player of the match
     */

    @Override
    public void notifySpecialFunction(Player player){
        Billboard billboard = player.getMatch().getBillboard();
        Position avoidPosition;
        ArrayList<Worker> workers = new ArrayList<>(player.getWorkers());
        Worker worker;

        if (player.hasSpecialFunction()){
            //player.setUnsetSpecialFunctionAvailable(null);
            for (int i=0;i<2;i++) {
                worker = workers.get(i);
                if (checkCells(player, worker)) {
                    Position workerPosition = worker.getPosition();
                    avoidPosition = worker.getAvailableCells(MOVE).stream()
                            .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(workerPosition))
                            .findAny().get();
                    if (billboard.getTowerHeight(avoidPosition) == billboard.getTowerHeight(workerPosition))
                        worker.getAvailableCells(BUILD).remove(avoidPosition);
                }
            }
            player.setTurnState(BUILD);
        }
        else player.setTurnState(MOVE);
    }

    /**
     * Method that checks if the boolean special function available can be true.
     * <p>
     * The method controls for both workers if they have at least one building available cells.
     *
     *
     * @param player  the current player of the match
     * @return        true if you can build before move, false otherwise
     */

    private Map<Position, Boolean> canBuildBeforeMove(Player player){
        player.setAvailableCells();
        if (player.getWorkers().stream().allMatch(worker -> worker.getAvailableCells(BUILD).size()==1 && checkCells(player,worker)))
            return null;
        else return player.getWorkers().stream().collect(Collectors.toMap(Worker::getPosition, worker -> worker.canDoSomething(BUILD) &&
                !(worker.getAvailableCells(BUILD).size()==1 && checkCells(player,worker))));
    }


    /**
     * Method that analyze how many cells are available to move into, after the first building turn.
     * <p>
     * The method confronts every available cells of the worker with the starting cell
     * and counts every cell that has an height tower of same or lower level then the starting one.
     *
     * @param player the current player of the match
     * @param worker the worker of the player
     * @return true if there's only one cell available, false otherwise
     */
    private boolean checkCells(Player player, Worker worker) {
      Billboard billboard = player.getMatch().getBillboard();

      long num = worker.getAvailableCells(MOVE).stream()
              .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(worker.getPosition()))
              .count();
      return num == 1;
    }
}
