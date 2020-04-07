package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.model.TurnState.*;

public class PrometheusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Prometheus;
    public PrometheusDecorator(Commands commands){
        this.commands=commands;
    }

    private boolean hasBuiltBeforeMoving;

    /**
     * Method that sets the next state of the player.
     * <p>
     * If the player sets the special function, from WAIT the turn shifts to BUILD
     * then if the player has done just his first building move, the turn shifts to MOVE
     * otherwise, the player has finished his turn, sets the boolean and the turn shifts to WAIT.
     * Else, the turn follows his standard shifting.
     *
     * @param player  the player who makes the turn, not null
     * @return  the next turn state in which the player is
     */
    @Override
    public TurnState nextState(Player player) {
        switch(player.getState()){
            case WAIT:
                if (player.getSpecialFunction())
                return BUILD;
                else return MOVE;
            case MOVE:
                return BUILD;
            case BUILD:
                if (player.getSpecialFunction() && hasBuiltBeforeMoving)
                    return MOVE;
                else player.setHasFinished(true);
            default:
                return WAIT;
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
        if (player.getSpecialFunction() && !hasBuiltBeforeMoving) {
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

        if (!player.getSpecialFunction())
            return super.computeAvailableMovements(player, worker);
        else {
            Billboard billboard = player.getMatch().getBillboard();
            Position currentPosition = player.getCurrentWorker().getPosition();

            return currentPosition
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == -1)
                    .filter(position -> (billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition)))
                    .filter(position -> !billboard.getDome(position))
                    .collect(Collectors.toSet());
        }
    }
}
