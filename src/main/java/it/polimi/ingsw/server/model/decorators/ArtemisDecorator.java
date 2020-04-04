package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArtemisDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Artemis;

    private int movesBeforeBuild = 2;
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
        Set<Position> availableMovements;
        if(movesBeforeBuild==2){
            this.startingPosition=worker.getPosition();
            availableMovements = computeAvailableMovements(player, worker);}
        else if(movesBeforeBuild==1)
            availableMovements = computeAvailableSecondMovements(player);
        else return;
        if(availableMovements.contains(position)){
            position.setZ(billboard.getTowerHeight(position));
            billboard.resetPlayer(worker.getPosition());
            worker.setPosition(position);
            billboard.setPlayer(position, worker);
            movesBeforeBuild--;
        }

        if(movesBeforeBuild==0){
            this.startingPosition=null;
            player.setState(TurnState.BUILD);}
    }


    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> computeAvailableMovement(Player player) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=player.getCurrentWorker().getPosition();

            Set<Position> availableMovements = player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) !=player.getCurrentWorker().getColor())
                    .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition))
                    .filter(position ->
                            player.getMatch().isMoveUpActive()
                                    && billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
            return availableMovements;
        }
        catch(Exception ex){
            throw new NullPointerException();
        }

    }

    public Set<Position> computeAvailableSecondMovements(Player player) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=player.getCurrentWorker().getPosition();

            Set<Position> availableMovements = player
                    .getCurrentWorker()
                    .getPosition()
                    .neighbourPositions()
                    .stream()
                    .filter(position -> billboard.getPlayer(position) !=player.getCurrentWorker().getColor())
                    .filter(position -> position != getStartingPosition())
                    .filter(position -> billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition))
                    .filter(position ->
                            player.getMatch().isMoveUpActive()
                                    && billboard.getTowerHeight(position) == billboard.getTowerHeight(currentPosition)+1)
                    .filter(position -> billboard.getDome(position) == false)
                    .collect(Collectors.toSet());
            return availableMovements;
        }
        catch(Exception ex){
            throw new NullPointerException();
        }

    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = null;
    }
}
