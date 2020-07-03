package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.GUI.View.updateView;

/**
 * @author: hamzahaddaoui
 *
 * Control/View class. Manages the waiting state, to start the match.
 */

public class WaitingForPlayers extends State {

    @FXML
    private BorderPane borderPane;
    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(Database.getNetworkHandler());
        Database.getNetworkHandler().addObserver(this);

        if (Database.getMatchState() == MatchState.SELECTING_GOD_CARDS){
            Button button = new Button();
            button.setId("button");
            button.getStylesheets().add("/css_files/waitingForPlayers.css");
            button.getStyleClass().add("button");
            VBox vbox = new VBox(button);
            vbox.setAlignment(Pos.CENTER);
            Platform.runLater(() -> borderPane.setBottom(vbox));
            button.setOnMouseEntered(actionEvent -> {
                button.setEffect(new Glow(0.4));
            });

            button.setOnMouseExited(actionEvent -> {
                button.setEffect(new Glow(0));
            });

            button.setOnMouseReleased(actionEvent -> {
                goOn();
            });
        }

        else {
            final ProgressIndicator pi = new ProgressIndicator(- 1.0f);
            pi.setStyle(" -fx-progress-color: darkgreen;");
            pi.setMinWidth(70);
            pi.setMinHeight(70);
            VBox vbox = new VBox(pi);
            vbox.setAlignment(Pos.CENTER);
            borderPane.setBottom(vbox);
        }
        int index = 1;
        for(int player : Database.getMatchPlayers().keySet()){
            StackPane pane = new StackPane();
            Label text = new Label();
            text.getStylesheets().add("/css_files/waitingForPlayers.css");
            text.getStyleClass().add("label");

            ImageView image = new ImageView(new Image("images/" + Database.getMatchColors().get(player) + "_label.png",546,70,false,true));
            text.setText(Database.getMatchPlayers().get(player));
            pane.getChildren().add(image);
            pane.getChildren().add(text);
            gridPane.add(pane,0,index);
            index += 2;
        }

    }


    @Override
    public void showPane(){
        Platform.runLater(() -> Controller.replaceSceneContent("fxml_files/waitForPlayers.fxml"));
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
            updateView();
            this.showPane();
            if(Database.getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND) {
                Database.setMatchCards(new ArrayList<>(message.getMatchCards()));
                goOn();
            }
            new Thread(()->{
                Database.getNetworkHandler().removeObserver(this);
                this.removeObserver(Database.getNetworkHandler());
            }).start();
        }

    }

    void goOn(){
        new Thread(()->{
            Database.getNetworkHandler().removeObserver(this);
            this.removeObserver(Database.getNetworkHandler());
        }).start();
        updateView();
        Database.getCurrentState().showPane();
    }


}
