package it.polimi.ingsw.utilities;
import java.util.*;

import com.google.gson.Gson;

/**
 * @author hamzahaddaoui
 * Class for managining 2D points on a natural numbers xy plane
 * Every point can have a value associated, which is the height, of the point (z)
 */

public class Position implements Comparable{
    private int x;
    private int y;

    private transient int z;

    public Position (int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setZ(int z){
        this.z = z;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getZ(){
        return this.z;
    }


    /**
     * Computes the set of neighbouring cells of a certain point on a matrix
     * The matrix is not fixed, but created at runtime based on the caller defined constraints
     * @param upperLeftConstraint point on the upper left side of the matrix
     * @param lowerRightConstraint point on the lower right side of the matrix
     * @return the set of computed positions
     */
    public Set<Position> neighbourPositions (Position upperLeftConstraint, Position lowerRightConstraint){
        int x, y;
        Set<Position> resultPositions = new HashSet<>();
        Position start = new Position(Math.max(this.x - 1,upperLeftConstraint.getX()), Math.max(this.y -1, upperLeftConstraint.getY()));
        Position end = new Position(Math.min(this.x + 1,lowerRightConstraint.getX()), Math.min(this.y +1, lowerRightConstraint.getY()));

        for (x = start.getX(); x <= end.getX(); x++){
            for (y = start.getY(); y <= end.getY(); y++){
                Position current = new Position(x,y);
                resultPositions.add(current);
            }
        }
        resultPositions.remove(this);
        return resultPositions;
    }

    /**
     * Computes the set of neighbouring cells of a certain point on a matrix
     * The matrix is a fixed 5x5 2D array
     *
     * @return the set of computed positions
     */
    public Set<Position> neighbourPositions (){
        return this.neighbourPositions(new Position(0,0), new Position(4,4));
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
        Position offset = new Position(position.getX() - this.getX(), position.getY() - this.getY());
        return CardinalDirection.valueOf(Math.toDegrees(Math.atan2(offset.getY(),offset.getX())));
    }

    public Position translateCardinalDirectionToPosition(CardinalDirection cardinalDirection){
        return new Position(this.getX() + cardinalDirection.xOffset, this.getY() + cardinalDirection.yOffset);
    }

    @Override
    public boolean equals(Object position) {
        Position pos;
        if (this == position) return true;
        if (position == null || getClass() != position.getClass()) return false;
        pos = (Position) position;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return (this.x * 17) ^ y; //hash function x*numeroprimo xor y
    }

    @Override
    public String toString(){
        return x+""+y;
    }

    @Override
    public int compareTo(Object o){
        if(o instanceof Position){
            Position p = (Position) o;
            return this.toString().compareTo(p.toString());
        }
        return 0;
    }
}

