package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.util.Objects;
import java.util.Set;

import static it.polimi.ingsw.server.Server.*;
import static it.polimi.ingsw.server.model.GameModel.*;





/*
------------to do------------------------
-> ENUM PUBBLICI, FUORI DAL MODEL!

 */

public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    GameModel model;
    MessageEvent messageTX;

    @Override
    public void update(MessageEvent messageRX){
        if (!(messageRX.getMsgType().equals("CONTROLLER_TO_CONTROLLER"))){
            return;
        }

        Integer playerID = messageRX.getPlayerID();

        if (playerID == null) {
            firstClientAccess(messageRX);
            return;
        }

        Integer matchID = messageRX.getMatchID();
        if ((matchID == null) || (! Objects.equals(getPlayerState(matchID, playerID), "ACTIVE"))){
            inputError(playerID);
            return;
        }
        switch (Objects.requireNonNull(getMatchState(matchID))) {
            case "GETTING_PLAYERS_NUM":
                int playersNum = messageRX.getPlayersNum();
                setMatchPlayersNum(matchID, playersNum);
                unstackToMatch(matchID);
                checkMatchStart(matchID);
                break;
            case "WAITING_FOR_PLAYERS":
                //nothing to do... lol
                break;
            case "SELECTING_GOD_CARDS":
                setMatchCards(matchID, messageRX.getGodCards());
                nextMatchState(matchID);
                nextMatchTurn(matchID);
                changeView(matchID);
                break;
            case "SELECTING_SPECIAL_COMMAND":
                selectPlayerCard(matchID, messageRX.getGodCard());
                nextMatchTurn(matchID);
                if (hasSelectedCard(matchID)) {
                    nextMatchState(matchID);
                }
                changeView(matchID);
                break;
            case "PLACING_WORKERS":
                placeWorker(matchID, messageRX.getEndPosition());
                if (hasPlacedWorkers(matchID)) {
                    nextMatchTurn(matchID);
                    if (hasPlacedWorkers(matchID)) {
                        nextMatchState(matchID);
                    }
                    changeView(matchID);
                }
                break;
            case "RUNNING":
                if (messageRX.getEndTurn() != null) {
                    setHasFinished(matchID);
                }

                else if (messageRX.getSpecialFunction() != null) {
                    setUnsetSpecialFunction(matchID, messageRX.getSpecialFunction());
                }

                else{
                    playerTurn(matchID, messageRX.getStartPosition(),messageRX.getEndPosition());
                }

                switch(getCurrentPlayerState(matchID)){
                    case "ACTIVE":
                        changeView(matchID,playerID);
                    case "IDLE":
                        nextMatchTurn(matchID);
                        changeView(matchID);
                        break;
                    case "WIN":
                        break;
                    case "LOSE":
                        break;

                }
                break;
            case "FINISHED":
                break;
        }
        model.modelUpdateView(matchID, playerID);
    }


    void firstClientAccess(MessageEvent messageRX){
        Integer matchID, playerID;
        int playersWaiting = getPlayersWaitingListSize();
        int notInitMatches = getNotInitMatchesListSize();
        int initMatches = getInitMatchesListSize();

        playerID = createPlayer(messageRX.getNickname());
        addClientSocket(playerID, messageRX.getClientHandler());

        //se il nick non è disponibile, elimino il player e il suo rif al clienthandler
        //dopo aver mandato la notifica di errore
        if (! isNickAvailable(messageRX.getNickname())) {
            inputError(playerID);
            removeInitPlayer(playerID);
            removeClientSocket(playerID);
            return;
        }

        //AGGIUNGO IL PLAYER AL MATCH
        if (playersWaiting == 0 && initMatches !=0){
            matchID = getInitMatchID();
            addPlayerToMatch(matchID, playerID);
        }

        //CREO UN MATCH
        else if (((playersWaiting != 0) && (playersWaiting >= (2 * notInitMatches))) || ((playersWaiting == 0) && (notInitMatches == 0))){
            matchID = createMatch(playerID);
        }

        //AGGIUNGO IL PLAYER ALLA WAITING LIST
        else{
            matchID = null;
            addPlayerToWaitingList(playerID);
        }

        clientHandlerUpdate(playerID, matchID);
        changeView(matchID, playerID);
    }

    private void unstackToMatch(Integer matchID){
        while ((getPlayersWaitingListSize() != 0) && !isNumReached(matchID)) {
            int opponentID = unstackPlayer();
            addPlayerToMatch(matchID, opponentID);
            clientHandlerUpdate(opponentID, matchID);
        }
        changeView(matchID);
    }

    private void checkMatchStart(Integer matchID){
        if (isNumReached(matchID)) {
            startMatch(matchID);
            nextMatchState(matchID);
            changeView(matchID);
        }
    }

    private void clientHandlerUpdate(Integer matchID, Integer playerID){
        ClientHandler clientHandler = getClientHandler(playerID);
        clientHandler.setMatchID(matchID);
        clientHandler.setPlayerID(playerID);
    }

    private void changeView(Integer matchID, Integer playerID){
        messageTX = new MessageEvent(
                "CONTROLLER_CHANGE_VIEW",
                playerID,
                matchID,
                getPlayerState(matchID, playerID),
                getMatchState(matchID));
        notify(messageTX);
    }

    private void changeView(Integer matchID){
        Set<Integer> matchPlayers = getMatchPlayers(matchID).keySet();
        for (Integer playerID: matchPlayers){
            messageTX = new MessageEvent(
                    "CONTROLLER_CHANGE_VIEW",
                    playerID,
                    matchID,
                    getPlayerState(matchID, playerID),
                    getMatchState(matchID));
            notify(messageTX);
        }
    }

    private void inputError(Integer playerID){
        messageTX = new MessageEvent(playerID,true);
        notify(messageTX);
    }
}





