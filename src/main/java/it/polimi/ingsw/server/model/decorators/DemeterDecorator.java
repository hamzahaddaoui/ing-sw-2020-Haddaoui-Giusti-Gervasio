package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;
import static it.polimi.ingsw.utilities.TurnState.IDLE;

/**
 * @author giusti-leo
 *
 * Demeter Commands Decorator
 * Description: "Your Worker may build one additional time, but not on the same space"
 * Differente methods from Basic Commands: nextState, build, computeAvailableBuildings
 */

public class DemeterDecorator extends CommandsDecorator {

    static final private GodCards card = GodCards.Demeter;

    private Position firstBuildPosition;
    private boolean secondBuildDone;

    /**
     * decorate the object Command with Demeter's special power
     *
     * @param commands represent the player behaviour
     */
    public DemeterDecorator(Commands commands){
        this.commands=commands;
        this.firstBuildPosition=null;
    }

    /**
     * method that manage the selection of the next player's state
     * if Special Function is TRUE and the first building is done-> nextTurn is BUILD
     * else -> nextTurn is IDLE
     *
     * @param player  is the current player
     */
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                secondBuildDone = false;
                firstBuildPosition = null;
                player.setTurnState(MOVE);
                break;
            case MOVE:
                secondBuildDone = false;
                firstBuildPosition = null;
                player.setTurnState(BUILD);
                break;
            case BUILD:
                if (losingCondition(player) || secondBuildDone || player.getCurrentWorker().getAvailableCells(BUILD).size() == 1){
                    player.setHasFinished();
                }
                else{
                    player.setTerminateTurnAvailable();
                }
                break;
        }
    }

    /**
     * method of construction's action -> BasicCommands's build method
     * if it's the first building action, set the firstBuildPosition
     * if it's the second building action, reset the firstBuildPosition
     *
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Position position, Player player) {
        if (this.firstBuildPosition == null){
            this.firstBuildPosition = position;
        }
        else {
           this.secondBuildDone = true;
            }

        super.build(position, player);
    }

    /**
     * method that compute the set of the available buildings
     * if it's the first build action, BasicCommands's computeAvailableBuildings method
     * if it's the second build action, BasicCommands's computeAvailableBuildings method less firstBuildPosition
     *
     * @param player  is the current player
     * @param worker  is the current worker
     * @return  the set of the position where the worker can build on
     */
    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        Set<Position> result = super.computeAvailableBuildings(player, worker);

            if(this.firstBuildPosition == null)
                return result;
            else{
                return result
                        .stream()
                        .filter(position -> position.getX() != firstBuildPosition.getX() || position.getY() != firstBuildPosition.getY())
                        .collect(Collectors.toSet());
            }
        }

}
