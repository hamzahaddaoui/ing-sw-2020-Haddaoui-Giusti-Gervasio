package it.polimi.ingsw.server.model.decorators;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utilities.Position;

public class HephaestusDecorator extends CommandsDecorator {
    static final GodCards card = GodCards.Hephaestus;

    private boolean secondBuild;
    private Position firstBuildPosition;

    public HephaestusDecorator(Commands commands) {
        this.commands = commands;
    }

    @Override
    public void build(Position position, Player player) {
        if(firstBuildPosition != null && secondBuild)
            return;
    }

    /**
     * Activates (deactivates) special function related to a certain player
     *
     */
    @Override
    public void specialFunctionSetUnset() {
        secondBuild ^= true;
    }

    @Override
    public void reset(Player player) {
        commands.reset(player);
        secondBuild = false;
        firstBuildPosition = null;
    }

}