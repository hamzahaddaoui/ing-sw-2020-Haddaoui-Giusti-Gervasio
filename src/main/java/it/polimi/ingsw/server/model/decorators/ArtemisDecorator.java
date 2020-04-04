package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArtemisDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Artemis;

    private Position startingPosition=null;

    /**
     * decorate the object Command with Artemis's special power
     *
     * @param commands  represent the player behaviour
     */
    public ArtemisDecorator(Commands commands){
        this.commands=commands;
    }


    /**
     * worker may move one additional time but not back to the initial space
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();

        if(player.getMovesBeforeBuild()==2){
            startingPosition=worker.getPosition();}

        position.setZ(billboard.getTowerHeight(position));

        billboard.resetPlayer(worker.getPosition());
        worker.setPosition(position);
        billboard.setPlayer(position, worker);

        if(player.getMovesBeforeBuild()==1){
            startingPosition=null;}
    }

    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        try{
            if(player.getMovesBeforeBuild()==2){
                return computeAvailableFirstMovements(player,worker);}
            else if(player.getMovesBeforeBuild()==1){
                return computeAvailableSecondMovements(player,worker);}
            else return null;
        }
        catch(Exception ex){
            throw new NullPointerException();}
    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailableFirstMovements(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=worker.getPosition();

            return currentPosition
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) !=worker.getColor())
                    .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition))
                    .filter(position ->
                            player.getMatch().isMoveUpActive()
                                    && billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
        }
        catch(Exception ex){
            throw new NullPointerException();
        }

    }

    public Set<Position> computeAvailableSecondMovements(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=worker.getPosition();

            return  currentPosition
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) !=worker.getColor())
                    .filter(position -> position != startingPosition)
                    .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition))
                    .filter(position ->
                            player.getMatch().isMoveUpActive()
                                    && billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
        }
        catch(Exception ex){
            throw new NullPointerException();
        }

    }

}
