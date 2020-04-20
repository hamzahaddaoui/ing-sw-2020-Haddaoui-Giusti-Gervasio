package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.utilities.MessageEvent;

public class QCommand implements CommandCharacter {
    @Override
    public boolean executeNumberStatus() {

        //messaggio View: "non fa nulla"
        return false;
    }

    @Override
    public boolean executePlacingWorkerStatus() {
        return false;
    }

    @Override
    public boolean executeRunningStatus() {
        return false;
    }

    @Override
    public boolean executeSpecialCommandsStatus() {
        return false;
    }

    @Override
    public boolean executeSelectingGodCardsStatus() {
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

}
