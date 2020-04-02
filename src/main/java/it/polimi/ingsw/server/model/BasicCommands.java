package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

public class BasicCommands implements Commands {
    /**
     * method that allows the stardard placing movement
     *
     * @param position  the position that player have inserted, not null
     * @param player  that does the placing action
     */
    @Override
    public void placeWorker(Position position, Player player) {
        try{
            Billboard billboard=player.getMatch().getBillboard();
            if(billboard.getPlayer(position)==null){
                player.getCurrentWorker().setPosition(position);
                player.getMatch().getBillboard().setPlayerColor(position, player.getCurrentWorker());
            }
            else System.out.println("The space is full. Chose another space. /n");
        }
        catch(Exception ex){
            throw new NullPointerException();
        }
    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *  @param position   the position that player have inserted, not null
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {

    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Position position, Player player) {
        Worker worker=player.getCurrentWorker();
        //List<Position> availableBuilding=worker.getPosition().;
        //if position.altezza >=3 costruisci cupola
        // else costruisci blocco


    }

    /**
     * method that show the list of cells that are available for the standard movement of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can move on
     */
    public Set<Position> getAvailableMovement(Player player) {
        try{
            Set<Position> neighboringCells=player.getCurrentWorker().getPosition().neighbourPositions();
            Billboard billboard=player.getMatch().getBillboard();
            Position currentPosition=player.getCurrentWorker().getPosition();
            Set<Position> collect = neighboringCells
                    .stream()
                    .filter(position -> billboard.getPlayer(position)==null)
                    .filter(position -> billboard.getTowerHeight(position)<=billboard.getTowerHeight(currentPosition))
                    .filter(position -> billboard.getTowerHeight(position)==billboard.getTowerHeight(currentPosition)+1)
                    .filter(position -> billboard.getDome(position)==false)
                    .collect(Collectors.toSet());
            return collect;
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */

    public void buildDome(Worker worker, Position position, Billboard billboard) {

    }

    /**
     * method that divide the different implementation of available cells: for building action and for movement action
     *
     * @param player  is the current player
     * @return  the list of Position that are available for that specific action
     */
    //@Override
    public Set<Position> getAvailableCells(Player player) {
        try{TurnState playerState=player.getState();
        if(playerState==TurnState.BUILD){
            return getAvailableBuilding(player);
        }
        else if(playerState==TurnState.MOVE){
            return getAvailableMovement(player);
        }
        else return null;
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }


    /**
     * method that show the list of cells that are available for the standard building action of the player
     *
     * @param player  is the current player
     * @return  the list of Position where the worker can build on
     */
    public Set<Position> getAvailableBuilding(Player player) {
        try{
            Set<Position> neighboringCells=player.getCurrentWorker().getPosition().neighbourPositions();
            Billboard billboard=player.getMatch().getBillboard();
            Set<Position> collect = neighboringCells
                    .stream()
                    .filter(position -> billboard.getTowerHeight(position) <= 3)
                    .filter(position -> billboard.getPlayer(position)==null)
                    .filter(position -> billboard.getDome(position)==false)
                    .collect(Collectors.toSet());
            return collect;
        }
        catch(Exception ex){
            throw new NullPointerException("PLAYER IS NULL");
        }
    }

    @Override
    public void specialFunctionSetUnset(Player player) {
        return;
    }

}
