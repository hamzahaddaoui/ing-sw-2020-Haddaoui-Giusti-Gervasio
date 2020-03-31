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
     * @param worker     the player's selected worker, not null
     * @param position   the position that player have inserted, not null
     * @param billboard  the reference to the gameboard, not null
     */
    @Override
    public void build(Worker worker, Position position, Billboard billboard) {
        super.build(worker, position, billboard);
        if (billboard.getTowerHeight(position) < 3) {
            //scelta opzionale seconda costruzione
        }
    }
}