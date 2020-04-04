package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;

public class AtlasDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Atlas;

    /**
     * decorate the object Command with Atlas's special power
     *
     * @param commands  represent the player behaviour
     */
    public AtlasDecorator(Commands commands){
        this.commands=commands;
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
        if (player.isSpecialFunction())
            player.getMatch().getBillboard().setDome(position);
        else
            super.build(position, player);
    }
}
