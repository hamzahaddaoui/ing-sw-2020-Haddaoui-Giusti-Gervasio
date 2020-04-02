package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApolloDecorator extends CommandsDecorator {
    private GodCards card = GodCards.Apollo;

    private int movesBeforeBuild = 1;
    private int numOfBuilds = 1;
    private int movesAfterBuild = 0;
    private boolean doneStandard = false;
    private boolean positionedWorkers = false;

    /**
     * decorate the object Command with Apollo's special power
     *
     * @param commands represent the player behaviour
     */
    public ApolloDecorator(Commands commands){
        this.commands=commands;
    }

    public GodCards getCard () {
        return card;
    }

    /**
     * worker may move into ah opponent Worker's space by forcing their worker to the space yours just vacated
     *
     *  @param position  is the position that player have inserted
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker = player.getCurrentWorker();
        Set<Position> availableMovements = computeAvailableMovements(player);

        if (!availableMovements.contains(position))
            return;

        else if(billboard.getPlayer(position)==null){
            position.setZ(billboard.getTowerHeight(position));
            billboard.resetPlayer(worker.getPosition());
            worker.setPosition(position);
            billboard.setPlayer(position, worker);
        }
        else{
            exchangePosition(player,position);
        }

        player.setState(TurnState.BUILD);
    }

    public void exchangePosition(Player player,Position position){
        Billboard billboard=player.getMatch().getBillboard();
        Worker myWorker= player.getCurrentWorker();
        Match activeMatch=player.getMatch();
        Worker opponentWorker= findOpponentWorker(position,player);

        Position provisionalPosition=myWorker.getPosition();
        opponentWorker.setPosition(provisionalPosition);
        billboard.setPlayer(provisionalPosition,opponentWorker);
        myWorker.setPosition(myWorker.getPosition());
        billboard.setPlayer(position,myWorker);
    }

    private Worker findOpponentOldWorker (Position position, Player player) {
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

    private Worker findOpponentWorker (Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();

        Worker worker= player.getMatch().getPlayers()
                .stream()
                .map(player1 -> player1
                        .getWorkers()
                        .stream()
                        .filter(worker1 -> worker1.getPosition()==position)
                        .filter(worker1 -> worker1.getColor()==billboard.getPlayer(position))
                        .findAny().get())
                .findAny().get();
        return worker;
    }

    /**
     * method that divide the different implementation of available cells: for building action and for movement action
     *
     * @param player  is the current player
     * @return  the list of Position that are available for that specific action
     */
    //@Override
    public Set<Position> getAvailableCells(Player player) {
        try{
            switch (player.getState()){
                case PLACING:
                    return computeAvailablePlacing(player);
                case MOVE:
                    return computeAvailableMovements(player);
                case BUILD:
                    return computeAvailableBuildings(player);
                default:
                    return null;
            }
        } catch(NullPointerException ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
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
            throw new NullPointerException("PLAYER IS NULL");
        }

    }

    public boolean hasWon() {
        return false;
    }


    public boolean hasLost() {
        return false;
    }


    public boolean hasMoved() {
        return false;
    }


    public boolean hasBuilt() {
        return false;
    }


    public boolean hasDoneStandard() {
        return false;
    }
}
