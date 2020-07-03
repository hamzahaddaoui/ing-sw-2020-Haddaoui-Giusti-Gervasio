package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.server.Server.addClientSocket;
import static it.polimi.ingsw.server.model.GameModel.*;


/**
 * @author: hamzahaddaoui
 *
 * Controller class that manages the first player access.
 * Checks if the input nickname is available
 */


public class FirstPlayerAccess extends State {


    @Override
    public boolean handleRequest(MessageEvent messageEvent){
        ClientHandler clientHandler = messageEvent.getClientHandler();
        int matchID;
        int playerID;

        synchronized (GameModel.class){
            int playersWaiting = getPlayersWaitingListSize();
            int notInitMatches = getNotInitMatchesListSize();
            int initMatches = getInitMatchesListSize();
            String nickname = messageEvent.getNickname();

            if(nickname == null){
                MessageEvent message = new MessageEvent();
                message.setInfo("Please specify a nickname.");
                notify(List.of(messageEvent.getClientHandler()), basicErrorConfig(message));
            }


            playerID = createPlayer(nickname) ;

            if (playerID == 0){
                MessageEvent message = new MessageEvent();
                message.setError(true);
                message.setInfo("Nickname '"+ nickname +"' not available.");
                notify(List.of(messageEvent.getClientHandler()), basicErrorConfig(message));
                return false;
            }

            addClientSocket(playerID, clientHandler);
            clientHandler.setPlayerID(playerID);


            if (playersWaiting == 0 && initMatches !=0){
                matchID = getInitMatchID();
                addPlayerToMatch(matchID, playerID);
                clientHandler.setMatchID(matchID);
                checkMatchStart(matchID);
                return true;
            }

            //CREO UN MATCH
            else if (((playersWaiting != 0) && (playersWaiting >= (2 * notInitMatches))) || ((playersWaiting == 0) && (notInitMatches == 0))) {
                matchID = createMatch(playerID);
                clientHandler.setMatchID(matchID);

                MessageEvent message = new MessageEvent();
                message.setInfo("Match created!");
                notify(List.of(clientHandler), basicPlayerConfig(basicMatchConfig(message, matchID), playerID));
                return false;
            }

            //AGGIUNGO IL PLAYER ALLA WAITING LIST
            else{
                addPlayerToWaitingList(playerID);

                MessageEvent message = new MessageEvent();
                message.setPlayerID(playerID);
                message.setMatchState(MatchState.NONE);
                message.setInfo("Wait for a match to start...");
                notify(List.of(messageEvent.getClientHandler()), message);
                return false;
            }

        }

    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
    }

}
