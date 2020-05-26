package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.client.GUI.View;
import it.polimi.ingsw.utilities.MessageEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitingList extends State{

    @FXML
    BorderPane borderPane;



    @Override
    public void showPane(){
        Platform.runLater(() -> Controller.replaceSceneContent("fxml_files/waitingList.fxml"));
    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){

    }

    @Override
    public void update(MessageEvent message){
        if (message.getError()){
            System.out.println(message);
            showError();
        }
        else if (message.isFinished()){
            Database.setCurrentState(new userDisconnected());
            Database.getCurrentState().showPane();
        }
        else {
            Database.updateStandardData(message);
            View.updateView();
            Database.getCurrentState().showPane();
            new Thread(()->{
                Database.getNetworkHandler().removeObserver(this);
                this.removeObserver(Database.getNetworkHandler());
            }).start();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(Database.getNetworkHandler());
        Database.getNetworkHandler().addObserver(this);

        final ProgressIndicator pi = new ProgressIndicator(-1.0f);
        pi.setStyle(" -fx-progress-color: dodgerblue;");
        pi.setMinWidth(130);
        pi.setMinHeight(130);
        final HBox hb = new HBox(pi);
        final VBox vb = new VBox(hb);

        hb.setAlignment(Pos.CENTER);
        vb.setAlignment(Pos.CENTER);

        borderPane.setCenter(pi);
    }
}
