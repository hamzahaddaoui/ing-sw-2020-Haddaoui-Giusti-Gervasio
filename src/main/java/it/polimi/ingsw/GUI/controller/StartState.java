package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.View;
import it.polimi.ingsw.utilities.MessageEvent;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.GUI.Controller.replaceSceneContent;
import static it.polimi.ingsw.GUI.Controller.setServer;
import static it.polimi.ingsw.GUI.Database.*;
import static javafx.scene.control.Alert.AlertType;

public class StartState extends State {


    @FXML
    private ImageView santorini;

    @FXML
    private TextField nickname;
    @FXML
    private TextField address;

    @FXML
    private BorderPane startScreen;

    Node dataBox;

    Popup popupIP, popupNick;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        ScaleTransition rt = new ScaleTransition(Duration.seconds(0.0005), santorini);
        rt.setCycleCount(1);
        rt.setToX(0);
        rt.setToY(0);
        rt.play();

        ScaleTransition rt2 = new ScaleTransition(Duration.seconds(5), santorini);
        rt2.setCycleCount(1);
        rt2.setToX(1.15);
        rt2.setToY(1.15);

        rt2.play();

        ScaleTransition rt3 = new ScaleTransition(Duration.seconds(5), santorini);
        rt3.setCycleCount(1);
        rt3.setToX(0.85);
        rt3.setToY(0.85);

        rt2.setOnFinished((e) -> rt3.play());
        rt3.setOnFinished((e) -> rt2.play());
    }

    @Override
    public void showPane() {

        Platform.runLater(() -> replaceSceneContent("fxml_files/startScreen.fxml"));
    }

    @Override
    public void showError(){
        Platform.runLater(() -> {
            startScreen.setCenter(dataBox);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Nickname already taken");
            alert.setContentText("Please insert a different nickname");
            alert.showAndWait();
        });
    }

    public void playButton(MouseEvent mouseEvent){
        play();
    }

    public void enterPressed(ActionEvent keyEvent){
        play();
    }


    public void play(){
        setIP(address.getCharacters().toString());
        setNickname(nickname.getCharacters().toString());
        System.out.println(getIP()+"  "+getNickname());

        if(getIP().equals("") ){
            popupIP = new Popup();
            Label label = new Label();
            popupIP.getContent().add(label);
            label.setOnMouseReleased(e -> popupIP.hide());
            label.getStylesheets().add("/css_files/startScreen.css");
            label.getStyleClass().add("popup");
            popupIP.setHideOnEscape(true);
            label.setText("Insert Server IP");
            popupIP.setAutoHide(true);
            popupIP.setOnShown(e -> {
                popupIP.setX(getStage().getX() + getStage().getWidth() / 2 - popupIP.getWidth() / 2);
                popupIP.setY(address.localToScene(0, 0).getY() + 63);

            });
            popupIP.show(getStage());
            return;
        }

        if (getNickname().equals("")){
            popupNick = new Popup();
            Label label = new Label();
            label.setOnMouseReleased(e -> popupNick.hide());
            label.getStylesheets().add("/css_files/startScreen.css");
            label.getStyleClass().add("popup");
            popupNick.getContent().add(label);
            popupNick.setAutoHide(true);
            popupNick.setHideOnEscape(true);
            label.setText("Insert your nickname");
            popupNick.setOnShown(e -> {
                popupNick.setX(getStage().getX() + getStage().getWidth()/2 - popupNick.getWidth()/2);
                popupNick.setY(nickname.localToScene(0,0).getY()+63 );
            });
            popupNick.show(getStage());
            return;
        }

        if (getNetworkHandler() == null){
            final ProgressIndicator pi = new ProgressIndicator(-1.0f);
            pi.setStyle(" -fx-progress-color: dodgerblue;");
            pi.setMinWidth(130);
            pi.setMinHeight(130);
            final HBox hb = new HBox(pi);
            final VBox vb = new VBox(hb);

            hb.setAlignment(Pos.CENTER);
            vb.setAlignment(Pos.CENTER);
            dataBox = startScreen.getCenter();
            startScreen.setCenter(pi);

            new Thread(() -> {
                try {
                    setServer();
                    this.addObserver(getNetworkHandler());
                    getNetworkHandler().addObserver(this);
                    sendData();
                } catch (IOException exception) {
                    Platform.runLater(() -> {
                        startScreen.setCenter(dataBox);
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Connection error");
                        alert.setHeaderText("No server found");
                        alert.setContentText("Check your internet connection");
                        //alert.getDialogPane().getStylesheets().add("/css_files/dialog.css");
                        alert.showAndWait();
                    });
                }
            }).start();

        }
        else {
            sendData();
        }


    }


    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setNickname(getNickname());
        notify(message);
    }


    @Override
    public void update(MessageEvent message){
        if (message.getError()){
            System.out.println(message);
            showError();
        }
        else {
            updateStandardData(message);
            View.updateView();

            getCurrentState().showPane();
            new Thread(()->{
                getNetworkHandler().removeObserver(this);
                this.removeObserver(getNetworkHandler());
            }).start();

        }
    }
}
