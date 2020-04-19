package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.utilities.MessageEvent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class ControlState {

    public void nextState(Controller ctrl) {}
    public boolean doSomething(MessageEvent messageEvent,Object viewObject) {return true;}

}
