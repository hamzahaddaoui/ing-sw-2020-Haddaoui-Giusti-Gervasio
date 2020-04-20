package it.polimi.ingsw.client.controller.commandsCharacter;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;

public class EnterCommand implements CommandCharacter {
    @Override
    public boolean executeNumberStatus() {
        Controller.getMessage().setPlayersNum(View.getPlayersNumChoice());
        return true;
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
        /*if(carta selezionata non è nulla e  View.getGodCards().size()<= View.getPlayersNum())
        *inserisci dentro arraylist lato VIEW;
        *
        */
        if (View.getGodCards().size() == View.getPlayersNum()) {
            Controller.getMessage().setGodCards(View.getGodCards());
        }
        return false;
    }

    @Override
    public void executeWaitingStatus() {

    }

}
