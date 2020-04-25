package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

/**
 * @author giusti-leo
 *
 * Artemis Commands Decorator
 * Description: "Your Worker may move one additional time, but not back to its initial space"
 * Differente methods from Basic Commands: nextState, moveWorker, computeAvailableMovements
 */

public class ArtemisDecorator extends CommandsDecorator {

    static final GodCards card = GodCards.Artemis;

    private Position startingPosition=null;
    private boolean secondMoveDone;

    /**
     * decorate the object Command with Artemis's special power
     *
     * @param commands  represent the player behaviour
     */
    public ArtemisDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that manage the selection of the next player's state
     * if Special Function is TRUE and the first movement is done-> nextTurn is MOVE
     * else -> nextTurn is BUILD
     *
     * @param player  is the current player
     */
    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                startingPosition = null;
                secondMoveDone = false;
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

    @Override
    public void notifySpecialFunction(Player player){
        if (!player.hasSpecialFunction()){
            player.setUnsetSpecialFunctionAvailable(null);
            player.setTurnState(MOVE);
        }
    }

    /**
     * change the position of the worker with BasicCommands's moveWorker method
     * if it's the first movement, set the starting position
     * if it's the second movement, reset the starting position
     *
     *  @param position  is the position that player have inserted
     * @param player  is the current player
     */
    @Override
    public void moveWorker(Position position, Player player) {

        Position currentPosition = player.getCurrentWorker().getPosition();

        super.moveWorker(position, player);

        player.getWorkersAvailableCells().remove(currentPosition);
        player.getWorkers().stream().forEach(worker -> {
            worker.setAvailableCells(MOVE, this.computeAvailableMovements(player, worker));
            worker.setAvailableCells(BUILD, this.computeAvailableBuildings(player, worker));
        });
        player.getWorkersAvailableCells();

        if(this.startingPosition==null){
            this.startingPosition=currentPosition;
            player.setUnsetSpecialFunctionAvailable(canDoSecondMove(player));
        }
        else{
            this.secondMoveDone = true;
        }
    }

    /**
     * method that compute the set of position where the current worker can move in
     * if it's the first movement, the available position are the same of the Basic Commands
     * if it's the second movement, the available position are the same of the Basic Commands less the old starting position
     *
     * @param player  is the current player
     * @param worker  is the current worker
     * @return the set of position where the player can move in
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
            Set<Position> result = super.computeAvailableMovements(player, worker);

            result
                .stream()
                .forEach(position -> System.out.print(" *" + position + "* ,"));
            System.out.println();

            if(this.startingPosition == null && secondMoveDone == false)
                return result;

            else{
            return  result
                    .stream()
                    .filter(position -> player.getCurrentWorker() == )
                    .filter(position -> position.getX() != this.startingPosition.getX() &&
                            position.getY() != this.startingPosition.getY())
                    .collect(Collectors.toSet());}
    }

    /**
     * Method that establish if the current worker can move for the second time
     *
     * @param player  is the current player
     * @return  true can move for the second time, else false
     */
    private Map<Position, Boolean> canDoSecondMove(Player player){

        return player
                .getWorkers()
                .stream()
                .filter(worker -> worker.getPosition().getY() == player.getCurrentWorker().getPosition().getY())
                .filter(worker -> worker.getPosition().getX() == player.getCurrentWorker().getPosition().getX())
                .collect(Collectors.toMap(Worker::getPosition, worker -> worker.canDoSomething(MOVE)));

    }

}
