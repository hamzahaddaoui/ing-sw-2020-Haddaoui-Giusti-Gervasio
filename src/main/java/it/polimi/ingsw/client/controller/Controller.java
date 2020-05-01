package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.ControlState;
import it.polimi.ingsw.client.controller.state.SelectionNumberStatus;
import it.polimi.ingsw.client.controller.state.StartingStatus;
import it.polimi.ingsw.client.controller.state.WaitingStatus;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.Match;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> implements Observer<Object> {

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    private ControlState controlState;
    private static MessageEvent message;
    private boolean messageReady = false;
    Player player = View.getPlayer();

    public Controller() {
        controlState = new StartingStatus();
        message = new MessageEvent();
    }

    @Override
    public synchronized void update(Object viewObject){
        checkStatus();
        executor.submit(() -> execute(viewObject));
    }

    private synchronized void execute(Object viewObject) {
        messageReady = false;
        messageReady = controlState.processingMessage(viewObject);

        if (messageReady) {
            if (playerState != null)
                message.setPlayerID(player.getPlayerID());
            if (matchState != null)
                message.setMatchID(player.getMatchID());
            notify(message);
            reset();
        }
        System.out.print("\nctrl MATCHSTATE ->" + matchState);
        System.out.print("/   ctrl PLAYERSTATE ->" + playerState);
        System.out.println("  / ctrl ControlSTATE ->" + controlState + "  \n ");
    }

    public synchronized void nextState(){
        MatchState matchState = View.getPlayer().getMatchState();
        PlayerState playerState = View.getPlayer().getPlayerState();
        if(playerState == null && matchState == null) {
            controlState = new StartingStatus();
            return;
        }
        else if(matchState == null && playerState != null){
            controlState = new WaitingStatus();
            return;
        }
        switch (matchState){
            case GETTING_PLAYERS_NUM: {
                if(playerState == PlayerState.ACTIVE) {
                    controlState = new SelectionNumberStatus();
                }
                else{
                    controlState = new WaitingStatus();
                }
                return;
            }
            case : {
                if(playerState == PlayerState.ACTIVE) {
                    controlState = new SelectionNumberStatus();
                }
                else{
                    controlState = new WaitingStatus();
                }
                return;
            }

        }
    }

    public MatchState getMatchState() {
        return this.matchState;
    }

    public void setMatchState(MatchState matchState){
        this.matchState = matchState;
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    public void setPlayerState(PlayerState playerState){
        this.playerState = playerState;
    }

    public void setState(ControlState ctrlState){
        this.controlState=ctrlState;
    }

    public static MessageEvent getMessage(){
        return message;
    }

    public ControlState getControlState() {return this.controlState;}

    public void setPlayerAndMatchState(PlayerState plState,MatchState matState) {
        playerState = plState;
        matchState = matState;
    }

    public void reset (){
        message.setMatchID(null);
        message.setPlayerID(null);
        message.setNickname(null);
        message.setPlayersNum(null);
        message.setGodCards(null);
        message.setGodCard(null);
        message.setInitializedPositions(null);
        message.setStartPosition(null);
        message.setEndPosition(null);
        message.setEndTurn(null);
        message.setSpecialFunction(null);
    }

    private void checkStatus() {
        PlayerState newPlayerState = player.getPlayerState();
        MatchState newMatchState = player.getMatchState();
        if (newMatchState == null && newPlayerState == null) {
            reset();
        } else if (newPlayerState != playerState || newMatchState != matchState) {
            playerState = newPlayerState;
            matchState = newMatchState;
            nextState();
        }
    }

    /*
    ------------------- UTILE PER I TEST ------------------
     */

    public boolean isMessageReady(){
        return messageReady;
    }
}
