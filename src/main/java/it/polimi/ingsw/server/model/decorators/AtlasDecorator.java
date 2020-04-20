package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;
import it.polimi.ingsw.utilities.TurnState;

import static it.polimi.ingsw.utilities.TurnState.*;

public class AtlasDecorator extends CommandsDecorator {

    /**
     * decorate the object Command with Atlas's special power
     *
     * @param commands  represent the player behaviour
     */
    public AtlasDecorator(Commands commands){
        this.commands=commands;
    }

    @Override
    public TurnState nextState(Player player) {
        switch(player.getTurnState()){
            case IDLE:
                return MOVE;
            case MOVE:
                player.setUnsetSpecialFunctionAvailable(true);
                return BUILD;
            case BUILD:
                player.setHasFinished();
                return IDLE;
        }
        return null;
    }


    /**
     * method that allows the standard building block action
     * the Atlas player can build a block or a dome, depending on the special function activation
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Position position, Player player) {
        if (player.getSpecialFunction())
            player.getMatch().getBillboard().setDome(position);
        else
            super.build(position, player);
    }
}
