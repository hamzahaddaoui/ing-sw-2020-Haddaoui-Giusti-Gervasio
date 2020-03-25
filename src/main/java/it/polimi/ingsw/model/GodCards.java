package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decorators.*;

public enum GodCards {
    Apollo("Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated") {
        @Override
        GameEntity create(GameEntity entity) {
            return new ApolloDecorator(entity);
        }
    },
    Artemis("Your Worker may move one additional time, but not back to its initial space") {
        @Override
        GameEntity create(GameEntity entity) {
            return new ArtemisDecorator(entity);
        }
    },
    Athena("If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn") {
        @Override
        GameEntity create(GameEntity entity) {
            return new AthenaDecorator(entity);
        }
    },
    Atlas("Your Worker may build a dome at any level") {
        @Override
        GameEntity create(GameEntity entity) {
            return new AtlasDecorator(entity);
        }
    },
    Demeter("Your Worker may build one additional time, but not on the same space") {
        @Override
        GameEntity create(GameEntity entity) {
            return new DemeterDecorator(entity);
        }
    },
    Hephaestus("Your Worker may build one additional block (not dome) on top of your first block") {
        @Override
        GameEntity create(GameEntity entity) {
            return new HephaestusDecorator(entity);
        }
    },
    Minotaur("Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level") {
        @Override
        GameEntity create(GameEntity entity) {
            return new MinotaurDecorator(entity);
        }
    },
    Pan("You also win if your Worker moves down two or more levels") {
        @Override
        GameEntity create(GameEntity entity) {
            return new PanDecorator(entity);
        }
    },
    Prometheus("If your Worker does not move up, it may build both before and after moving") {
        @Override
        GameEntity create(GameEntity entity) {
            return new PrometheusDecorator(entity);
        }
    };

    private final String description;

    GodCards(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    abstract GameEntity create(GameEntity entity);

}

