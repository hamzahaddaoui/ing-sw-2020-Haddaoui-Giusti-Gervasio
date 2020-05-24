package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.utilities.MessageEvent;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.GUI.Controller.exit;
import static it.polimi.ingsw.GUI.Controller.replaceSceneContent;
import static it.polimi.ingsw.GUI.Database.*;

public class userDisconnected extends State{
    @Override
    public void showPane(){
        Platform.runLater(() -> replaceSceneContent("fxml_files/userHasDisconnected.fxml"));
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


    public static void userHasDisconnected(){
    }

    public void userNewGame(MouseEvent mouseEvent){
        getNetworkHandler().shutdownAll();
        wipeData();
        setCurrentState(new StartState());
        getCurrentState().showPane();
    }

    public void userExit(MouseEvent mouseEvent){
        exit();
    }
}
