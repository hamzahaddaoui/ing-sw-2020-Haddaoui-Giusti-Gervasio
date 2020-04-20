package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import static it.polimi.ingsw.server.model.GameModel.*;

public class GettingPlayersNum extends State{
    @Override
    public void handleRequest(MessageEvent messageEvent){
        Integer matchID = messageEvent.getMatchID();
        int playersNum = messageEvent.getPlayersNum();
        int playerID = messageEvent.getPlayerID();
        if (!(playersNum == 2 || playersNum == 3)){
            notify(basicErrorConfig(basicMatchConfig(basicPlayerConfig(new MessageEvent(), playerID),matchID)));
        }

        setMatchPlayersNum(messageEvent.getMatchID(), playersNum);
        nextMatchState(matchID);

        while ((getPlayersWaitingListSize() != 0) && !isNumReached(matchID)) {
            int opponentID = unstackPlayer();
            addPlayerToMatch(matchID, opponentID);
            clientHandlerUpdate(matchID, opponentID);

        }

        checkMatchStart(matchID);
    }
}
