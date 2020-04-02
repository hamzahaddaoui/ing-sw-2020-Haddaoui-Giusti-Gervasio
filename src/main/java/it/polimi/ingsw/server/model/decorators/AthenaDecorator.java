package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;

public class AthenaDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Athena;

    private int movesBeforeBuild;
    private int numOfBuilds;
    private int movesAfterBuild;

    /**
     * decorate the object Command with Athena's special power
     *
     * @param commands  represent the player behaviour
     */
    public AthenaDecorator(Commands commands){
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

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position  is the position that player have inserted
     */
    @Override
    public void build(Position position, Player player) {
        super.build(position, player);
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
}
