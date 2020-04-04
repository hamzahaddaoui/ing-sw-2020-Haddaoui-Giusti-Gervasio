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

        if(billboard.getPlayer(position)==null){
            position.setZ(billboard.getTowerHeight(position));// imposta l'altezza al quale corrisponde il movimento

            billboard.resetPlayer(worker.getPosition());//nella Billboard imposta il color nella posizione
            worker.setPosition(position);//imposta la nuova posizione nel nuovo player
            billboard.setPlayer(position, worker);//imposta il colore del player
        }
        else{
            exchangePosition(player,position);
        }
    }

    public void exchangePosition(Player player,Position position){
        Billboard billboard=player.getMatch().getBillboard();
        Worker myWorker= player.getCurrentWorker();
        Position exchangePosition= myWorker.getPosition();
        Worker opponentWorker= findOpponentWorker(position,player);

        opponentWorker.getPosition().setZ(billboard.getTowerHeight(opponentWorker.getPosition()));
        position.setZ(billboard.getTowerHeight(position));

        billboard.resetPlayer(opponentWorker.getPosition());
        myWorker.setPosition(position);
        billboard.setPlayer(position, myWorker);
        billboard.resetPlayer(exchangePosition);
        opponentWorker.setPosition(exchangePosition);
        billboard.setPlayer(position, opponentWorker);
    }

    private Worker findOpponentWorker (Position position, Player player) {
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
            Position currentPosition=worker.getPosition();
            return worker
                    .getPosition()
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
            throw new NullPointerException("PLAYER IS NULL");
        }

    }


}
