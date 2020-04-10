package it.polimi.ingsw.utilities;

public interface Observer<T> {
    void update(T message);

    void update(Integer playerID, Integer matchID, T message);
}
