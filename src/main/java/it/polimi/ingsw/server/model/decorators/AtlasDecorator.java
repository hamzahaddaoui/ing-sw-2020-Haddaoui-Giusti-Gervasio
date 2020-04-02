package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;

public class AtlasDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Atlas;

    private boolean domeBuild;

    /**
     * decorate the object Command with Atlas's special power
     *
     * @param commands  represent the player behaviour
     */
    public AtlasDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that allows the stardard placing movement
     * also if the selected position is free
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void placeWorker(Position position, Player player) {
        super.placeWorker(position, player);
    }

    /**
     * method that allows the stardard player movement
     *
     * @param position the position where whe worker has to move
     * @param player the player which is performing the command
     *
     */
    @Override
    public void moveWorker(Position position, Player player) {
        super.moveWorker(position, player);
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
        build(position, player, domeBuild);
    }

    /**
     * return the spaces that are available after a check on billboard
     *
     * @param player reference to the player
     * @return
     */
    @Override
    public Set<Position> getAvailableCells(Player player) {
        return super.getAvailableCells(player);
    }

    /**
     * Activates (deactivates) special function related to a certain player
     *
     */
    @Override
    public void specialFunctionSetUnset() {
        domeBuild ^= true;
    }
}
