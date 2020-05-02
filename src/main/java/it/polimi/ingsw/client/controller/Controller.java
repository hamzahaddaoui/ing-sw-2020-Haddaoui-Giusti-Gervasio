package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> implements Observer<Object>{

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
        synchronized (View.getPlayer()){
            synchronized (View.getGameBoard()){
                checkState();
                executor.submit(() -> execute(viewObject));
            }
        }

    }

    private synchronized void execute(Object viewObject) {
        messageReady = false;
        messageReady = controlState.processingMessage(viewObject);

        if (messageReady) {
            notify(message);
            reset();
        }
    }

    public synchronized void checkState(){
        MatchState matchState = View.getPlayer().getMatchState();
        PlayerState playerState = View.getPlayer().getPlayerState();

        if(playerState == null && matchState == null) {
            controlState = new StartingStatus();
            return;
        }
        else if(matchState == null || playerState == PlayerState.IDLE || playerState == PlayerState.INITIALIZED){
            controlState = new WaitingStatus();
            return;
        }
        else{
            switch (matchState){
                case GETTING_PLAYERS_NUM: {
                    if(playerState == PlayerState.ACTIVE)
                        controlState = new SelectionNumberStatus();
                            return;
                }
                case SELECTING_GOD_CARDS: {
                    if(playerState == PlayerState.ACTIVE)
                        controlState = new SelectingGodCardsStatus();
                            return;
                }
                case SELECTING_SPECIAL_COMMAND: {
                    if(playerState == PlayerState.ACTIVE)
                        controlState = new SelectingSpecialCommandStatus();
                            return;
                }
                case PLACING_WORKERS: {
                    if(playerState == PlayerState.ACTIVE)
                        controlState = new PlacingWorkersStatus();
                            return;
                }
                case RUNNING: {
                    if(playerState == PlayerState.ACTIVE)
                        controlState = new RunningStatus();
                            return;
                }
            }
        }
    }

    public void setState(ControlState ctrlState){
        this.controlState=ctrlState;
    }

    public static MessageEvent getMessage(){
        return message;
    }

    public ControlState getControlState() {return this.controlState;}

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


    /*
    ------------------- UTILE PER I TEST ------------------
     */

    public boolean isMessageReady(){
        return messageReady;
    }
}
