package it.polimi.ingsw.utilities;

public class VCEvent {
    private String userID;
    private Position position;

    public VCEvent(String userID, Position position) {
        this.userID = userID;
        this.position = position;
    }

    public String getUserID() {
        return userID;
    }

    public Position getPosition(){
        return position;
    }
}
