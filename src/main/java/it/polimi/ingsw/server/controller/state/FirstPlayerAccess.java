package it.polimi.ingsw.server.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observer;

import java.util.List;

import static it.polimi.ingsw.server.Server.addClientSocket;
import static it.polimi.ingsw.server.Server.removeClientSocket;
import static it.polimi.ingsw.server.model.GameModel.*;

public class FirstPlayerAccess extends State {


    @Override
    public void handleRequest(MessageEvent messageEvent){
        Integer matchID = null;
        MessageEvent message;
        int playersWaiting = getPlayersWaitingListSize();
        int notInitMatches = getNotInitMatchesListSize();
        int initMatches = getInitMatchesListSize();

        Integer playerID = createPlayer(messageEvent.getNickname());
        addClientSocket(playerID, messageEvent.getClientHandler());
        clientHandlerUpdate(matchID, playerID);


        //se il nick non è disponibile, elimino il player e il suo rif al clienthandler
        //dopo aver mandato la notifica di errore
        if (! isNickAvailable(messageEvent.getNickname())) {
            notify(List.of(messageEvent.getClientHandler()), basicErrorConfig(new MessageEvent(), playerID));
            removeInitPlayer(playerID);
            removeClientSocket(playerID);
            return;
        }

        //AGGIUNGO IL PLAYER AL MATCH -> aggiorno tutti
        if (playersWaiting == 0 && initMatches !=0){
            matchID = getInitMatchID();
            addPlayerToMatch(matchID, playerID);
            checkMatchStart(matchID);
            clientHandlerUpdate(matchID, playerID);
        }

        //CREO UN MATCH
        else if (((playersWaiting != 0) && (playersWaiting >= (2 * notInitMatches))) || ((playersWaiting == 0) && (notInitMatches == 0))) {
            matchID = createMatch(playerID);
            clientHandlerUpdate(matchID, playerID);
        }

        //AGGIUNGO IL PLAYER ALLA WAITING LIST
        else{
            message = new MessageEvent();
            message.setPlayerID(playerID);
            notify(List.of(messageEvent.getClientHandler()), message);
            addPlayerToWaitingList(playerID);
            clientHandlerUpdate(matchID, playerID);
        }
    }

    @Override
    public void viewNotify(List<Observer<MessageEvent>> observers, Integer matchID){
    }

    @Override
    public void exit(Integer matchID){

    }
}
