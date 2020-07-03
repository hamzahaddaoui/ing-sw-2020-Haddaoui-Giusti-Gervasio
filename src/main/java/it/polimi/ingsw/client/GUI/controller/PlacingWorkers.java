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
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static it.polimi.ingsw.client.GUI.Database.*;

/**
 * @author: hamzahaddaoui
 *
 * Controller/View class. Manage the placing worker state, the GUI initialization, as well as the control of the buttons/click/mouse hover
 * */

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
        message.setInitializedPositions(getInitializedPositions());
        notify(message);
    }

    @Override
    public void update(MessageEvent message){
        if (message.getError()){
            System.out.println(message);
            showError();
        }
        if (message.getPlayerState() == PlayerState.LOST){
            System.out.println("LOSER");
            lost();
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

    /**
     * Initialization of the GUI; A map is shown, on which the user can select the cell where to put his worker
     * On the bottom, information about the match is shown, as well as the users (on the left)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(getNetworkHandler());
        getNetworkHandler().addObserver(this);
        userPane.setImage(new Image("images/user_" + getMatchColors().get(getPlayerID()) + ".png", 150, 75, false, true));

        int index = 4 - getMatchPlayers().size();
        for(int player : getMatchPlayers().keySet()){
            StackPane pane = new StackPane();
            Label text = new Label();
            text.getStylesheets().add("/css_files/placingWorkers.css");
            text.getStyleClass().add("label");
            ImageView image = new ImageView(new Image("images/" + getMatchColors().get(player) + "_label.png",360,70,false,true));
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
            ImageView worker = new ImageView(new Image("images/" + getMatchColors().get(getBillboardStatus().get(position).getPlayerID()) + ".png" ,38,38, false,true));
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
                if (finished)
                    return;
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);
                if (!getPlacingAvailableCells().contains(new Position(rowIndex, colIndex)))
                        return;


                ImageView circle = new ImageView(new Image("images/" + getMatchColors().get(getPlayerID()) + "_circle.png" ,38,38, false,true));
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
            desc.setText(getMatchPlayers().get(getCurrentPlayer()) + "'s turn to place the workers");
        }
    }


    /**
     * Selecting the cell, adding it to the initializedPositions set.
     * If the selected cells number is two, then these cells are sent to the server.
     */
    @FXML
    private void chooseCell(MouseEvent event) {
        if (finished)
            return;



        Node source = (Node) event.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        Position pos = new Position(rowIndex, colIndex);

        if (!getPlacingAvailableCells().contains(new Position(rowIndex, colIndex)))
            return;

        ImageView worker = new ImageView(new Image("images/" + getMatchColors().get(getPlayerID()) + ".png" ,38,38, false,true));
        worker.setCursor(Cursor.DEFAULT);
        gridPane.add(worker, colIndex, rowIndex);
        GridPane.setHalignment(worker, HPos.CENTER); // To align horizontally in the cell
        GridPane.setValignment(worker, VPos.CENTER);

        source.setMouseTransparent(true);

        addInitializedPosition(pos);
        System.out.printf("Mouse clicked cell in [%d, %d]%n", rowIndex, colIndex);
        System.out.println(getInitializedPositions());
        if (getInitializedPositions().size() == 1 && getGodCard().equals("Eros")) {
            Set<Position> positionSet = new HashSet<>();
            if(pos.getX() == 0 || pos.getX() == 4){
                for (int i = 0; i < 5; i++) positionSet.add(new Position((pos.getX()+4)%8, i));
            }
            if(pos.getY() == 0 || pos.getY() == 4){
                for (int i = 0; i < 5; i++) positionSet.add(new Position(i, (pos.getY()+4)%8));
            }

            setPlacingAvailableCells(positionSet);
        }



        if (getInitializedPositions().size() == 2){
            finished = true;
            System.out.println("Sending data");
            sendData();
        }
    }

    /**
     * If the user loses during workers placing.
     * Rare case! A has lost.
     *      A A B o o
     *      C C B o o
     *      o o o o o
     *      o o o o o
     */
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
            getStage().show();
        });
    }



}



