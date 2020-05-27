package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.decorators.*;

public enum GodCards {
    Apollo {
        @Override
        Commands apply(Commands commands) {
            return new ApolloDecorator(commands);
        }
    },
    Artemis {
        @Override
        Commands apply(Commands commands) {
            return new ArtemisDecorator(commands);
        }
    },
    Athena{
        @Override
        Commands apply(Commands commands) {
            return new AthenaDecorator(commands);
        }
    },
    Atlas{
        @Override
        Commands apply(Commands commands) {
            return new AtlasDecorator(commands);
        }
    },
    Demeter{
        @Override
        Commands apply(Commands commands) {
            return new DemeterDecorator(commands);
        }
    },
    Hephaestus{
        @Override
        Commands apply(Commands commands) {
            return new HephaestusDecorator(commands);
        }
    },
    Minotaur{
        @Override
        Commands apply(Commands commands) {
            return new MinotaurDecorator(commands);
        }
    },
    Pan {
        @Override
        Commands apply(Commands commands) {
            return new PanDecorator(commands);
        }
    },
    Prometheus {
        @Override
        Commands apply(Commands commands) {
            return new PrometheusDecorator(commands);
        }
    },
    Charon {
        @Override
        Commands apply(Commands commands) {
            return new CharonDecorator(commands);
        }
    },
    Eros {
        @Override
        Commands apply(Commands commands) {
            return new ErosDecorator(commands);
        }
    },
    Hestia {
        @Override
        Commands apply(Commands commands) {
            return new HestiaDecorator(commands);
        }
    },
    Triton {
        @Override
        Commands apply(Commands commands) {
            return new TritonDecorator(commands);
        }
    },
    Zeus {
        @Override
        Commands apply(Commands commands) {
            return new ZeusDecorator(commands);
        }
    };

    /**
     * Decorator of a basic commands.
     *
     * @param commands the basic command of the player, not null
     * @return the decorator command of the corresponding gods
     */
    abstract Commands apply(Commands commands);
}

/*  -> Apollo("Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated"),
    -> Artemis("Your Worker may move one additional time, but not back to its initial space")
    -> Athena("If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn")
    Atlas("Your Worker may build a dome at any level")
    Demeter("Your Worker may build one additional time, but not on the same space")
    Hephaestus("Your Worker may build one additional block (not dome) on top of your first block")
    -> Minotaur("Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level")
    Pan("You also win if your Worker moves down two or more levels")
    Prometheus("If your Worker does not move up, it may build both before and after moving")
 */
