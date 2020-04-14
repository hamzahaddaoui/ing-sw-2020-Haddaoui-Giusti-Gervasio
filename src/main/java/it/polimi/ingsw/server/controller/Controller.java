package it.polimi.ingsw.server.controller;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;



public class Controller extends Observable<MessageEvent> implements Observer<MessageEvent> {
    MessageEvent messageRX, messageTX;
    Integer matchID, playerID;
    Integer nextPlayerID;

    /*
     *  - RAGGRUPPARE PARTI SIMILI IN FUNZIONI
     *  - AGGIUNGERE AGGIORNAMENTI MATCHSTATE DOPO OGNI FUNZIONE
     *  - SE C'è DA AGGIORNARE LA VIEW, CREARE UN MESSAGGIO, E FARE NOTIFY
     *
     * */

    @Override
    public void update(MessageEvent messageRX){
        this.messageRX = messageRX;

        playerID = messageRX.getPlayerID();
        matchID = messageRX.getMatchID();

        if (matchID == null) {
            int playersWaiting = GameModel.getPlayersWaitingListSize();
            int notInitMatches = GameModel.getNotInitMatchesListSize();
            int initMatches = GameModel.getInitMatchesListSize();

            if (! GameModel.isNickAvailable(messageRX.getNickname())) {
                //errore! nickname non disponibile
                messageTX = new MessageEvent(playerID, matchID, true);
                notify(messageTX);
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

            messageTX = new MessageEvent(
                    "CONTROLLER_CHANGE_VIEW",
                    playerID,
                    matchID,
                    GameModel.getPlayerState(matchID, playerID),
                    GameModel.getMatchState(matchID));

            messageTX = new MessageEvent(
                    "CONTROLLER_CHANGE_VIEW",
                    playerID,
                    matchID,
                    GameModel.getPlayerState(matchID, playerID),
                    GameModel.getMatchState(matchID));
            return;
        }

        checkCurrentPlayer();

        switch (GameModel.getMatchState(matchID)) {
            case "GETTING_PLAYERS_NUM":
                int playersNum = messageRX.getPlayersNum();
                if (playersNum == 2 || playersNum == 3){
                    GameModel.setMatchPlayersNum(matchID, playersNum);
                    messageTX = new MessageEvent(
                            "CONTROLLER_CHANGE_VIEW",
                            matchID,
                            GameModel.getMatchState(matchID));
                    notify(messageTX);

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
                GameModel.setMatchCards(matchID, messageRX.getGodCards());
                GameModel.nextMatchTurn(matchID);
                GameModel.nextMatchState(matchID);
                break;
            case "SELECTING_SPECIAL_COMMAND":
                if (!GameModel.getMatchCards(matchID).contains(messageRX.getGodCard())){
                    //error!!!!
                    return;
                }
                GameModel.selectPlayerCard(matchID, messageRX.getGodCard());
                GameModel.nextMatchTurn(matchID);
                if (GameModel.getMatchCards(matchID).size() == 0)
                    GameModel.nextMatchState(matchID);
                break;
            case "PLACING WORKERS":
                GameModel.placeWorker(matchID,playerID,messageRX.getEndPosition());
                if (GameModel.hasPlayerPlacedWorkers(matchID)){
                    GameModel.nextMatchTurn(matchID);
                    if (GameModel.hasPlayerPlacedWorkers(matchID)){
                        GameModel.nextMatchState(matchID);
                    }
                }
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
        if (operation){
            matchID = GameModel.createMatch(playerID, messageRX.getNickname());
        }
        else
            GameModel.addToWaitingList(playerID, messageRX.getNickname());
    }

    private void addToMatch(){
        matchID = GameModel.getInitMatchID();
        GameModel.addPlayerToMatch(matchID, playerID, messageRX.getNickname());
        Server.getSocket(playerID).setMatchID(matchID);
    }

    private void unstackToMatch(){
        int opponentID;
        while (GameModel.getPlayersWaitingListSize() != 0 && !GameModel.isNumReached(matchID)) {
            opponentID = GameModel.unstackPlayerToMatch(matchID);
            Server.getSocket(opponentID).setMatchID(matchID);
            messageTX = new MessageEvent(
                    "CONTROLLER_CHANGE_VIEW",
                    opponentID,
                    matchID,
                    GameModel.getPlayerState(matchID, opponentID),
                    GameModel.getMatchState(matchID));
            notify(messageTX);
        }
    }

    private void checkMatchStart(){
        if (GameModel.isNumReached(matchID)) {
            GameModel.startMatch(matchID);

        }
    }

}





