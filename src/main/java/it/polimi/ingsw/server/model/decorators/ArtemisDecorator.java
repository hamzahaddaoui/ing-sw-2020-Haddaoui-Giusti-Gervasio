package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

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
                player.setUnsetSpecialFunction(true);
                if(player.hasSpecialFunction()){

                }
              /*  //  devo fare il secondo move
                if (secondMoveDone=false && this.startingPosition != null)
                    player.setTurnState(MOVE);
                else{
                    startingPosition=null;
                    secondMoveDone=false;
                    player.setTurnState(BUILD);
                }*/
                break;
            case BUILD:
                if (losingCondition(player)){
                    player.setHasFinished();
                }
                player.setTerminateTurnAvailable();
                break;
    }}

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

        if(this.startingPosition==null){
            this.startingPosition=position;
        }
        else this.secondMoveDone = true;

        super.moveWorker(position, player);
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

            if(this.startingPosition==null)
                return result;

            else{
            return  result
                    .stream()
                    .filter(position -> position != this.startingPosition)
                    .collect(Collectors.toSet());}
    }

}
