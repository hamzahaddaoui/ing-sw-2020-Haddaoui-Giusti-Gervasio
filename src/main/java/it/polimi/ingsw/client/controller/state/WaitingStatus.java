package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.server.controller.state.GettingPlayersNum;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.PlayerState;

public class WaitingStatus extends ControlState {

    @Override
    public boolean processingMessage(Object viewObject) throws IllegalArgumentException{
        //forse faccio qualcosa nella view
        if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        System.out.println("\nWait for your turn!!\n");

        return false;
    }

}
