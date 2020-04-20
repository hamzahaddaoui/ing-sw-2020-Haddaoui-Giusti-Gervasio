package it.polimi.ingsw.client.controller.commandsCharacter;

public interface CommandCharacter {
    boolean executeNumberStatus();
    void executePlacingWorkerStatus();
    void executeRunningStatus();
    void executeGodCardsStatus();
    void executeSpecialCommandsStatus();
    boolean executeSelectingGodCardsStatus();
    void executeWaitingStatus();
}
