package it.polimi.ingsw;

public class Player {
    private String nickname;
    private Match matchID;

    public Player(String nickname) {
        this.nickname = nickname;
        matchID = Match.MatchID(this, 2);
    }


}
