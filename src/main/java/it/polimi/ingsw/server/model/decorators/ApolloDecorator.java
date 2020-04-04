package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ApolloDecorator extends CommandsDecorator {
    private GodCards card = GodCards.Apollo;
    Set<Position> availableMovements = new HashSet<>();

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
        Set<Position> availableMovements = computeAvailableMovements(player, worker);

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
    }

    public void exchangePosition(Player player,Position position){
        Billboard billboard=player.getMatch().getBillboard();
        Worker myWorker= player.getCurrentWorker();
        Match activematch=player.getMatch();
        Worker opponentWorker= findOpponentWorker(position,player);
        Position provisionalPosition=myWorker.getPosition();
        opponentWorker.setPosition(provisionalPosition);
        billboard.setPlayer(provisionalPosition,opponentWorker);
        myWorker.setPosition(myWorker.getPosition());
        billboard.setPlayer(position,myWorker);
    }

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

    private Worker findOpponentNewWorker (Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();

        return player.getMatch().getPlayers()
                .stream()
                .map(player1 -> player1
                        .getWorkers()
                        .stream()
                        .filter(worker1 -> worker1.getPosition()==position)
                        .filter(worker1 -> worker1.getColor()==billboard.getPlayer(position))
                        .findAny().get())
                .findAny().get();

    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    @Override
    public Set<Position> computeAvailableMovements(Player player, Worker worker) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=player.getCurrentWorker().getPosition();
            availableMovements = player
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


}
