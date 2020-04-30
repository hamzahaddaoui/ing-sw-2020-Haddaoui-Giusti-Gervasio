package it.polimi.ingsw.client.controller.state;

import it.polimi.ingsw.client.controller.commandsCharacter.*;

public enum InsertCharacter {
    W (30474) {
        @Override
        CommandCharacter apply() {
            return new WCommand();
        }
    },
    A (24842){
        @Override
    CommandCharacter apply() {
        return new ACommand();
        }
    },
    S (29450){
        @Override
        CommandCharacter apply() {
            return new SCommand();
        }
    },
    D (25610){
        @Override
        CommandCharacter apply() {
            return new DCommand();
        }
    },
    ENTER (2570){
        @Override
        CommandCharacter apply() {
            return new EnterCommand();
        }
    },
    F (26122){
        @Override
        CommandCharacter apply() {
            return new FCommand();
        }
    },
    E (25866){
        @Override
        CommandCharacter apply() {
            return new ECommand();
        }
    },
    Q (28938){
        @Override
        CommandCharacter apply() {
            return new QCommand();
        }
    };

    InsertCharacter(int code) {
        this.code = code;
    }

    private final int code;

    abstract CommandCharacter apply();

    public int getCode() {return code;}
}
