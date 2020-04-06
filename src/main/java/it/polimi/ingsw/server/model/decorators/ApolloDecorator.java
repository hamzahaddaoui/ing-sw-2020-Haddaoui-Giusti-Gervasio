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

        if(billboard.getPlayer(position)==-1){
            super.moveWorker(position, player);
        }
        else{
            exchangePosition(player,position);
        }
    }

    private void exchangePosition(Player player,Position position){
        Billboard billboard=player.getMatch().getBillboard();
        Worker myWorker= player.getCurrentWorker();
        Player opponentPlayer=findOpponentPlayer(position, player);
        Position actualPosition=myWorker.getPosition();

        Worker opponentWorker= opponentPlayer
                .getWorkers()
                .stream()
                .filter(worker1 -> worker1.getPosition()==position)
                .findAny()
                .get();

        billboard.resetPlayer(position);
        myWorker.setPosition(position);
        billboard.setPlayer(position, player);

        billboard.resetPlayer(actualPosition);
        opponentWorker.setPosition(actualPosition);
        billboard.setPlayer(actualPosition, opponentPlayer);
    }

    private Player findOpponentPlayer (Position position, Player player) {
        Billboard billboard = player.getMatch().getBillboard();

        return player
                .getMatch()
                .getPlayers()
                .stream()
                .filter(player1 -> player1.getID()==billboard.getPlayer(position))
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
        Billboard billboard=player.getMatch().getBillboard();
        Position currentPosition=player.getCurrentWorker().getPosition();

        return worker
                .getPosition()
                .neighbourPositions()
                .stream()
                .filter(position -> billboard.getPlayer(position)!=player.getID())
                .filter(position -> {
                    if (billboard.getTowerHeight(position) <= billboard.getTowerHeight(currentPosition))
                        return true;
                    if (player.getMatch().isMoveUpActive()) {
                        return billboard.getTowerHeight(position) == (billboard.getTowerHeight(currentPosition) + 1);
                    }
                    return false;
                })
                .filter(position -> !billboard.getDome(position))
                .collect(Collectors.toSet());
    }


}
