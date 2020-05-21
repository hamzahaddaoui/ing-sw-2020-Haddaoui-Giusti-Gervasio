package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.View;
import it.polimi.ingsw.utilities.MatchState;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import it.polimi.ingsw.utilities.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.GUI.Controller.replaceSceneContent;
import static it.polimi.ingsw.GUI.Database.*;

public class PlacingWorkers extends State{

    @FXML GridPane gridPane;
    @FXML GridPane playerGrid;

    @FXML Label desc;
    @FXML Label user;

    @FXML ImageView god;



    @Override
    public void showPane(){
        Platform.runLater(() -> replaceSceneContent("fxml_files/placingWorkers.fxml"));
    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setInitializedPositions(getInitializedPositions());
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
            setBillboardStatus(message.getBillboardStatus());
            if(getMatchState() == MatchState.PLACING_WORKERS){
                setPlacingAvailableCells(message.getAvailablePlacingCells());
            }
            else if (getMatchState() == MatchState.RUNNING && getPlayerState() == PlayerState.ACTIVE){
                setTerminateTurnAvailable(message.getTerminateTurnAvailable());
                setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
                setWorkersAvailableCells(message.getWorkersAvailableCells());
            }
            View.updateView();
            getCurrentState().showPane();
            new Thread(()->{
                getNetworkHandler().removeObserver(this);
                this.removeObserver(getNetworkHandler());
            }).start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(getNetworkHandler());
        getNetworkHandler().addObserver(this);

        int index = 4-getMatchPlayers().size();
        for(int player : getMatchPlayers().keySet()){
            StackPane pane = new StackPane();
            Label text = new Label();
            text.getStylesheets().add("/css_files/placingWorkers.css");
            text.getStyleClass().add("label");
            ImageView image = new ImageView(new Image("images/"+getMatchColors().get(player)+"_label.png",360,70,false,true));
            text.setText(getMatchPlayers().get(player));
            pane.getChildren().add(image);
            pane.getChildren().add(text);
            playerGrid.add(pane,0,index);
            index += 2;
        }
        user.getStylesheets().add("/css_files/placingWorkers.css");
        user.getStyleClass().add("player");
        user.setText(getNickname());

        god.setImage(new Image("images/gods_no_back/" + getGodCard() + ".png",120,140,false,true));

        getBillboardStatus().keySet().stream().filter(position -> getBillboardStatus().get(position).getPlayerID() != 0).forEach(position -> {
            ImageView worker = new ImageView(new Image("images/"+getMatchColors().get(getBillboardStatus().get(position).getPlayerID())+".png" ,38,38, false,true));
            gridPane.add(worker, position.getY(), position.getX());
            GridPane.setHalignment(worker, HPos.CENTER); // To align horizontally in the cell
            GridPane.setValignment(worker, VPos.CENTER);
        });


        gridPane.getChildren().forEach(node -> node.setMouseTransparent(true));

        if (getPlayerState() == PlayerState.ACTIVE){
            System.out.println("my turn");

            //label.getStylesheets().add("/css_files/placingWorkers.css");
            //label.getStyleClass().add("label");

            desc.setText("Place your workers");

            gridPane.getChildren().stream()
                    .filter(node -> getPlacingAvailableCells().contains(new Position(GridPane.getRowIndex(node), GridPane.getColumnIndex(node))))
                    .forEach(node -> node.setMouseTransparent(false));

            gridPane.getChildren().forEach(node -> node.setOnMouseEntered(e->{
                ImageView circle = new ImageView(new Image("images/"+getMatchColors().get(getPlayerID())+"_circle.png" ,38,38, false,true));
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
            desc.setText(getMatchPlayers().get(getCurrentPlayer()) + " is placing its workers");
        }
    }



    @FXML
    private void chooseCell(MouseEvent event) {
        Node source = (Node) event.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        //source.setOpacity(1);
        ImageView worker = new ImageView(new Image("images/"+getMatchColors().get(getPlayerID())+".png" ,38,38, false,true));
        worker.setCursor(Cursor.DEFAULT);
        gridPane.add(worker, colIndex, rowIndex);
        GridPane.setHalignment(worker, HPos.CENTER); // To align horizontally in the cell
        GridPane.setValignment(worker, VPos.CENTER);

        source.setMouseTransparent(true);
        addInitializedPosition(new Position(rowIndex, colIndex));
        System.out.printf("Mouse clicked cell in [%d, %d]%n", rowIndex, colIndex);
        System.out.println(getInitializedPositions());
        if (getInitializedPositions().size() == 2){
            System.out.println("Sending data");
            sendData();
        }
    }
}
