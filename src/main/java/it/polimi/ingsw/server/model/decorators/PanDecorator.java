package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;

/**
 * @author Vasio1298
 *
 * Pan Commands Decorator
 * Description: "You also win if your Worker moves down two or more levels"
 * Differente methods from Basic Commands: winningCondition
 */

public class PanDecorator extends CommandsDecorator {

    static final private GodCards card = GodCards.Pan;

    public PanDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * Method that implements the winning conditions for Pan.
     * <p>
     * If the player moves down for two or more levels, he wins.
     * Else, check if the player wins with the standard method.
     * <p>
     *
     * @param player  the player who is making the game turn, not null
     * @return        true if he wins, false otherwise
     */
    @Override
    public boolean winningCondition(Player player) {
        Worker worker = player.getCurrentWorker();
        return worker.getHeightVariation() <= -2 || super.winningCondition(player);
    }

}
