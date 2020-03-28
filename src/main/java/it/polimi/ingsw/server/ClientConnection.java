package it.polimi.ingsw.server;

import it.polimi.ingsw.utilities.Message;
import it.polimi.ingsw.utilities.Observer;

public interface ClientConnection{

    void closeConnection();

    void addObserver(Observer<Message> observer);

    void asyncSend(Object message);
}
