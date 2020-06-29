package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * @author giusti-leo
 *
 * Eros Commands Decorator
 * Description->
 * Set Up: "Place your Workers anywhere along opposite edges of the board"
 * Winning Condition : "You also win if one of your Workers moves to a space neighboring your other worker and both
 * are on the first level (or the same level in a 3-player game)"
 *
 * Differente methods from Basic Commands:
 */

public class ErosDecorator  extends CommandsDecorator {

    static final private GodCards card = GodCards.Eros;

    private boolean winningCondition;  //true if the current movement is build, else false
    private boolean three_playerGame;

    public ErosDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * method that show the list of cells that are available for the placing movements.
     * The available placing cells are along the board
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailablePlacing(Player player) {
        try{
            three_playerGame = player.getMatch().getPlayers().size() == 3;

            Set<Position> positions = new HashSet<>();
            player.getMatch().getBillboard().getCells().forEach((key,val) -> {
                if (val.getPlayerID() == 0 && (key.getX() == 4 || key.getX() == 0 || key.getY() == 0 || key.getY() == 4)){
                    positions.add(key);
                }
            });
            return positions;
        }
        catch(Exception ex){throw new NullPointerException();}
    }

    @Override
    public boolean winningCondition(Player player) {
        Worker worker1 = player.getCurrentWorker();
        Worker worker2 = null;

        if(player.getWorkers().stream().anyMatch(w-> w.getPosition() != worker1.getPosition()))
            worker2 = player.getWorkers().stream().filter(w-> w.getPosition() != worker1.getPosition()).findAny().get();

        if(winningCondition && worker2 != null){
            return super.winningCondition(player) || heightCondition(worker1, worker2);
        }
        else{
            return super.winningCondition(player);
        }
    }

    @Override
    public void build(Position position, Player player) {
        winningCondition = false;
        super.build(position,player);
    }

    @Override
    public void moveWorker(Position position, Player player) {
        super.moveWorker(position, player);

        Worker worker1 = player.getCurrentWorker();
        Worker worker2;

        if(player.getWorkers().stream().anyMatch(w-> w.getPosition() != worker1.getPosition())){
            worker2 = player.getWorkers().stream().filter(w-> w.getPosition() != worker1.getPosition()).findAny().get();
            winningCondition = worker1.getPosition().neighbourPositions().contains(worker2.getPosition());
        }
    }

    private boolean heightCondition(Worker worker1, Worker worker2){
        if(three_playerGame){
            return worker1.getPosition().getZ() == worker2.getPosition().getZ() && worker2.getPosition().getZ() >= 0;
        }
        else{
            return worker1.getPosition().getZ() == 1 && worker2.getPosition().getZ() == 1;
        }
    }


}
