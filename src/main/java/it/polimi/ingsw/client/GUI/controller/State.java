package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import javafx.fxml.Initializable;

public abstract class State extends Observable<MessageEvent> implements Observer<MessageEvent>, Initializable {
    /**
     * Initialize the pane of the current state
     */
    public abstract void showPane();

    /**
     * Shows the error related to a certain input of the user
     */
    public abstract void showError();

    /**
     * Send the data to the server
     */
    public abstract void sendData();


}
