package it.polimi.ingsw.server.model;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.*;

/**
 * @author hamzahaddaoui, giusti-leo
 * Basic move and build commands for a generic worker, with no special power associated
 */

public class BasicCommands implements Commands {

    @Override
    public void nextState(Player player) {
        switch(player.getTurnState()){
            case IDLE:
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
     * method that allows the stardard placing movement
     *
     * @param position  the position that player have inserted, not null
     * @param player  that does the placing action
     */
    @Override
    public void placeWorker(Position position, Player player) {
        try{
            player.getMatch().getBillboard().setPlayer(position, player.getID());
        }
        catch(NullPointerException ex){
            throw new NullPointerException();
        }
    }


    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *  @param position   the position that player have inserted, not null
     * @param player the match current player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();

        position.setZ(billboard.getTowerHeight(position));

        billboard.resetPlayer(worker.getPosition());
        worker.setPosition(position);
        billboard.setPlayer(position, player.getID());
        System.out.println("POS :" + worker.getPosition() + "\n\n");
    }

    @Override
    public void build(Position position, Player player) {
        player.getMatch().getBillboard().incrementTowerHeight(position);
    }


    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailablePlacing(Player player) {
        try{
            Set<Position> positions = new HashSet<>();
            player.getMatch().getBillboard().getCells().forEach((key,val) -> {
                if (val.getPlayerID() == null){
                    positions.add(key);
                }
            });
            return positions;
        }
        catch(Exception ex){
            throw new NullPointerException();
        }
    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {

            Billboard billboard=player.getMatch().getBillboard();
            //Position currentPosition=player.getCurrentWorker().getPosition();

        return worker
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) == null)
                    .filter(position -> (billboard.getTowerHeight(position) <= billboard.getTowerHeight(worker.getPosition())) ||
                            (player.getMatch().isMoveUpActive() &&
                                    billboard.getTowerHeight(position) == (billboard.getTowerHeight(worker.getPosition()) + 1)))
                    .filter(position -> !billboard.getDome(position))
                    .collect(Collectors.toSet());
    }

    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    public Set<Position> computeAvailableBuildings(Player player, Worker worker) {
        Billboard billboard=player.getMatch().getBillboard();
        return worker
                .getPosition()
                .neighbourPositions()
                .stream()
                .filter(position -> billboard.getPlayer(position) == null)
                .filter(position -> ! billboard.getDome(position))
                .collect(Collectors.toSet());
    }

    @Override
    public void notifySpecialFunction(Player player){

    }

    @Override
    public boolean winningCondition(Player player) {
        Worker worker = player.getCurrentWorker();
        return worker.getHeightVariation() == 1 && worker.getPosition().getZ() == 3;
    }

    @Override
    public boolean losingCondition(Player player){
        return player
                .getWorkers()
                .stream()
                .noneMatch(worker -> worker
                        .canDoSomething(player.getTurnState()));
    }
}
