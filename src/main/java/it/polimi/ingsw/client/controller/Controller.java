package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> implements Observer<String> {

    static ExecutorService executor = Executors.newSingleThreadExecutor();

    private ControlState controlState;
    private static MessageEvent message;
    private boolean messageReady;
    Player player;

    public Controller() {
        controlState = new StartingStatus();
        message = new MessageEvent();
    }

    @Override
    public void update(String viewObject) {
        executor.submit(() -> execute(viewObject));
    }

    private synchronized void execute(String viewObject) {

        checkStatus();

        //System.out.println(viewObject + " -> " + controlState);

        messageReady = false;
        messageReady = controlState.processingMessage(viewObject);

        if (messageReady) {
            notify(message);
            reset();
        }
    }

    public void setState(ControlState ctrlState){
        this.controlState=ctrlState;
    }

    public static MessageEvent getMessage(){
        return message;
    }

    public ControlState getControlState() {return controlState;}

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

    private synchronized void checkStatus() {
        player = View.getPlayer();

        if (player.getPlayerState()==null && player.getMatchState()==null)
            return;
        else if (player.getPlayerState()==PlayerState.ACTIVE) {
            switch (player.getMatchState()) {
                case GETTING_PLAYERS_NUM:
                    controlState = new SelectionNumberStatus();
                    break;
                case SELECTING_GOD_CARDS:
                    if (controlState.getClass() == SelectingGodCardsStatus.class)
                        return;
                    controlState = new SelectingGodCardsStatus();
                    break;
                case SELECTING_SPECIAL_COMMAND:
                    controlState = new SelectingSpecialCommandStatus();
                    break;
                case PLACING_WORKERS:
                    if (controlState.getClass() == PlacingWorkersStatus.class)
                        return;
                    controlState = new PlacingWorkersStatus();
                    break;
                case RUNNING:
                    controlState = new RunningStatus();
                    break;
                default:
                    controlState = new WaitingStatus();
                    break; }
        } else controlState = new WaitingStatus();
    }

    /*
    ------------------- UTILE PER I TEST ------------------
     */

    public boolean isMessageReady(){
        return messageReady;
    }
}
