package it.polimi.ingsw.client.CLI.controller.state;

import it.polimi.ingsw.utilities.MessageEvent;

/**
 * @author giusti-leo , Vasio1298
 *
 * Abstract class of State Pattern that rapresents the tipology of the ControllerSatte
 *
 */

public abstract class ControlState {

    public abstract MessageEvent computeInput(String input);

    public abstract void updateData(MessageEvent message);

    public abstract String computeView();

    public abstract String error();
}

