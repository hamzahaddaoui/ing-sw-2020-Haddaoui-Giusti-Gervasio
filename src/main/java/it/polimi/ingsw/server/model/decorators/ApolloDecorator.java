package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Cell;
import it.polimi.ingsw.utilities.Position;

import java.util.Set;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.MOVE;

/**
 * @author giusti-leo
 *
 * Apollo Commands Decorator
 * Description: "Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated"
 * Differente methods from Basic Commands: moveWorker, computeAvailableMovements
 */

public class ApolloDecorator extends CommandsDecorator {

    private GodCards card = GodCards.Apollo;

    /**
     * decorate the object Command with Apollo's special power
     *
     * @param commands represent the player behaviour
     */
    public ApolloDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * worker may move into ah opponent Worker's space by forcing their worker to the space yours just vacated
     * if position is free -> BasicCommands's method
     * if position is occupied by an enemy -> exchangePosition
     *
     *  @param position  is the position that player have inserted
     *  @param player  is the current player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();

        if(billboard.getCells().get(position).getPlayerID() != null && billboard.getPlayer(position) != player.getID() ){
            exchangePosition(player,position);
        }
        else{
            super.moveWorker(position, player);
        }
    }

    /**
     * method that allow the change of the positions of the workers
     *
     * @param player  is the current player
     * @param position  is the cell where your worker will go
     */
    private void exchangePosition(Player player,Position position){

        Worker myWorker = player.getCurrentWorker();
        Player opponentPlayer = findOpponentPlayer(position, player);
        Worker opponentWorker = findOpponentWorker(position, opponentPlayer);
        Position actualPosition = myWorker.getPosition();

        realizationMove(player,position,actualPosition,myWorker);

        realizationMove(opponentPlayer,actualPosition,position,opponentWorker);
    }

    /**
     * Method that change the position of the workers, and compute the new available cells
     *
     * @param player  the current player
     * @param nextPosition  the future position
     * @param actualPosition  starting position
     * @param worker  the worker used
     */
    private void realizationMove(Player player, Position nextPosition, Position actualPosition, Worker worker){
        Billboard billboard = player.getMatch().getBillboard();

        billboard.resetPlayer(nextPosition);
        worker.setPosition(nextPosition);
        player.getWorkersAvailableCells().remove(actualPosition);
        billboard.setPlayer(nextPosition, player.getID());
        player.setAvailableCells();
    }

    /**
     * find the opponentPlayer and return it
     *
     * @param position  that is selected
     * @param player  current player that move his worker
     * @return  the Player that occupy the cell "position"
     */
    private Player findOpponentPlayer (Position position, Player player) {

        Billboard billboard = player.getMatch().getBillboard();

        return player
                .getMatch()
                .getPlayers()
                .stream()
                .filter(player1 -> player1.getID() == billboard.getPlayer(position) )
                .findAny()
                .get();
    }

    /**
     * find the opponentWorker that is in the that position
     *
     * @param position  that is selected
     * @param player  current player that move his worker
     * @return  the Worker that occupy the cell "position"
     */
    private Worker findOpponentWorker (Position position, Player player) {
        Position pos= position;

        return player
                .getWorkers()
                .stream()
                .filter(worker1 -> worker1.getPosition().getX() == pos.getX() &&  worker1.getPosition().getY() == pos.getY())
                .findAny()
                .get();
    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {

        Billboard billboard = player.getMatch().getBillboard();
        Position currentPosition = worker.getPosition();

        return worker
                .getPosition()
                .neighbourPositions()
                .stream()
                .filter(position -> billboard.getPlayer(position) != billboard.getPlayer(currentPosition) ||
                        billboard.getPlayer(position) == null)
                .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition) ||
                        (player.getMatch().isMoveUpActive() &&
                                billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1))
                .filter(position -> !billboard.getDome(position))
                .collect(Collectors.toSet());
    }

}
