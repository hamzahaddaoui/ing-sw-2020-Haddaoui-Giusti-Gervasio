package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

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
                break;
            case BUILD:
                if (player.hasSpecialFunction() && hasBuiltBeforeMoving)
                    player.setTurnState(MOVE);
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


    @Override
    public void notifySpecialFunction(Player player){
        Billboard billboard = player.getMatch().getBillboard();
        Position avoidPosition;

        if (player.hasSpecialFunction()){
            player.setUnsetSpecialFunctionAvailable(null);
            if (checkCells(player,player.getCurrentWorker())) {
                avoidPosition = player.getCurrentWorker().getAvailableCells(MOVE).stream()
                        .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(player.getCurrentWorker().getPosition()))
                        .findAny().get();
                player.getCurrentWorker().getAvailableCells(BUILD).remove(avoidPosition); }
            player.setTurnState(BUILD);
        }
    }

    /**
     * Method that checks if the boolean special function available can be true.
     * <p>
     * If there's only one cell in the building available cells and that cell has a tower height == 0,
     * you can build in it and then move in it too.
     * Else if there's only one cell in the building available cell and that cell has a different tower height
     * (i.e. tower height == 1) the player can't build in it because after that he won't be able to move in that cell.
     * Else the method does the standard losing condition check.
     *
     *
     * @param player  the current player of the match
     * @return        true if you can build before move, false otherwise
     */

    private Map<Position, Boolean> canBuildBeforeMove(Player player){
        return player.getWorkers().stream().collect(Collectors.toMap(Worker::getPosition, worker -> worker.canDoSomething(BUILD)));



        //return player.getWorkers().stream().noneMatch(worker -> worker.canDoSomething(BUILD));

        /*boolean retVal;
        Worker worker = player.getCurrentWorker();
        //if (worker.getAvailableCells(state)==null)
            //worker.setAvailableCells(state,player.getCommands().computeAvailableBuildings(player,worker));
        Set<Position> buildingPositions = worker.getAvailableCells(BUILD);
        TurnState prec = player.getTurnState();
        player.setTurnState(BUILD);
        if (buildingPositions.size()==1) {
            retVal = buildingPositions.stream().anyMatch(position -> player.getMatch().getBillboard().getTowerHeight(position) == 0);
        }
        else retVal = !losingCondition(player);
        player.setTurnState(prec);
        return retVal;*/
    }

    private boolean checkCells(Player player, Worker worker) {
      Billboard billboard = player.getMatch().getBillboard();

      long num = worker.getAvailableCells(MOVE).stream()
              .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(worker.getPosition()))
              .count();
      return num == 1;
    }
}
