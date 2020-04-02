package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utilities.Position;

import java.util.List;
import java.util.Set;

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


    public List<Position> getAvailableCells(Player player) {
        Position workerPosition=player.getCurrentWorker().getPosition();
        List<Position> neighboringCells=workerPosition.neighbourPositions(workerPosition,workerPosition);//=metodo che restituisce una lista di posizioni vicine ad una data posizione
        Billboard billboard = player.getMatch().getBillboard();
        int i=0;
        while( i < neighboringCells.size()){
            if(     !(billboard.getPlayer(neighboringCells.get(i))==null
                    && ((billboard.getTowerHeight(neighboringCells.get(i))==billboard.getTowerHeight(player.getCurrentWorker().getPosition())+1)
                    || (billboard.getTowerHeight(neighboringCells.get(i))<=billboard.getTowerHeight(player.getCurrentWorker().getPosition())))
                    && (billboard.getDome(neighboringCells.get(i))==false))) {
                neighboringCells.remove(i);
                i--;}
            i++;
        }
        return neighboringCells;
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
     * method that control the cells that are available according to standard characteristic from the list of
     * neighboring cells
     *
     * @param player  the current worker, not null
     * @return  the list of spaces that are available after a check on billboard
     */
    //@Override
    public List<Position> getAvailableMovement(Player player) {
        List<Position> neighboringCells=null;//=metodo che restituisce una lista di posizioni vicine ad una data posizione
        Billboard billboard = player.getMatch().getBillboard();
        int i=0;
        while( i < neighboringCells.size()){
            if(     !(billboard.getPlayer(neighboringCells.get(i))==null
                    && ((billboard.getTowerHeight(neighboringCells.get(i))==billboard.getTowerHeight(player.getCurrentWorker().getPosition())+1)
                    || (billboard.getTowerHeight(neighboringCells.get(i))<=billboard.getTowerHeight(player.getCurrentWorker().getPosition())))
                    && (billboard.getDome(neighboringCells.get(i))==false))) {
                neighboringCells.remove(i);
                i--;}
            i++;
        }
        return neighboringCells;
    }

    @Override
    public void specialFunctionSetUnset(Player player) {
    }

    /**
     * method that return a list of neighboring cells for the method getAvailableCells
     *
     * @param worker  the current worker
     * @return  the list of neighboring cells
     */
    public Set<Position> getNeighboringCells(Worker worker){
        Set<Position> neighboringCells = null;
        return neighboringCells;
    }
}
