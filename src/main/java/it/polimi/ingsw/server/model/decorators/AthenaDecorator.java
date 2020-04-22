package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

public class AthenaDecorator extends CommandsDecorator {
    /**
     * decorate the object Command with Athena's special power
     *
     * @param commands  represent the player behaviour
     */
    public AthenaDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that allows the stardard player movement
     * if the player moves up, the other players aren't allowed to moved up until the next Athena turn
     *
     * @param position the position where whe worker has to move
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
