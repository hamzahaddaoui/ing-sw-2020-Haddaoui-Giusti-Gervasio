package it.polimi.ingsw.utilities;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer){
            observers.add(observer);
    }

    public void removeObserver(Observer<T> observer){
            observers.remove(observer);
    }

    public List<Observer<T>> getObservers(){
        return observers;
    }

    protected void notify(T message){
            for(Observer<T> observer : observers){
                observer.update(message);
            }
    }

    protected void notify(List<Observer<T>> observerList, T message){
            for(Observer<T> observer : observerList){
                observer.update(message);
            }
    }


}
