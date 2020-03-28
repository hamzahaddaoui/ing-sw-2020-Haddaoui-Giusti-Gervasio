package it.polimi.ingsw.utilities;

public class Message {
    private Integer matchID;
    private Integer playerID;
    private String payload;

    public Message(int matchID, int playerID, String payload) {
        this.matchID = matchID;
        this.playerID = playerID;
        this.payload = payload;
    }

    public Integer getMatchID() {
        return matchID;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public String getPayload() {
        return payload;
    }
}
