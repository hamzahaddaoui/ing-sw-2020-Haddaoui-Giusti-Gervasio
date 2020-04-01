package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

import java.util.List;

public class MinotaurDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Minotaur;
    public MinotaurDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * Method that allows the specific player movement of Minotaur.
     * <p>
     * He can move in an opponent's worker space, only if the next box in the same direction is free.
     * Then, the opponent's worker is forced to move there.
     * <p>
     * {@link #getAvailableCells(Billboard)}
     *  @param position    the position that player have inserted, not null
     * @param player
     */
    @Override
    public void moveWorker(Position position, Player player) {
        super.moveWorker(position, player);
    }

    /**
     * Return the spaces that are available after a check on billboard.
     * <p>
     * This is used both in the phase of moving and building.
     * <p>
     *  //metodi della Billboard da definire
     *  {@link #checkNextPosition(Position, Position, Billboard)}
     *
     *
     * @param player@return List<Position>  the spaces that are available
     */
    @Override
    public List<Position> getAvailableCells(Player player) {
        // switch(PlayerState):
        // case MOVE:
        // check che sulla posizione del worker, se non è sul bordo -> checkNextPosition(opponentPosition,worker.getPosition(),billboard)
        // se torna true allora considero anche quella posizione, altrimenti no
        // return

        return null;
    }

    /**
     * Check the next position of the opponent's worker.
     * <p>
     * {@link Billboard#getDome(Position)}
     * {@link Billboard#getPlayerPosition(Position)}
     * {@link Position#getX()}
     * {@link Position#getY()}
     * {@link Position#set(int, int)}
     * 
     * @param opponentPosition  the position of your opponent's worker, not null
     * @param myPosition        the position of you current worker, not null
     * @param billboard         the reference to the gameboard, not null
     * @return                  true if is available, otherwise false
     * @throws IllegalArgumentException if the opponentPosition and myPosition are the same
     * @throws IllegalArgumentException if the opponentPosition is a perimeter space
     */
    public boolean checkNextPosition(Position opponentPosition, Position myPosition, Billboard billboard) {
        Position nextPosition = null;

        if (myPosition.getX()==opponentPosition.getX()) {
            if (opponentPosition.getY()>myPosition.getX())
                nextPosition.set(myPosition.getX(),opponentPosition.getY()+1);
            else nextPosition.set(myPosition.getX(),opponentPosition.getY()-1);
        }
        else if (myPosition.getY()==opponentPosition.getY()) {
            if (opponentPosition.getX()>myPosition.getX())
                nextPosition.set(opponentPosition.getX()+1,myPosition.getY());
            else nextPosition.set(opponentPosition.getX()-1,myPosition.getY());
        }
        else if (myPosition.getX()==opponentPosition.getX()+1 && myPosition.getY()==opponentPosition.getY()+1)
            nextPosition.set(opponentPosition.getX()-1,opponentPosition.getY()-1);
        else if (myPosition.getX()==opponentPosition.getX()-1 && myPosition.getY()==opponentPosition.getY()+1)
            nextPosition.set(opponentPosition.getX()+1,opponentPosition.getY()-1);
        else if (myPosition.getX()==opponentPosition.getX()+1 && myPosition.getY()==opponentPosition.getY()-1)
            nextPosition.set(opponentPosition.getX()-1,opponentPosition.getY()+1);
        else if (myPosition.getX()==opponentPosition.getX()-1 && myPosition.getY()==opponentPosition.getY()-1)
            nextPosition.set(opponentPosition.getX()+1,opponentPosition.getY()+1);

        if(billboard.getDome(nextPosition)==false && billboard.getPlayerPosition(nextPosition)==null) return true;
        else return false;
    }
}
