package it.polimi.ingsw.client.controller.state;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.controller.commandsCharacter.CommandCharacter;
import it.polimi.ingsw.client.view.GameBoard;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.Player;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Position;

import javax.security.auth.callback.CallbackHandler;

public class RunningStatus extends ControlState {

    @Override
    public boolean processingMessage(String viewObject) throws IllegalArgumentException {

        super.checkMessage(viewObject);

        MessageEvent message = Controller.getMessage();
        GameBoard gameBoard = View.getGameBoard();

        int x = Character.getNumericValue(viewObject.charAt(0));

        if (viewObject.length() == 1)
            return processingChar(x, gameBoard, message);
        else if (viewObject.length() == 2 && x <= 4 && x >= 0) {
            int y = Character.getNumericValue(viewObject.charAt(1));
            if (y <= 4 && y >= 0)
                return processingPosition(x, y, gameBoard, message);
            else throw new IllegalArgumentException("\nposizione non valida!");
        }
        else throw new IllegalArgumentException("\ninput non corretto");

        /*if (!(viewObject instanceof InsertCharacter))
            throw new IllegalArgumentException("Comando non riconosciuto!");

        InsertCharacter characterView = (InsertCharacter) viewObject;
        CommandCharacter commandCharacter = characterView.apply();
        try {
            return commandCharacter.executeRunningStatus();
        }
        catch (IllegalArgumentException e) {
            e.getMessage();
            return false;
        }
    }*/

    }

    private boolean processingChar(int num, GameBoard gameBoard, MessageEvent message) {
        if (num == 15) {
            Player player = View.getPlayer();
            Position startingPosition = gameBoard.getStartingPosition();
            if (startingPosition != null) {
                if (player.getSpecialFunctionAvailable().get(startingPosition)) {
                    message.setStartPosition(startingPosition);
                    message.setSpecialFunction(true);
                    return true;
                } else throw new IllegalArgumentException("\nfunzione speciale non disponibile per questo worker!");
            } else throw new IllegalArgumentException("\ndevi prima selezionare il worker!");
        }
        else if (num == 14) {
            Player player = View.getPlayer();
            if (player.isTerminateTurnAvailable()) {
                message.setTerminateTurnAvailable(true);
                return true;
            } else throw new IllegalArgumentException("\nnon puoi ancora terminare il turno!");
        }
        else throw new IllegalArgumentException("\ninput incorretto!");
    }

    private boolean processingPosition(int x, int y, GameBoard gameBoard, MessageEvent message) {

        Position startingPosition = gameBoard.getStartingPosition();
        Position position = new Position(x,y);

        if (startingPosition == null) {
            if (gameBoard.isWorkerPresent(position)) {
                gameBoard.setStartingPosition(position);
                return false;
            } else throw new IllegalArgumentException("\nworker non esistente!");
        }
        else if (gameBoard.getWorkersAvailableCells(startingPosition).contains(position)) {
                message.setStartPosition(startingPosition);
                message.setEndPosition(position);
                return true;
        }
        else throw new IllegalArgumentException("\nposizione non disponibile!");
    }
}