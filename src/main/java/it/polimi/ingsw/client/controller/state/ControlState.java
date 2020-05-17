package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

public abstract class ControlState {

    public abstract MessageEvent computeInput(String input);

    public abstract void updateData(MessageEvent message);

    public abstract String computeView();

    public abstract void error();
}
    /*public void updateCurrentState(){
        if (player.getNickname() == null){
            player.setControlState(new NotInitialized());
        }
        else if(player.getPlayerState() != PlayerState.ACTIVE){
            player.setControlState(new None());
        }
        switch (Database.getMatchState()){
            case GETTING_PLAYERS_NUM:
                Database.setCurrentState( new GettingPlayersNum());
            case WAITING_FOR_PLAYERS:
                Database.setCurrentState( new WaitingForPlayers());
            case SELECTING_GOD_CARDS:
                Database.setCurrentState( new SelectingGodCards());
            case SELECTING_SPECIAL_COMMAND:
                Database.setCurrentState( new SelectingSpecialCommand());
            case PLACING_WORKERS:
                Database.setCurrentState( new PlacingWorkers());
            case RUNNING:
                Database.setCurrentState( new Running());
            default:
                Database.setCurrentState( new None());
        }
    }


    /*public boolean processingMessage(String viewObject) {return false;}

    public boolean checkMessage(String viewObject) {
        if (viewObject == null) {
            System.out.println("NULL MESSAGE");
            return false; }
        else if (viewObject.equals("")) {
            System.out.println("MESSAGE IS EMPTY");
            return false;
        }
        return true;
    }*/

