package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.Controller;
import it.polimi.ingsw.GUI.Database;
import it.polimi.ingsw.GUI.View;
import it.polimi.ingsw.utilities.MessageEvent;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.GUI.Database.*;
import static it.polimi.ingsw.GUI.View.updateView;

public class GettingPlayersNum extends State {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(getNetworkHandler());
        getNetworkHandler().addObserver(this);
    }



    @Override
    public void showPane(){
        Platform.runLater(() -> Controller.replaceSceneContent("fxml_files/selectNum.fxml"));
    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setPlayersNum(getPlayersNum());
        notify(message);
    }

    @Override
    public void update(MessageEvent message){

        if (message.getError()){
            System.out.println(message);
            showError();

            return;
        }
        else {
            updateStandardData(message);
            if (updateView()) {
                getCurrentState().showPane();
                new Thread(() -> {
                    getNetworkHandler().removeObserver(this);
                    this.removeObserver(getNetworkHandler());
                }).start();
            }
        }
    }


    public void selectedTwoPlayers(MouseEvent mouseEvent){
        setPlayersNum(2);
        System.out.println("Match players: 2");
        sendData();
    }

    public void selectedThreePlayers(MouseEvent mouseEvent){
        setPlayersNum(3);
        System.out.println("Match players: 3");
        sendData();
    }


}
