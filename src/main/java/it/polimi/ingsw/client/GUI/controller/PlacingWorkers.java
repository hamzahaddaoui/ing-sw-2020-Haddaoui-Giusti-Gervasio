package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.client.GUI.View;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlacingWorkers extends State{

    @FXML
    GridPane gridPane;
    @FXML
    GridPane playerGrid;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Label desc;
    @FXML
    Label user;

    @FXML
    ImageView god;

    @FXML ImageView userPane;

    boolean finished = false;

    @Override
    public void showPane(){
        Platform.runLater(() -> Controller.replaceSceneContent("fxml_files/placingWorkers.fxml"));
    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setInitializedPositions(Database.getInitializedPositions());
        notify(message);
    }

    @Override
    public void update(MessageEvent message){
        if (message.getError()){
            System.out.println(message);
            showError();
        }
        else if (message.isFinished()){
            if (message.getPlayerState() == PlayerState.LOST){
                System.out.println("LOSER");
                lost();
                return;
            }
            Database.setCurrentState(new userDisconnected());
            Database.getCurrentState().showPane();
        }
        else {
            Database.updateStandardData(message);
            Database.setBillboardStatus(message.getBillboardStatus());
            if(Database.getMatchState() == MatchState.PLACING_WORKERS){
                Database.setPlacingAvailableCells(message.getAvailablePlacingCells());
            }
            else if (Database.getMatchState() == MatchState.RUNNING && Database.getPlayerState() == PlayerState.ACTIVE){
                Database.setTerminateTurnAvailable(message.getTerminateTurnAvailable());
                Database.setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
                Database.setWorkersAvailableCells(message.getWorkersAvailableCells());
            }
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
        userPane.setImage(new Image("images/user_" + Database.getMatchColors().get(Database.getPlayerID()) + ".png", 150, 75, false, true));

        int index = 4 - Database.getMatchPlayers().size();
        for(int player : Database.getMatchPlayers().keySet()){
            StackPane pane = new StackPane();
            Label text = new Label();
            text.getStylesheets().add("/css_files/placingWorkers.css");
            text.getStyleClass().add("label");
            ImageView image = new ImageView(new Image("images/" + Database.getMatchColors().get(player) + "_label.png",360,70,false,true));
            text.setText(Database.getMatchPlayers().get(player));
            pane.getChildren().add(image);
            pane.getChildren().add(text);
            playerGrid.add(pane,0,index);
            index += 2;
        }
        user.getStylesheets().add("/css_files/placingWorkers.css");
        user.getStyleClass().add("player");
        user.setText(Database.getNickname());

        god.setImage(new Image("images/gods_no_back/" + Database.getGodCard() + ".png",120,140,false,true));

        Database.getBillboardStatus().keySet().stream().filter(position -> Database.getBillboardStatus().get(position).getPlayerID() != 0).forEach(position -> {
            ImageView worker = new ImageView(new Image("images/" + Database.getMatchColors().get(Database.getBillboardStatus().get(position).getPlayerID()) + ".png" ,38,38, false,true));
            gridPane.add(worker, position.getY(), position.getX());
            GridPane.setHalignment(worker, HPos.CENTER); // To align horizontally in the cell
            GridPane.setValignment(worker, VPos.CENTER);
        });


        gridPane.getChildren().forEach(node -> node.setMouseTransparent(true));

        if (Database.getPlayerState() == PlayerState.ACTIVE){
            System.out.println("my turn");

            //label.getStylesheets().add("/css_files/placingWorkers.css");
            //label.getStyleClass().add("label");

            desc.setText("Place your workers");

            gridPane.getChildren().stream()
                    .filter(node -> Database.getPlacingAvailableCells().contains(new Position(GridPane.getRowIndex(node), GridPane.getColumnIndex(node))))
                    .forEach(node -> node.setMouseTransparent(false));

            gridPane.getChildren().forEach(node -> node.setOnMouseEntered(e->{
                if (finished)
                    return;
                ImageView circle = new ImageView(new Image("images/" + Database.getMatchColors().get(Database.getPlayerID()) + "_circle.png" ,38,38, false,true));
                circle.setMouseTransparent(true);
                circle.setId("circle");
                gridPane.add(circle, GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
                GridPane.setHalignment(circle, HPos.CENTER); // To align horizontally in the cell
                GridPane.setValignment(circle, VPos.CENTER);
            }));

            gridPane.getChildren().forEach(node -> node.setOnMouseExited(e->{
                gridPane.getChildren().remove(gridPane.lookup("#circle"));;
            }));
        }
        else{
            desc.setText(Database.getMatchPlayers().get(Database.getCurrentPlayer()) + "'s turn to place the workers");
        }
    }



    @FXML
    private void chooseCell(MouseEvent event) {
        if (finished)
            return;
        Node source = (Node) event.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        //source.setOpacity(1);
        ImageView worker = new ImageView(new Image("images/" + Database.getMatchColors().get(Database.getPlayerID()) + ".png" ,38,38, false,true));
        worker.setCursor(Cursor.DEFAULT);
        gridPane.add(worker, colIndex, rowIndex);
        GridPane.setHalignment(worker, HPos.CENTER); // To align horizontally in the cell
        GridPane.setValignment(worker, VPos.CENTER);

        source.setMouseTransparent(true);
        Database.addInitializedPosition(new Position(rowIndex, colIndex));
        System.out.printf("Mouse clicked cell in [%d, %d]%n", rowIndex, colIndex);
        System.out.println(Database.getInitializedPositions());
        if (Database.getInitializedPositions().size() == 1 && Database.getGodCard().equals("Eros")) {

        }

        if (Database.getInitializedPositions().size() == 2){
            finished = true;
            System.out.println("Sending data");
            sendData();
        }
    }

    public void lost(){
        Parent page;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Controller.class.getClassLoader().getResource("fxml_files/loser.fxml")); //potrebbe dare problemi, ma non credo
        try {
            page = fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Failed to load fxml file.");
            exception.printStackTrace();
            return;
        }
        Platform.runLater(() -> {
            anchorPane.getChildren().add(page);
            page.toFront();
            Database.getStage().show();
        });
    }



}



