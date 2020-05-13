package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

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
                if(!secondMoveDone)
                    player.setUnsetSpecialFunctionAvailable(canDoSecondMove(player));
                player.setTurnState(BUILD);
                break;
            case BUILD:
                startingPosition = null;
                secondMoveDone = false;
                player.setHasFinished();
                break;
        }
    }

    /**
     * Method that is called if the gamer set the special function.
     * If the gamer call it (FALSE) -> from true to false as OR function , the next turn will be MOVE.
     * Reset of Map of setUnsetSpecialFunctionAvailable
     *
     * @param player
     */
    @Override
    public void notifySpecialFunction(Player player){
        if (player.hasSpecialFunction()){
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
        if(startingPosition != null)
            secondMoveDone = true;
        if(!secondMoveDone){
            startingPosition = player.getCurrentWorker().getPosition();
        }
        super.moveWorker( position , player );
        if(!secondMoveDone){
            player.setUnsetSpecialFunctionAvailable(canDoSecondMove(player));
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
        if(this.startingPosition == null){
            return super.computeAvailableMovements(player, worker);
        }
        else{
            return super
                    .computeAvailableMovements(player, worker)
                    .stream()
                    .filter(p -> startingPosition.getY() != p.getY() || startingPosition.getX() != p.getX())
                    .collect(Collectors.toSet());
        }
    }

    /**
     * Method that establish if the current worker can move for the second time
     *
     * @param player  is the current player
     * @return  true can move for the second time, else false
     */
    private Map<Position, Boolean> canDoSecondMove(Player player){
        player.getCurrentWorker().setAvailableCells(MOVE,computeAvailableMovements(player,player.getCurrentWorker()));

        return player
                .getWorkers()
                .stream()
                .filter(worker -> worker.getPosition().getY() == player.getCurrentWorker().getPosition().getY() &&
                        worker.getPosition().getX() == player.getCurrentWorker().getPosition().getX())
                .collect(Collectors.toMap(Worker::getPosition, worker -> worker.canDoSomething(MOVE)));
    }

}