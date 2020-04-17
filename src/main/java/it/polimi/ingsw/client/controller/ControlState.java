package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.utilities.MessageEvent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class ControlState {


    public void keyTyped(KeyEvent e, MessageEvent messageEvent) {

    }


    public void keyPressed(KeyEvent e, MessageEvent messageEvent) {

    }


    public void keyReleased(KeyEvent e, MessageEvent messageEvent) {

    }

    public void nextState(Controller ctrl){

    }

}
