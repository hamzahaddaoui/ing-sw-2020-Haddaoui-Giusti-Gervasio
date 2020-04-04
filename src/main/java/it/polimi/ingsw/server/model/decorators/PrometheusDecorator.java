package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.server.model.TurnState.*;

public class PrometheusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Prometheus;
    public PrometheusDecorator(Commands commands){
        this.commands=commands;
    }
    private int numOfBuildings;
    private boolean buildBefore;




    @Override
    public TurnState nextState(Player player) {
        switch(player.getState()){
            case WAIT:
                return MOVE;
            case MOVE:
                return BUILD;
            default:
                return WAIT;
        }
    }


    /**
     * Method that implements the specific movement of Prometheus.
     * <p>
     * If the flag buildBefore is true and you haven't done the first building, you have to build before move.
     * Else if the flag is true and you have already done the first building, you can move but not move up.
     * Else the flag is false and you do the standard move.
     *
     * @param position  the position that player have inserted, not null
     * @param player    the player who make the move, not null
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();

        if (buildBefore && numOfBuildings==2) {
                player.setState(BUILD); //valutare se ha senso farlo
                build(position, player);
        }
        else if (buildBefore && numOfBuildings==1) {
            if (!computeAvailableMovements(player, worker).contains(position))
                return;

            position.setZ(billboard.getTowerHeight(position));
            billboard.resetPlayer(worker.getPosition());
            worker.setPosition(position);
            billboard.setPlayer(position, worker);

            player.setState(BUILD);
        }
        else super.moveWorker(position, player);
    }

    /**
     * Method that allows the specific building block action of Prometheus.
     * <p>
     * If it's your first building move, you do the standard building move and increment your counter.
     * Else you do just your standard building move.
     *
     * @param player     the player who makes the building move, not null
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Position position, Player player) {
        if (buildBefore && numOfBuildings==2) {
            super.build(position,player);
            numOfBuildings--;
            player.setState(TurnState.MOVE);
        }
        else super.build(position, player);
    }


    /**
     * Returns the spaces that are available after a check in the billboard.
     * <p>
     * Check if the space is free, if has height less than or equal to the current space
     * and if there isn't a dome in it.
     *
     * @param player  the player who makes the move, not null
     * @return        the spaces which are available
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=player.getCurrentWorker().getPosition();

            return player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == null)
                    .filter(position -> (billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition)))
                    .filter(position -> !billboard.getDome(position))
                    .collect(Collectors.toSet());
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

}
