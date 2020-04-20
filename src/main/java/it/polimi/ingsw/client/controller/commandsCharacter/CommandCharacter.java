package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.utilities.MessageEvent;

public interface CommandCharacter {
    boolean executeNumberStatus();
    void executePlacingWorkerStatus();
    boolean executeRunningStatus();
    boolean executeSpecialCommandsStatus();
    boolean executeSelectingGodCardsStatus();
    void executeWaitingStatus();
}
