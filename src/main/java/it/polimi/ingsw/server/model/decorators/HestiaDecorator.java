package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.MOVE;

public class HestiaDecorator extends CommandsDecorator {

    static final private GodCards card = GodCards.Hestia;

    public HestiaDecorator(Commands commands) {this.commands=commands;}

    private boolean secondBuildDone;
    private Position firstBuildPosition;

    /**
     * Method that sets the next state of the player.
     * <p>
     * If the player has done just one building turn and there are not only perimeter cells
     * in his available building cells, the method sets true the available end turn and give
     * the player the chance to build again.
     *
     * @param player  the player who makes the turn, not null
     */
    @Override
    public void nextState(Player player) {
        switch (player.getTurnState()) {
            case IDLE:
                firstBuildPosition = null;
                secondBuildDone = false;
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setTurnState(BUILD);
                break;
            case BUILD:
                if (losingCondition(player) || secondBuildDone || player.getCurrentWorker().getAvailableCells(BUILD).stream().allMatch(this::isPerimeterSpace))
                    player.setHasFinished();
                else
                    player.setTerminateTurnAvailable();
                break;
        }
    }

    /**
     * Method that allows the specific building block action of Hestia.
     * <p>
     * If it's the first building turn and the position where the player wants to build
     * is now of level 4 (with dome in it) the method remove that cell from the available
     * for the second build.
     * Both the building moves are the standard ones.
     *
     * @param player     the player who makes the building move, not null
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Position position, Player player) {
        if (firstBuildPosition == null) {

            firstBuildPosition = position;

            super.build(position,player);

            if (player.getMatch().getBillboard().getDome(firstBuildPosition))
                player.getCurrentWorker().getAvailableCells(BUILD).remove(firstBuildPosition);
        }
        else {
            secondBuildDone = true;
            super.build(position,player);
        }
    }

    /**
     * Returns the spaces that are available for building after a check in the billboard.
     * <p>
     * If the player has already done the first building turn, the method removes all the
     * perimeter cells from the available buildings cells.
     *
     * @param player  the player who makes the move, not null
     * @param worker  the current worker of the player, not null
     * @return        the spaces which are available
     */
    @Override
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        if (firstBuildPosition != null) {
            return super.computeAvailableBuildings(player, worker).stream().filter(position -> !isPerimeterSpace(position)).collect(Collectors.toSet());
        }
        else return super.computeAvailableBuildings(player, worker);
    }

    /**
     * Method that defines if a space is a perimeter one.
     * <p>
     * If x or y are between 0 and 4, the method return true; else otherwise.
     *
     * @param position  the position you want to define, not nul
     * @return  true if it's a perimeter one, false otherwise
     */
    private boolean isPerimeterSpace(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x == 0 || x == 4 || y == 0 || y == 4;
    }
}
