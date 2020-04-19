package it.polimi.ingsw.client.controller;

public enum InsertCharacter {
    W {
        @Override
        void execution(String ctrlState) {
            //switchcase
            //
        }
    },
    A{
        @Override
        void execution(String ctrlState) {
        }
    },
    S{
        @Override
        void execution(String ctrlState) {
        }
    },
    D{
        @Override
        void execution(String ctrlState) {
        }
    },
    ENTER{
        @Override
        void execution(String ctrlState) {
        }
    },
    F{
        @Override
        void execution(String ctrlState) {
        }
    },
    E{
        @Override
        void execution(String ctrlState) {
        }
    },
    Q {
        @Override
        void execution(String ctrlState) {
        }
    };

    abstract void execution(String ctrlState);
}
