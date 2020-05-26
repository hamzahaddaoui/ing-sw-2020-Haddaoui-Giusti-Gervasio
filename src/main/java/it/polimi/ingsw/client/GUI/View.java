package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUI.controller.*;
import it.polimi.ingsw.client.GUI.controller.*;

public class View {

    static public void updateView() {
        if (Database.getNickname() == null) {
            Database.setCurrentState(new StartState());
            return;
        }
        switch (Database.getMatchState()) {
            case GETTING_PLAYERS_NUM:
                if (Database.getCurrentState().getClass() != GettingPlayersNum.class)
                    Database.setCurrentState(new GettingPlayersNum());
                break;
            case WAITING_FOR_PLAYERS:
                if (Database.getCurrentState().getClass() != WaitingForPlayers.class)
                    Database.setCurrentState(new WaitingForPlayers());
                break;
            case SELECTING_GOD_CARDS:
                if (Database.getCurrentState().getClass() == StartState.class){
                    Database.setCurrentState(new WaitingForPlayers());
                }
                else{
                    Database.setCurrentState(new SelectingGodCards());
                }
                break;
            case SELECTING_SPECIAL_COMMAND:
                if (Database.getCurrentState().getClass() != SelectingSpecialCommand.class)
                    Database.setCurrentState(new SelectingSpecialCommand());
                break;
            case PLACING_WORKERS:
                if (Database.getCurrentState().getClass() != PlacingWorkers.class)
                    Database.setCurrentState(new PlacingWorkers());
                break;
            case RUNNING:
                if (Database.getCurrentState().getClass() != Running.class)
                    Database.setCurrentState(new Running());
                break;
            case FINISHED:
                Database.setCurrentState(new StartState());
                break;
            default:
                Database.setCurrentState(new WaitingList());
        }
    }

}
