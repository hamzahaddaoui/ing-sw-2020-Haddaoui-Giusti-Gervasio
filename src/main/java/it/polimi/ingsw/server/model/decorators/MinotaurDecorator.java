package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import javax.swing.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MinotaurDecorator extends CommandsDecorator {

    static final GodCards card = GodCards.Minotaur;

    public MinotaurDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * Method that allows the specific player movement of Minotaur.
     * <p>
     * If in the selected position there's no player, it does the basic moveWorker function.
     * Else, we store the next position, set the opponent's worker there and then i reset him from the position I
     * chose so i can set my worker there.
     * <p>
     * {@link #setNextPosition(Position, Position)}
     * {@link #findOpponentWorker(Position, Player)}
     * {@link super#moveWorker(Position, Player)}
     * {@link Player#setState(TurnState)}
     * {@link Player#getMatch()}
     * {@link Player#getCurrentWorker()}
     * {@link Billboard#getPlayer(Position)}
     * {@link Billboard#getTowerHeight(Position)}
     * {@link Billboard#setPlayer(Position, Worker)}
     * {@link Billboard#resetPlayer(Position)}
     * {@link Match#getBillboard()}
     * {@link Worker#setPosition(Position)}
     * {@link Position#setZ(int)}

     *
     * @param position    the position that player have inserted, not null
     * @param player      the player who is making the move, not null
     */

    @Override
    public void moveWorker(Position position, Player player) {

        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();

        if (billboard.getPlayer(position)==-1)
            super.moveWorker(position,player);
        else {
            Position nextPosition = setNextPosition(position, worker.getPosition());

            nextPosition.setZ(billboard.getTowerHeight(nextPosition));
            billboard.setPlayer(nextPosition,findOpponentWorker(position, player));
            findOpponentWorker(position,player).setPosition(nextPosition);
            billboard.resetPlayer(position);
            position.setZ(billboard.getTowerHeight(position));
            billboard.resetPlayer(worker.getPosition());
            worker.setPosition(position);
            billboard.setPlayer(position, worker);
        }
    }

    /**
     * Method that show the list of cells that are available for this specific movement.
     * <p>
     * Check if the space is free or if there's an opponent player,
     * if there's another player checks if the next space is free and
     * then it does the other standard checks.
     *
     * @param player  the player who makes the move, not null
     * @return        the spaces which are available
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        Billboard billboard = player.getMatch().getBillboard();
        Position currentPosition = worker.getPosition();

        return currentPosition
                .neighbourPositions()
                .stream()
                .filter(position -> billboard.getPlayer(position)==null ||
                        (billboard.getPlayer(position) != billboard.getPlayer(currentPosition) &&
                        checkNextPosition(position,player)))
                .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition) ||
                        (player.getMatch().isMoveUpActive() &&
                                billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1))
                .filter(position -> !billboard.getDome(position))
                .collect(Collectors.toSet());
    }


    /**
     * Check the next position of the opponent's worker.
     * <p>
     * Check if the next position is not null and there's no dome or player in it.
     *
     * {@link Billboard#getDome(Position)}
     * {@link Billboard#getPlayer(Position)}
     * {@link Position#getX()}
     * {@link Position#getY()}
     * {@link Position#set(int, int)}
     * 
     * @param opponentPosition  the position of the player opponent's worker, not null
     * @param player            the player who makes the move, not null
     * @return                  true if is available, otherwise false
     */
    private boolean checkNextPosition(Position opponentPosition, Player player) {

            Billboard billboard = player.getMatch().getBillboard();
            Position myPosition = player.getCurrentWorker().getPosition();

            Position nextPosition = setNextPosition(opponentPosition, myPosition);

            if (nextPosition!=null &&
                    !billboard.getDome(nextPosition) &&
                    billboard.getPlayer(nextPosition) == null)
                return true;
            else return false;

    }

    /**
     * Method that returns the next position.
     * <p>
     * It compares the player's position and his opponent's position and finds the next position in that direction.
     *
     * @param opponentPosition the position of the opponent's worker, not null
     * @param myPosition       the position of the worker who makes the move, not null
     * @return                 the next position if exists, or null
     * @throws IllegalArgumentException if the positions you want to compare are the same
     */
    private Position setNextPosition(Position opponentPosition, Position myPosition) throws IllegalArgumentException {

            if (opponentPosition==myPosition) throw new IllegalArgumentException("Same position!");

            for (Position position : opponentPosition.neighbourPositions()) {
                if (opponentPosition.checkMutualPosition(position) == myPosition.checkMutualPosition(opponentPosition))
                    return position;
            }
            return null;
    }

    /**
     * Method which returns the worker which is in the specific space where the player wants to go.
     * <p>
     * First the method finds out which is the player who has a worker in that space,
     * then finds out which is the specific worker.
     *
     * @param position  the position of the space, not null
     * @param player    the player who makes the move, not null
     * @return          the worker which is in the specific space, not null
     */
    private Worker findOpponentWorker (Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();

        return player
                .getMatch()
                .getPlayers()
                .stream()
                .filter(player1 -> player.getCurrentWorker().getColor() == billboard.getPlayer(position))
                .map(player1 -> player1
                                .getWorkers()
                                .stream()
                                .filter(worker1 -> worker1.getPosition() == position)
                        .findAny().get())
                .findAny().get();
    }


}