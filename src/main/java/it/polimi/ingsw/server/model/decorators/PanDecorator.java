package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;

public class PanDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Pan;

    public PanDecorator(Commands commands){
        this.commands=commands;
    }

    /**
     * Method that implements the winning conditions for Pan.
     * <p>
     * If the player moves down for two or more levels, he wins.
     * Else, check if the player wins with the standard method.
     * <p>
     * {@link Player#getCurrentWorker()}
     * {@link Worker#getHeightVariation()}
     * {@link super#winningCondition(Player)}
     *
     * @param player  the player who is making the game turn, not null
     * @return        true if he wins, false otherwise
     */
    @Override
    public boolean winningCondition(Player player) {
        Worker worker = player.getCurrentWorker();
        if (worker.getHeightVariation() <= -2)
            return true;
        else
            return super.winningCondition(player);
    }

}
