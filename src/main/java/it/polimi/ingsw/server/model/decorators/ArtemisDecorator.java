package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.model.TurnState.*;

public class ArtemisDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Artemis;

    private Position startingPosition=null;

    /**
     * decorate the object Command with Artemis's special power
     *
     * @param commands  represent the player behaviour
     */
    public ArtemisDecorator(Commands commands){
        this.commands=commands;
    }

    @Override
    public TurnState nextState(Player player) {
        switch (player.getState()) {
            case PLACING:
                player.setHasFinished(true);
            case WAIT:
                return MOVE;
            case MOVE:
                if (player.getSpecialFunction() && startingPosition != null)//ho già fatto prima mossa e Special Function
                    return MOVE;
                else{
                    startingPosition=null;
                    return BUILD;
                }
            case BUILD:
                player.setHasFinished(true);
                return WAIT;
            default:
                return WAIT;
    }}

    /**
     * worker may move one additional time but not back to the initial space
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        if(this.startingPosition==null)
            this.startingPosition=position;
        super.moveWorker(position, player);
    }

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
