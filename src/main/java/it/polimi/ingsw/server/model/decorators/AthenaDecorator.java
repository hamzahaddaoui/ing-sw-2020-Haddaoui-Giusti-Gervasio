package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.MOVE;

/**
 * @author: hamzahaddaoui
 *
 * Redefinition of methods related to the Athena power.
 */

public class AthenaDecorator extends CommandsDecorator {

    static final private GodCards card = GodCards.Athena;
    /**
     * decorate the object Command with Athena's special power
     *
     * @param commands  represent the player behaviour
     */
    public AthenaDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that changes the current state of the player
     * If the state is IDLE then the player can MOVE
     * If the state is MOVE then the player can BUILD
     * If the state is BUILD then the player goes IDLE
     * @param player the match current player
     */
    @Override
    public void nextState(Player player){
        switch(player.getTurnState()){
            case IDLE:
                player.getMatch().setMoveUpActive(true);
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setTurnState(BUILD);
                break;
            case BUILD:
                player.setHasFinished();
        }
    }

    /**
     * method that allows the stardard player movement
     * if the player moves up, the other players aren't allowed to moved up until the next Athena turn
     *
     * @param position the position where the worker has to move
     * @param player the player which is performing the command
     *
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Match match = player.getMatch();
        int startHeight, endHeight;

        startHeight = player.getCurrentWorker().getPosition().getZ();
        super.moveWorker(position, player);
        endHeight = player.getCurrentWorker().getPosition().getZ();

        if (startHeight - endHeight < 0)
            match.setMoveUpActive(false);
        else
            match.setMoveUpActive(true);
    }

    /**
     * Checks if the player has lost.
     * If the player has lost, the moveUp is reactivated if disabled
     * @param player the player which is performing the command
     * @return if the player has lost
     */
    @Override
    public boolean losingCondition(Player player){
        if (super.losingCondition(player)){
            player.getMatch().setMoveUpActive(true);
            return true;
        }
        else{
            return false;
        }
    }

}
