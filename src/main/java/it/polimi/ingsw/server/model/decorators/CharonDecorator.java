package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static it.polimi.ingsw.utilities.TurnState.BUILD;
import static it.polimi.ingsw.utilities.TurnState.MOVE;

public class CharonDecorator  extends CommandsDecorator {

    Map<Position, Set<Position>> opponentPositions;

    public CharonDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * Method that sets the next state of the player.
     * <p>
     * If the player sets the special function, from WAIT the turn shifts to BUILD
     * then if the player has done just his first building move, the turn shifts to MOVE
     * otherwise, the player has finished his turn, sets the boolean and the turn shifts to WAIT.
     * Else, the turn follows his standard shifting.
     *
     * @param player  the player who makes the turn, not null
     */
    @Override
    public void nextState(Player player) {
        switch(player.getTurnState()){
            case IDLE:
                opponentPositions = null;
                player.setUnsetSpecialFunctionAvailable(canMoveOpponent(player));
                player.setTurnState(MOVE);
                break;
            case MOVE:
                player.setUnsetSpecialFunctionAvailable(null);
                if (player.hasSpecialFunction()){
                    player.setTurnState(MOVE);
                    player.setUnsetSpecialFunction(false);
                }
                else {
                    player.setTurnState(BUILD);
                }
                break;
            case BUILD:
                player.setHasFinished();
                break;
        }
    }

    @Override
    public void moveWorker(Position position, Player player){
        super.moveWorker(position, player);
    }

    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        if (!player.hasSpecialFunction())
            return super.computeAvailableMovements(player, worker);
        else {
            return opponentPositions.get(worker.getPosition());
        }
    }

    @Override
    public void notifySpecialFunction(Player player){
        player.setAvailableCells();
    }

    private Map<Position, Boolean> canMoveOpponent(Player player){
        // trovo i giocatori circostanti per entrambi i worker
        // verifico che questi giocatori si possano spostare al lato opposto del mio worker selezionato
        //inserisco le posizioni dei worker nella mappa opponentPositions
        Map<Position, Boolean> returnMap = new HashMap<>();
        Billboard billboard = player.getMatch().getBillboard();


        //Map<Positions


        player.getWorkers()
                .stream()
                .map(Worker::getPosition)
                .forEach(position -> opponentPositions.put(position, position.neighbourPositions().stream()
                                .filter(pos -> billboard.getPlayer(pos) != 0)
                                .filter(pos -> billboard.getCells().containsKey(oppositePos(position, pos)) && billboard.getPlayer(oppositePos(position, pos)) == 0 )
                                .collect(Collectors.toSet())));


        player.getWorkers().stream().map(Worker::getPosition).forEach(position -> returnMap.put(position, !opponentPositions.get(position).isEmpty()));

        return returnMap;
    }


    private Position oppositePos(Position center, Position position){
        return new Position(2 * center.getX() - position.getX(), 2 * center.getY() - position.getY());
    }

}
