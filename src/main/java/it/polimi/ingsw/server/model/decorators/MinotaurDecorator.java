package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import javax.swing.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MinotaurDecorator extends CommandsDecorator {

    static final GodCards card = GodCards.Minotaur;

    protected Set<Position> availablePlacing = new HashSet<>();
    protected Set<Position> availableMovements = new HashSet<>();
    protected Set<Position> availableBuildings = new HashSet<>();

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
     *     
     * @param position    the position that player have inserted, not null
     * @param player      the player who is making the move, not null
     */

    @Override
    public void moveWorker(Position position, Player player) {

        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();

        if (!availableMovements.contains(position))
            return;

        if (billboard.getPlayer(position)==null)
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

            player.setState(TurnState.BUILD);
        }
    }

    /**
     * Return the spaces that are available after a check on billboard.
     * <p>
     * This is used both in the phase of moving and building.
     * <p>
     *  //metodi della Billboard da definire
     *  {@link #checkNextPosition(Position, Position, Billboard)}
     *
     * @param player           the player who make the move, not null
     * @return List<Position>  the spaces that are available
     */
    @Override
    public Set<Position> getAvailableCells(Player player) {
        try{
            switch (player.getState()){
                case PLACING:
                    super.computeAvailablePlacing(player);
                    return availablePlacing;
                case MOVE:
                    computeAvailableMovements(player);
                    return availableMovements;
                case BUILD:
                    super.computeAvailableBuildings(player);
                    return availableBuildings;
                default:
                    return null;
            }
        } catch(NullPointerException ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

    //check 1 : casella libera da player oppure occupato da un player avversario la cui casella successiva
    // è libera
    @Override
    public Set<Position> computeAvailableMovements(Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Position currentPosition = player.getCurrentWorker().getPosition();

        availableMovements = currentPosition
                .neighbourPositions()
                .stream()
                .filter(position -> billboard.getPlayer(position)==null ||
                        (billboard.getPlayer(position) != billboard.getPlayer(currentPosition) &&
                        checkNextPosition(position,player)))
                .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition) ||
                        (player.getMatch().isMoveUpActive() &&
                                billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1))
                .filter(position -> billboard.getDome(position) == false)
                .collect(Collectors.toSet());
        return availableMovements;
    }


    /**
     * Check the next position of the opponent's worker.
     * <p>
     * {@link Billboard#getDome(Position)}
     * {@link Billboard#getPlayerColor(Position)}
     * {@link Position#getX()}
     * {@link Position#getY()}
     * {@link Position#set(int, int)}
     * 
     * @param opponentPosition  the position of your opponent's worker, not null
     * @param myPosition        the position of you current worker, not null
     * @param billboard         the reference to the gameboard, not null
     * @return                  true if is available, otherwise false
     * @throws IllegalArgumentException if the opponentPosition and myPosition are the same
     * @throws IllegalArgumentException if the opponentPosition is a perimeter space
     */
    private boolean checkNextPosition(Position opponentPosition, Player player) throws IllegalArgumentException, NullPointerException {

        try {
            Billboard billboard = player.getMatch().getBillboard();
            Position myPosition = player.getCurrentWorker().getPosition();

            Position nextPosition = setNextPosition(opponentPosition, myPosition);

            if (nextPosition!=null &&
                    !billboard.getDome(nextPosition) &&
                    billboard.getPlayer(nextPosition) == null) {return true;}
            else return false;

        } catch (NullPointerException e) {
            throw new NullPointerException("Null player or position!");
        }
    }

    //check per la posizione successiva rispetto a quella del worker avversario
    private Position setNextPosition(Position opponentPosition, Position myPosition) throws IllegalArgumentException,NullPointerException {

        try {
            if (opponentPosition==myPosition) throw new IllegalArgumentException("Same position!");

            for (Position position : opponentPosition.neighbourPositions()) {
                if (opponentPosition.checkMutualPosition(position) == myPosition.checkMutualPosition(opponentPosition))
                    return position;
            }
            return null;
        } catch (NullPointerException e) {
            throw new NullPointerException("Null positions.");
        }
    }

    //data la posizione in cui finisci e il player con cui fai la mossa, ti dice qual è il worker avversario presente, in modo da cambiargli poi posizione
    private Worker findOpponentWorker (Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();

        for (Player opponent : player.getMatch().getPlayers()) {
            if (opponent.getCurrentWorker().getColor() == billboard.getPlayer(position)) {
                for(Worker opponentWorker : opponent.getWorkers())
                    if (opponentWorker.getPosition()==position)
                        return opponentWorker;
            }
        }
        return null;
    }

}