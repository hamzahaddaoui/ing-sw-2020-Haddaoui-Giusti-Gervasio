package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;

public class WCommand implements CommandCharacter {

    @Override
    public boolean executeNumberStatus() {

        //messaggio View: "non fa nulla"
        return false;
    }

    @Override
    public void executePlacingWorkerStatus() {

    }

    @Override
    public void executeRunningStatus() {

    }

    @Override
    public void executeGodCardsStatus() {

    }

    @Override
    public void executeSpecialCommandsStatus() {

    }

    @Override
    public boolean executeSelectingGodCardsStatus() {
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }
}
