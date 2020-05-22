package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.Database;
import it.polimi.ingsw.GUI.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static it.polimi.ingsw.GUI.Controller.replaceSceneContent;
import static it.polimi.ingsw.GUI.Database.*;

public class SelectingSpecialCommand extends State {

    @FXML
    BorderPane borderPane;
    @FXML
    StackPane spane;
    @FXML
    Label title;


    Set<Button> buttonSet ;

    boolean selectedCard = false;

    @Override
    public void showPane(){
        Platform.runLater(() -> replaceSceneContent("fxml_files/selectingSpecialCommand.fxml"));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        buttonSet = new HashSet<>();
        this.addObserver(getNetworkHandler());
        getNetworkHandler().addObserver(this);

        GridPane gridPane = new GridPane();
        spane.getChildren().add(gridPane);
        StackPane.setAlignment(gridPane, Pos.CENTER);

        gridPane.setLayoutX(spane.getLayoutX());
        gridPane.setLayoutY(spane.getLayoutY());
        gridPane.setAlignment(Pos.CENTER);

        for(String card: Database.getMatchCards()){
            gridPane.getRowConstraints().add(new RowConstraints(180));
            String string = "\"/images/gods_no_back/" + card + ".png\"";
            ImageView img = new ImageView(new Image("images/gods_no_back/" + card + ".png",150,200,false,true));
            Button button = new Button();
            //button.getStyleClass().add("image");
            buttonSet.add(button);
            button.setId(card);
            button.getStylesheets().add("/css_files/selectingSpecialCommand.css");
            button.getStyleClass().add("button");
            button.setStyle("-fx-background-image: url("+string+");\n");

            button.setOnMouseEntered(e->{
                Timeline timeline1 = new Timeline(
                        new KeyFrame(Duration.seconds(0.2), new KeyValue(button.scaleXProperty(), 1.4)),
                        new KeyFrame(Duration.seconds(0.2), new KeyValue(button.scaleYProperty(), 1.4)),
                        new KeyFrame(Duration.seconds(0.2), new KeyValue(button.scaleZProperty(), 1.4))
                );

                if (buttonSet.size()>1) {
                    buttonSet.stream().filter(button1 -> button1 != button).forEach(button1 -> {
                        Timeline timeline2 = new Timeline(
                                new KeyFrame(Duration.seconds(0.2), new KeyValue(button1.scaleXProperty(), 0.6)),
                                new KeyFrame(Duration.seconds(0.2), new KeyValue(button1.scaleYProperty(), 0.6)),
                                new KeyFrame(Duration.seconds(0.2), new KeyValue(button1.scaleZProperty(), 0.6))
                        );

                        ParallelTransition sequence = new ParallelTransition(timeline1, timeline2);
                        sequence.play();
                    });
                }
                else{
                    timeline1.play();
                }
                StackPane pane = new StackPane();
                ImageView desc = new ImageView(new Image("images/god_desc/"+button.getId()+".png" ,872,497, false,true));
                pane.getChildren().add(desc);
                pane.setMinSize(839,626);
                pane.setMaxSize(839,626);
                pane.setAlignment(desc, Pos.CENTER);
                borderPane.setRight(pane);
            });

            button.setOnMouseExited(e -> {
                borderPane.setRight(new ImageView());
                //text.setText("");
                buttonSet.stream().forEach(button1 -> {
                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.seconds(0.2), new KeyValue(button1.scaleXProperty(), 1)),
                            new KeyFrame(Duration.seconds(0.2), new KeyValue(button1.scaleYProperty(), 1)),
                            new KeyFrame(Duration.seconds(0.2), new KeyValue(button1.scaleZProperty(), 1))
                    );
                    timeline.play();
                });
            });

            gridPane.add(button,0, Database.getMatchCards().indexOf(card));

            gridPane.setHalignment(button, HPos.CENTER); // To align horizontally in the cell
            gridPane.setValignment(button, VPos.CENTER);
        }

        if(getPlayerState() != PlayerState.ACTIVE){
            title.getStylesheets().add("/css_files/SelectingGodCards.css");
            title.getStyleClass().add("label_title");
            title.setText(getMatchPlayers().get(getCurrentPlayer()) + " is selecting his card...");
            VBox vbox = new VBox(title);
            vbox.setAlignment(Pos.CENTER);
            borderPane.setTop(vbox);
        }
        else{
            title.getStylesheets().add("/css_files/SelectingGodCards.css");
            title.getStyleClass().add("label_title");
            title.setText("Choose your special power card");
            VBox vbox = new VBox(title);
            vbox.setAlignment(Pos.CENTER);
            borderPane.setTop(vbox);
            buttonSet.forEach(button -> {
                button.setOnAction(this::selectedCard);
                button.setCursor(Cursor.HAND);
            });
        }
    }

    @Override
    public void showError(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Unexpected error from the server");
            alert.showAndWait();
        });
    }

    @Override
    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setGodCard(getGodCard());
        notify(message);
    }

    @Override
    public void update(MessageEvent message){
        if (message.getError()){
            System.out.println(message);
            showError();
        }
        else if (message.isFinished()){
            setCurrentState(new userDisconnected());
            getCurrentState().showPane();
        }
        else {
            updateStandardData(message);
            if (getMatchState() == MatchState.SELECTING_SPECIAL_COMMAND)
                setMatchCards(new ArrayList<>(message.getMatchCards()));
            else {
                setPlacingAvailableCells(message.getAvailablePlacingCells());
                setBillboardStatus(message.getBillboardStatus());
            }
            View.updateView();
            getCurrentState().showPane();
            new Thread(()->{
                getNetworkHandler().removeObserver(this);
                this.removeObserver(getNetworkHandler());
            }).start();
        }
    }


    public void selectedCard(ActionEvent event){
        if (selectedCard)
            return;

        String card = ((Node) event.getSource()).getId();
        setGodCard(card);
        sendData();
        selectedCard = true;
        System.out.println("selected card: "+ card);
    }


}