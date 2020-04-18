package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import static it.polimi.ingsw.server.model.GameModel.*;

public class GettingPlayersNum extends State{
    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
        int playersNum = messageEvent.getPlayersNum();
        setMatchPlayersNum(messageEvent.getMatchID(), playersNum);
        unstackToMatch(matchID);
        checkMatchStart(matchID);
    }

    private void unstackToMatch(Integer matchID){
        while ((getPlayersWaitingListSize() != 0) && !isNumReached(matchID)) {
            int opponentID = unstackPlayer();
            addPlayerToMatch(matchID, opponentID);
            clientHandlerUpdate(opponentID, matchID);
        }
        changeView(matchID);
    }
}
