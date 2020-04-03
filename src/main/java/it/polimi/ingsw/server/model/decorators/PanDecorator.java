package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;

public class PanDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Pan;

    public PanDecorator(Commands commands){
        this.commands=commands;
    }

    protected Set<Position> availablePlacing = new HashSet<>();
    protected Set<Position> availableMovements = new HashSet<>();
    protected Set<Position> availableBuildings = new HashSet<>();

    /**
     * Method who implements the standard move.
     * <p>
     * At the end of the move, you check the winningCondition.
     *
     * @param position  the position that player have inserted, not null
     * @param player    the player who makes the move, not null
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();
        Position startingPosition = worker.getPosition();

        if (!availableMovements.contains(position))
            return;

        position.setZ(billboard.getTowerHeight(position));
        billboard.resetPlayer(worker.getPosition());
        worker.setPosition(position);
        billboard.setPlayer(position, worker);

        winningCondition(startingPosition,player);
    }

    @Override
    public Set<Position> getAvailableCells(Player player) {
        return super.getAvailableCells(player);
    }

    @Override
    public Set<Position> computeAvailablePlacing(Player player) {
        return super.computeAvailablePlacing(player);
    }

    @Override
    public Set<Position> computeAvailableMovements(Player player) {
        return super.computeAvailableMovements(player);
    }

    @Override
    public Set<Position> computeAvailableBuildings(Player player) {
        return super.computeAvailableBuildings(player);
    }

    /**
     * Method which determines if a player wins.
     * <p>
     * If the final position is at level 3 and the starting position is at level 2
     * or the final position is at least two levels lower, you win
     * so you set the player as the winner of the match and you indicates that match is finished.
     * Else you set the turn state to the building phase.
     * <p>
     *
     * @param startingPosition the position where is the worker before the move, not null
     * @param player           the player who makes the move, not null
     */
    @Override
    public void winningCondition(Position startingPosition, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();
        Match match = player.getMatch();
        int startHeight = billboard.getTowerHeight(startingPosition);
        int endHeight = billboard.getTowerHeight(worker.getPosition());

        if (endHeight == startHeight + 1 && endHeight == 3 ||
                endHeight <= startHeight - 2) {
            match.setWinner(player);
            match.setFinished(true);
        }
        else player.setState(TurnState.BUILD);
    }

}
