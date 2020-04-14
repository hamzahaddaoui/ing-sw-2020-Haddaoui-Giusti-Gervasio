package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.GodCards;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;

import java.util.Set;

public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    MessageEvent message;
    Integer matchID, playerID;

    /*
     *  - RAGGRUPPARE PARTI SIMILI IN FUNZIONI
     *  - AGGIUNGERE AGGIORNAMENTI MATCHSTATE DOPO OGNI FUNZIONE
     *  - SE C'è DA AGGIORNARE LA VIEW, CREARE UN MESSAGGIO, E FARE NOTIFY
     *
     * */

    @Override
    public void update(MessageEvent message){
        this.message = message;
        playerID = message.getPlayerID();
        matchID = message.getMatchID();

        if (matchID == null) {
            int playersWaiting = GameModel.getPlayersWaitingListSize();
            int notInitMatches = GameModel.getNotInitMatchesListSize();
            int initMatches = GameModel.getInitMatchesListSize();

            if (! GameModel.isNickAvailable(message.getNickname())) {
                //errore! nickname non disponibile
                return;
            }

            if (playersWaiting == 0) {
                if (initMatches == 0)
                    matchCreationManagement(notInitMatches == 0);
                else {
                    addToMatch();
                    checkMatchStart();
                }
            } else
                matchCreationManagement(playersWaiting >= 2 * notInitMatches);

            Server.getSocket(playerID).setMatchID(matchID);
        }

        checkCurrentPlayer();

        switch (GameModel.getMatchState(matchID)) {
            case "GETTING_PLAYERS_NUM":
                int playersNum = message.getPlayersNum();
                if (playersNum == 2 || playersNum == 3){
                    GameModel.setMatchPlayersNum(matchID, playersNum);
                    unstackToMatch();
                    checkMatchStart();
                }
                else{
                    //errore
                    return;
                }

                break;
            case "WAITING_FOR_PLAYERS":
                //nothing to do... lol
                break;
            case "SELECTING_GOD_CARDS":
                GameModel.setMatchCards(matchID, message.getGodCards());
                //NEXT MATCH STATE-NEXT PLAYER
                break;
            case "SELECTING_SPECIAL_COMMANDS":
                if (!GameModel.getMatchCards(matchID).contains(message.getGodCard())){
                    //error!!!!
                    return;
                }
                GameModel.selectPlayerCard(matchID, message.getGodCard());
                //nextMatchTurn
                //se DECK è VUOTO allora nextMatch state
                break;
            case "PLACING WORKER":
                break;
            case "RUNNING":
                break;
            case "FINISHED":
                break;
        }


    }


    private void checkCurrentPlayer(){
        if (playerID == GameModel.getCurrentPlayerID(matchID)){
            return;
        }
        else{
            //OPERATION FORBIDDEN!!!!!
        }
    }

    //se operation è true, allora creo un match
    //se è false, non posso farlo, quindi mi metto in lista d'attesa
    private void matchCreationManagement(boolean operation){
        if (operation)
            matchID = GameModel.createMatch(matchID, playerID, message.getNickname());
        else
            GameModel.addToWaitingList(playerID, message.getNickname());
    }

    private void addToMatch(){
        matchID = GameModel.getInitMatchID();
        GameModel.addPlayerToMatch(matchID, playerID, message.getNickname());
        Server.getSocket(playerID).setMatchID(matchID);
    }

    private void unstackToMatch(){
        int opponentID;
        while (GameModel.getPlayersWaitingListSize() != 0
               && !GameModel.isNumReached(matchID)) {

            opponentID = GameModel.unstackPlayerToMatch(matchID);
            Server.getSocket(opponentID).setMatchID(matchID);
        }
    }

    private void checkMatchStart(){
        if (GameModel.isNumReached(matchID)) {
            GameModel.startMatch(matchID);
        }
    }

}




        /*AGGIORNO LA VIEW
        MessageEvent = new MessageEvent()
        notify();
        */





