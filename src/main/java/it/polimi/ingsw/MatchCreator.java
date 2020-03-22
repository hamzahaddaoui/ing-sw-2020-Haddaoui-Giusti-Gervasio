package it.polimi.ingsw;

public class MatchCreator {
    static Match matchID;

    public static Match create(int n){
        if (matchID == null){
            matchID = new Match();
        }
        return matchID;
    }
}
