package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.MOVE;

/**
 * @author giusti-leo
 *
 * Zeus Commands Decorator
 * Description: "Your worker may build a block under itself"
 * Differente methods from Basic Commands: computeAvailableBuildings
 */

public class ZeusDecorator  extends CommandsDecorator {

    static final private GodCards card = GodCards.Zeus;

    private Position positionBuilt;

    public ZeusDecorator(Commands commands){
        this.commands=commands;
    }

    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                positionBuilt = null;
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setTurnState(BUILD);
                break;
            case BUILD:
                player.setHasFinished();
                break;
        }
    }

    /**
     * This method computes the available buildings. Zeus can build in his same position.
     *
     * @param player  is the current player
     * @param worker  is the current worker for the turn
     * @return  the set of available cells for the building turn
     */
    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        Set<Position> result = super.computeAvailableBuildings(player, worker);
        if(player.getCurrentWorker() != null && player.getCurrentWorkerPosition().getZ() < 3)
            result.add(player.getCurrentWorkerPosition());
        return result;
    }

    /**
     * This method makes the building action and it saves the position where the current worker build
     *
     * @param position  is the position that player have inserted
     * @param player  is the current player
     */
    @Override
    public void build(Position position, Player player) {
        Position workerPosition = player.getCurrentWorker().getPosition();
        Worker worker = player.getCurrentWorker();
        Billboard billboard = player.getMatch().getBillboard();

        if(position.getY() == workerPosition.getY() && position.getX() == workerPosition.getX()){
            super.build(position, player);
            positionBuilt = new Position( position.getX(), position.getY(), billboard.getTowerHeight(position));
            worker.setPosition(positionBuilt);
        }
        else{
            super.build(position,player);
        }
    }


    /**
     * Standard winning condition method and an addition condition.
     * Zeus does not win by forcing itself in the third level cells, so if the worker is on the same cell where he has built
     * he does not win
     *
     * @param player  is the current player
     * @return  true if the player has won, false if he does not
     */
    @Override
    public boolean winningCondition(Player player) {
        Worker worker = player.getCurrentWorker();
        if (worker == null)
            return false;

        return super.winningCondition(player) && positionBuilt == null;
    }




}
