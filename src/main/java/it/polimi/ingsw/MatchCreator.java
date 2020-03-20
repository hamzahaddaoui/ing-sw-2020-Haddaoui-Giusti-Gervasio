package it.polimi.ingsw;
import java.util.HashSet;


public class MatchCreator {
    static Match matchID;

    public static Match create(Player player, int n){
        if (matchID == null){
            matchID = new Match(n);
        }
        matchID.addPlayer(player);
        return matchID;
    }
}
