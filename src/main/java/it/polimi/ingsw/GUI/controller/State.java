package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.Observable;
import it.polimi.ingsw.utilities.Observer;
import javafx.fxml.Initializable;

public abstract class State extends Observable<MessageEvent> implements Observer<MessageEvent>, Initializable {
    public abstract void showPane();

    public abstract void showError();

    public abstract void sendData();
}
