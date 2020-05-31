package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.utilities.MessageEvent;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.Database.getNetworkHandler;

public class userDisconnected extends State{
    @Override
    public void showPane(){
        Platform.runLater(() -> Controller.replaceSceneContent("fxml_files/userHasDisconnected.fxml"));
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
        getNetworkHandler().shutdownAll();
    }


    public void userNewGame(MouseEvent mouseEvent){
        //provare a chiudere conneessione server
        Controller.userNewGame();
    }

    public void userExit(MouseEvent mouseEvent){
        Controller.exit();
    }


}
