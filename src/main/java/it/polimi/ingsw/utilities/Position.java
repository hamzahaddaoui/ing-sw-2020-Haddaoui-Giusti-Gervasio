package it.polimi.ingsw.utilities;

import org.jetbrains.annotations.NotNull;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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

    public List<Position> neighbourPositions (){
        List<Position> resultPositions = new ArrayList<>();
        Position current = new Position(0,0);
        Position start = new Position(Math.max(this.x - 1, 0), Math.max(this.y -1, 0));
        Position end = new Position(Math.min(this.x + 1, 4), Math.min(this.y -1, 4));

        for (current.setX(this.x-1); x <= this.x+1; x++){
            for (current.setY(this.y-1); y <= this.y+1; y++){
                resultPositions.add(current);
            }
        }
        return resultPositions;
    }

    public List<Position> neighbourPositions (Position upperLeftConstraint, Position lowerRightConstraint){
        List<Position> resultPositions = new ArrayList<>();
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

    public CardinalDirection checkMutualPosition(Position position){
        if (!this.neighbourPositions().contains(position))
            return CardinalDirection.NONE;

        Position offset = new Position(position.getX()-this.getX(), position.getY()-this.getY());

        return CardinalDirection.valueOf(Math.toDegrees(Math.atan2(offset.getY(),offset.getX())));
    }
}

