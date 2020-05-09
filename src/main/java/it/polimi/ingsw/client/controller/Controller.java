package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.controller.state.*;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller extends Observable<MessageEvent> implements Runnable {

    static ExecutorService executor = Executors.newCachedThreadPool();
    private static boolean activeInput = true;
    private static boolean messageReady;

    public static void setActiveInput(boolean activeInput) {
        Controller.activeInput = activeInput;
    }

    public static void setMessageReady(boolean messageReady) {
        Controller.messageReady = messageReady;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (activeInput) {
                activeInput = false;
                String input = scanner.nextLine();
                synchronized (View.class){
                    executor.submit(()-> {
                       MessageEvent message = View.getPlayer().getControlState().computeInput(input);
                       if (messageReady) {
                           notify(message);
                           messageReady = false;
                       }
                });}
                notifyAll();
            }
            else{
                scanner.nextLine();
            }
        }
    }

    public static void updateStandardData(MessageEvent messageEvent){
        Player player = View.getPlayer();
        if(messageEvent.getMatchState() != player.getMatchState() && messageEvent.getMatchState() != null){
            player.setMatchState(messageEvent.getMatchState());
        }
        if(messageEvent.getPlayerState() != player.getPlayerState() && messageEvent.getPlayerState() != null){
            player.setPlayerState(messageEvent.getPlayerState());
        }
        if(messageEvent.getTurnState() != player.getTurnState() && messageEvent.getTurnState() != null){
            player.setTurnState(messageEvent.getTurnState());
        }
        if(messageEvent.getMatchPlayers() != player.getMatchPlayers() && messageEvent.getMatchPlayers() != null)
            player.setMatchPlayers(messageEvent.getMatchPlayers());
        if(messageEvent.getCurrentPlayer() != player.getPlayer())
            player.setPlayer(messageEvent.getCurrentPlayer());
    }

    public static void updateControllerState(){
        Player player = View.getPlayer();
        ControlState controlState = player.getControlState();
        if (player.getNickname() == null && controlState.getClass() != NotInitialized.class){
            player.setControlState(new NotInitialized());
        }
        switch (player.getMatchState()){
            case GETTING_PLAYERS_NUM:
                if (controlState.getClass() != GettingPlayersNum.class)
                    player.setControlState( new GettingPlayersNum());
            case WAITING_FOR_PLAYERS:
                if (controlState.getClass() != WaitingForPlayers.class)
                    player.setControlState( new WaitingForPlayers());
            case SELECTING_GOD_CARDS:
                if (controlState.getClass() != SelectingGodCards.class)
                    player.setControlState( new SelectingGodCards());
            case SELECTING_SPECIAL_COMMAND:
                if (controlState.getClass() != SelectingSpecialCommand.class)
                    player.setControlState( new SelectingSpecialCommand());
            case PLACING_WORKERS:
                if (controlState.getClass() != PlacingWorkers.class)
                    player.setControlState( new PlacingWorkers());
            case RUNNING:
                if (controlState.getClass() != Running.class)
                    player.setControlState( new Running());
            default:
                player.setControlState( new WaitingList());
        }
    }


    /*private ControlState controlState;
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

    public boolean isMessageReady(){
        return messageReady;
    }
    */

}
