package it.polimi.ingsw.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Position {

    private int x, y;

    Position (int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }


    /**
     * Computes the set of neighbouring cells of a certain point on a matrix
     * The matrix is not fixed, but created at runtime based on the caller defined constraints
     * @param upperLeftConstraint point on the upper left side of the matrix
     * @param lowerRightConstraint point on the lower right side of the matrix
     * @return the set of computed positions
     */
    public Set<Position> neighbourPositions (Position upperLeftConstraint, Position lowerRightConstraint){
        Set<Position> resultPositions = new HashSet<>();
        Position current = new Position(0,0);
        Position start = new Position(Math.max(this.x - 1,upperLeftConstraint.getX()), Math.max(this.y -1, upperLeftConstraint.getY()));
        Position end = new Position(Math.min(this.x + 1,lowerRightConstraint.getX()), Math.min(this.y -1, lowerRightConstraint.getY()));

        for (current.setX(start.getX()); x <= end.getX(); x++){
            for (current.setY(start.getY()); y <= end.getY(); y++){
                resultPositions.add(current);
            }
        }

        return resultPositions;
    }

    /**
     * Computes the set of neighbouring cells of a certain point on a matrix
     * The matrix is a fixed 5x5 2D array
     *
     * @return the set of computed positions
     */
    public Set<Position> neighbourPositions (){
        return neighbourPositions(new Position(0,0), new Position(4,4));
    }

    /**
     * Checks the mutual position of two neighbouring points.
     * The mutual position returned is a cardinal direction
     *
     * @param position the 2nd neighbour point to be checked
     * @return the cardinal direction of 2nd point related to the 1st point
     */
    public CardinalDirection checkMutualPosition(Position position){
        if (!this.neighbourPositions().contains(position))
            return null;

        Position offset = new Position(position.getX()-this.getX(), position.getY()-this.getY());

        return CardinalDirection.valueOf(Math.toDegrees(Math.atan2(offset.getY(),offset.getX())));
    }
}

