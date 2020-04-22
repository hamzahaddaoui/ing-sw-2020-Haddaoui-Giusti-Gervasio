package it.polimi.ingsw.server.model.decorators;

import com.sun.jdi.event.StepEvent;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

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
                player.setUnsetSpecialFunctionAvailable(canBuildBeforeMove(player, BUILD));
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
                    .filter(position -> billboard.getPlayer(position) == null)
                    .filter(position -> (billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition)))
                    .filter(position -> !billboard.getDome(position))
                    .collect(Collectors.toSet());
        }
    }


    @Override
    public void notifySpecialFunction(Player player){
        if (player.hasSpecialFunction()) {
            player.setUnsetSpecialFunctionAvailable(false);
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
     * @param state   always BUILD, the state you have to check
     * @return        true if you can build before move, false otherwise
     */

    private boolean canBuildBeforeMove(Player player, TurnState state){

        boolean retVal;
        Set<Position> buildingPositions = player.getCurrentWorker().getAvailableCells(state);
        TurnState prec = player.getTurnState();
        player.setTurnState(state);
        if (buildingPositions.size()==1) {
            retVal = buildingPositions.stream().anyMatch(position -> player.getMatch().getBillboard().getTowerHeight(position) == 0);
        }
        else retVal = losingCondition(player);
        player.setTurnState(prec);
        return retVal;

    }
}
