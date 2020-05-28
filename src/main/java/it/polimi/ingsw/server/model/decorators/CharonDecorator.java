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

    Map<Position, Set<Position>> opponentPositions = new HashMap<>();

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
                opponentPositions = new HashMap<>();
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
        if (!player.hasSpecialFunction())
            super.moveWorker(position, player);
        else{
            Billboard billboard = player.getMatch().getBillboard();

            Player opponent = player.getMatch().getPlayers().stream().filter(p -> p.getID() == billboard.getCells().get(position).getPlayerID()).findFirst().get();

            Worker worker = opponent.getWorkers().stream().filter(w -> w.getPosition().equals(position)).findFirst().get();

            Position finalPos = oppositePos(player.getCurrentWorker().getPosition(), worker.getPosition());

            position.setZ(billboard.getTowerHeight(finalPos));

            billboard.resetPlayer(worker.getPosition());
            worker.setPosition(finalPos);
            billboard.setPlayer(finalPos, opponent.getID());
        }
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
        Map<Position, Boolean> returnMap = new HashMap<>();
        Billboard billboard = player.getMatch().getBillboard();

        player.getWorkers()
                .stream()
                .map(Worker::getPosition)
                .forEach(position -> opponentPositions.put(position, position.neighbourPositions().stream()
                                .filter(pos -> billboard.getPlayer(pos) != 0)
                                .filter(pos -> billboard.getCells().containsKey(oppositePos(position, pos))
                                               && billboard.getPlayer(oppositePos(position, pos)) == 0
                                               && !billboard.getDome(oppositePos(position, pos)))
                                .filter(pos -> player.getWorkers().stream().map(Worker::getPosition).noneMatch(p -> p.equals(pos)))
                                .collect(Collectors.toSet())));

        player.getWorkers().stream().map(Worker::getPosition).forEach(position -> returnMap.put(position, !opponentPositions.get(position).isEmpty()));

        return returnMap;
    }


    private Position oppositePos(Position center, Position position){
        return new Position(2 * center.getX() - position.getX(), 2 * center.getY() - position.getY());
    }

}
