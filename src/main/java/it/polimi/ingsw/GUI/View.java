package it.polimi.ingsw.GUI;

import it.polimi.ingsw.GUI.controller.*;

import static it.polimi.ingsw.GUI.Database.*;

public class View {

    static public void updateView() {
        if (getNickname() == null) {
            setCurrentState(new StartState());
            return;
        }
        switch (getMatchState()) { //NON CAPISCO PERCHE QUESTI IF... SE SI PUò TOGLIAMOLI!
            case GETTING_PLAYERS_NUM:
                if (getCurrentState().getClass() != GettingPlayersNum.class)
                    setCurrentState(new GettingPlayersNum());
                break;
            case WAITING_FOR_PLAYERS:
                if (getCurrentState().getClass() != WaitingForPlayers.class)
                    setCurrentState(new WaitingForPlayers());
                break;
            case SELECTING_GOD_CARDS:
                if (getCurrentState().getClass() == StartState.class){
                    setCurrentState(new WaitingForPlayers());
                }
                else{
                    setCurrentState(new SelectingGodCards());
                }
                break;
            case SELECTING_SPECIAL_COMMAND:
                if (getCurrentState().getClass() != SelectingSpecialCommand.class)
                    setCurrentState(new SelectingSpecialCommand());
                break;
            case PLACING_WORKERS:
                if (getCurrentState().getClass() != PlacingWorkers.class)
                    setCurrentState(new PlacingWorkers());
                break;
            case RUNNING:
                if (getCurrentState().getClass() != Running.class)
                    setCurrentState(new Running());
                break;
            case FINISHED:
                setCurrentState(new StartState());
                break;
            default:
                setCurrentState(new WaitingList());
        }
    }

}
