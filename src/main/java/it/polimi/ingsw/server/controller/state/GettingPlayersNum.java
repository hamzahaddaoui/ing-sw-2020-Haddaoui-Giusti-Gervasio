package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;

import static it.polimi.ingsw.server.Server.getClientHandler;
import static it.polimi.ingsw.server.model.GameModel.*;

public class GettingPlayersNum extends State{
    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        ClientHandler clientHandler = messageEvent.getClientHandler();
        int matchID = clientHandler.getMatchID();
        int playerID = clientHandler.getPlayerID();

        int playersNum = messageEvent.getPlayersNum();
        if (!(playersNum == 2 || playersNum == 3)){
            notify(List.of(messageEvent.getClientHandler()), basicErrorConfig((basicPlayerConfig(basicMatchConfig(new MessageEvent(), matchID), playerID))));
            return false;
        }

        setMatchPlayersNum(matchID, playersNum);
        nextMatchState(matchID);

        while ((getPlayersWaitingListSize() != 0) && !isNumReached(matchID)) {
            int opponentID = unstackPlayer();
            addPlayerToMatch(matchID, opponentID);
            getClientHandler(opponentID).setMatchID(matchID);
        }

        checkMatchStart(matchID);
        return true;
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
    }
}
