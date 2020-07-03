package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.utilities.MessageEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author: hamzahaddaoui
 *
 * Controller/View class. Manages the state on which the game has finished and the user has to decide if starting a new game, or exiting.
 */

public class gameFinish extends State {
    @FXML
    Label finishLabel;

    @Override
    public void showPane(){

    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){

    }

    @Override
    public void update(MessageEvent message){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    public void userNewGame(MouseEvent mouseEvent){
        //provare a chiudere conneessione server
        Controller.userNewGame();
    }

    public void userExit(MouseEvent mouseEvent){
        Controller.exit();
    }
}
