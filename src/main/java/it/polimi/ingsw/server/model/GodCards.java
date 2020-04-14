package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.decorators.*;

public enum GodCards {
    Apollo("Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated") {
        @Override
        Commands apply(Commands commands) {
            return new ApolloDecorator(commands);
        }
    },
    Artemis("Your Worker may move one additional time, but not back to its initial space") {
        @Override
        Commands apply(Commands commands) {
            return new ArtemisDecorator(commands);
        }
    },
    Athena("If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn") {
        @Override
        Commands apply(Commands commands) {
            return new AthenaDecorator(commands);
        }
    },
    Atlas("Your Worker may build a dome at any level") {
        @Override
        Commands apply(Commands commands) {
            return new AtlasDecorator(commands);
        }
    },
    Demeter("Your Worker may build one additional time, but not on the same space") {
        @Override
        Commands apply(Commands commands) {
            return new DemeterDecorator(commands);
        }
    },
    Hephaestus("Your Worker may build one additional block (not dome) on top of your first block") {
        @Override
        Commands apply(Commands commands) {
            return new HephaestusDecorator(commands);
        }
    },
    Minotaur("Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level") {
        @Override
        Commands apply(Commands commands) {
            return new MinotaurDecorator(commands);
        }
    },
    Pan("You also win if your Worker moves down two or more levels") {
        @Override
        Commands apply(Commands commands) {
            return new PanDecorator(commands);
        }
    },
    Prometheus("If your Worker does not move up, it may build both before and after moving") {
        @Override
        Commands apply(Commands commands) {
            return new PrometheusDecorator(commands);
        }
    };

    private final String description;

    GodCards(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Decorator of a basic commands.
     *
     * @param commands the basic command of the player, not null
     * @return the decorator command of the corresponding gods
     */
    abstract Commands apply(Commands commands);
}

