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

