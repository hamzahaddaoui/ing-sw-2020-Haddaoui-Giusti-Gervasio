package it.polimi.ingsw.model;

import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class BasicCommands implements Commands {

    /**
     * method that allows the stardard placing movement
     *
     * @param position  the position that player have inserted, not null
     * @param player  that does the placing action
     */
    @Override
    public void placeWorker(Position position, Player player) {
        Billboard billboard=player.getMatch().getBillboard();
        if(billboard.getPlayerColor(position)==null){
            player.getCurrentWorker().setPosition(position);
            player.getMatch().getBillboard().setPlayerColor(position, player.getCurrentWorker());
        }
        else{
            throw new IllegalArgumentException("The space is full. Chose another space. /n");
        }
    }

    /**
     * method that allows the stardard player movement
     * the player can move the selected Worker into one of the (up to) 8 neighboring spaces of the Billboard
     * if the position that is selected is free
     *
     * @param position  the position that player have inserted, not null
     * @param player  that does the movement action
     */
    @Override
    public void moveWorker(Position position, Player player) {

    }

    /**
     * method that allows the standard building block action
     * the player can build a block on an unoccupied space neighbouring the worker
     *
     * @param player  that does the building action
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Position position, Player player) {

    }


    /**
     * method that allows the standard building dome action
     * the player can build a dome on an unoccupied space neighbouring the worker
     *
     * @param player  that does the building action, not null
     * @param position   the position that player have inserted, not null
     */

    public void buildDome(Position position, Player player) {

    }

    /**
     * method that control the cells that are available according to standard characteristic from the list of
     * neighboring cells
     *
     * @param player  the current worker, not null
     * @return  the list of spaces that are available after a check on billboard
     */
    @Override
    public List<Position> getAvailableCells(Player player) {
        List<Position> neighboringCells=null;//=metodo che restituisce una lista di posizioni vicine ad una data posizione
        Billboard billboard = player.getMatch().getBillboard();
        Worker worker=player.getCurrentWorker();
        int i=0;
        while( i < neighboringCells.size()){
            if(     !(billboard.getPlayerColor(neighboringCells.get(i))==null
                    && ((billboard.getTowerHeight(neighboringCells.get(i))==billboard.getTowerHeight(worker.getPosition())+1)
                    || (billboard.getTowerHeight(neighboringCells.get(i))<=billboard.getTowerHeight(worker.getPosition())))
                    && (billboard.getDome(neighboringCells.get(i))==false))) {
                neighboringCells.remove(i);
                i--;}
            i++;
        }
        return neighboringCells;
    }

    /**
     * method that return a list of neighboring cells for the method getAvailableCells
     *
     * @param position  the current position
     * @return  the list of neighboring cells
     */
    public List<Position> getNeighboringCells(Position position){
        List<Position> neighboringCells = null;
        //int xPos=position.getX(), yPos=position.getY(), less=0, max=5;
        //if()

        return neighboringCells;
    }
}
