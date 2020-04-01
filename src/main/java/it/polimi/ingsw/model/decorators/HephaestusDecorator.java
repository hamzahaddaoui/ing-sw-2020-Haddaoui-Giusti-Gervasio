package it.polimi.ingsw.model.decorators;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utilities.Position;

public class HephaestusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Hephaestus;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
    }

    /**
     * Method that allows the specific building block action for Hephaestus.
     * <p>
     * The player can build two blocks in the same space, but not a dome.
     * <p>
     * {@link Billboard#getTowerHeight(Position)}
     *
     * @param player
     * @param position   the position that player have inserted, not null
     */
    @Override
    public void build(Player player, Position position) {
        super.build(player, position);
        if (billboard.getTowerHeight(position) < 3) {
            //scelta opzionale seconda costruzione
        }
    }
}