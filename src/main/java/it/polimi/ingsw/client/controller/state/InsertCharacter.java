package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.commandsCharacter.*;

public enum InsertCharacter {
    W {
        @Override
        CommandCharacter apply() {
            return new WCommand();
        }
    },
    A{
        @Override
    CommandCharacter apply() {
        return new ACommand();
        }
    },
    S{
        @Override
        CommandCharacter apply() {
            return new SCommand();
        }
    },
    D{
        @Override
        CommandCharacter apply() {
            return new DCommand();
        }
    },
    ENTER{
        @Override
        CommandCharacter apply() {
            return new EnterCommand();
        }
    },
    F{
        @Override
        CommandCharacter apply() {
            return new FCommand();
        }
    },
    E{
        @Override
        CommandCharacter apply() {
            return new ECommand();
        }
    },
    Q {
        @Override
        CommandCharacter apply() {
            return new QCommand();
        }
    };

    abstract CommandCharacter apply();
}
