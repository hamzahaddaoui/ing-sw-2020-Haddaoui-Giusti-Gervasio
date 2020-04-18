package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

import static it.polimi.ingsw.server.Server.addClientSocket;
import static it.polimi.ingsw.server.Server.removeClientSocket;
import static it.polimi.ingsw.server.model.GameModel.*;
import static it.polimi.ingsw.server.model.GameModel.addPlayerToWaitingList;

public class FirstPlayerAccess extends State {

    @Override
    public void handleRequest(Integer matchID, MessageEvent messageEvent){
        MessageEvent updateMessage;
        int playersWaiting = getPlayersWaitingListSize();
        int notInitMatches = getNotInitMatchesListSize();
        int initMatches = getInitMatchesListSize();

        Integer playerID = createPlayer(messageEvent.getNickname());
        addClientSocket(playerID, messageEvent.getClientHandler());

        //se il nick non è disponibile, elimino il player e il suo rif al clienthandler
        //dopo aver mandato la notifica di errore
        if (! isNickAvailable(messageEvent.getNickname())) {
            inputError(playerID);
            removeInitPlayer(playerID);
            removeClientSocket(playerID);
            return;
        }

        //AGGIUNGO IL PLAYER AL MATCH
        if (playersWaiting == 0 && initMatches !=0){
            matchID = getInitMatchID();
            addPlayerToMatch(matchID, playerID);
            sendToView(
                    new MessageEvent(
                            matchID,
                            playerID,
                            getMatchState(matchID),
                            getPlayerState(matchID, playerID),
                            getPlayerTurn(matchID, playerID),
                            getMatchPlayers(matchID)));
            checkMatchStart(matchID);
        }

        //CREO UN MATCH
        else if (((playersWaiting != 0) && (playersWaiting >= (2 * notInitMatches))) || ((playersWaiting == 0) && (notInitMatches == 0))) {
            matchID = createMatch(playerID);
            sendToView(
                    new MessageEvent(matchID,
                            playerID,
                            getMatchState(matchID),
                            getPlayerState(matchID, playerID),
                            getPlayerTurn(matchID, playerID),
                            getGameCards(),
                            getMatchPlayers(matchID),
                            getActiveMatches(),
                            getPlayersConnected()));
        }

        //AGGIUNGO IL PLAYER ALLA WAITING LIST
        else{
            matchID = null;
            addPlayerToWaitingList(playerID);
            sendToView(new MessageEvent(playerID, getPlayerState(null, playerID)));
        }

        clientHandlerUpdate(playerID, matchID);
    }
}