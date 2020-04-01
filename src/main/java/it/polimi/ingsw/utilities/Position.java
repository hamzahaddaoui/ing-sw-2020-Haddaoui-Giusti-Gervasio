package it.polimi.ingsw.utilities;

public class Position {

    private int x, y;
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public Position create(int x, int y){
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * method that give the Position to the right of the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
     public Position toRight(Position position){
        Position newPosition = create(position.getX()+1,position.getY());
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position to the left of the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position toLeft(Position position){
        Position newPosition = create(position.getX()-1, position.getY());
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position next to the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position nextTo(Position position){
        Position newPosition = create( position.getX(),position.getY()+1);
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position behind the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position behind(Position position){
        Position newPosition = create(position.getX(),position.getY()-1);
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position to NordEast of the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position toNordEast(Position position){
        Position newPosition = create(position.getX()+1,position.getY()+1);
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position to NordWest of the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position toNordWest(Position position){
        Position newPosition = create(position.getX()-1,position.getY()+1);
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position to SouthEast of the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position toSouthEast(Position position){
        Position newPosition = create(position.getX()+1,position.getY()-1);
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that give the Position to NordEst of the given position
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public Position toSouthWest(Position position){
        Position newPosition = create(position.getX()-1,position.getY()-1);
        if(check(newPosition)) return newPosition;
        else return null;
    }

    /**
     * method that control if the position is correct
     * a position is correct if position.x and position.y =<4 & >=0
     *
     * @param position  current position
     * @return  true if the position is correct, false if the position is incorrect
     */
    public boolean check(Position position){
        if(position.getX()>=0 && position.getX()<=4 && position.getY()>=0 && position.getY()<=4)
            return true;
        else
            return false;
    }
}

