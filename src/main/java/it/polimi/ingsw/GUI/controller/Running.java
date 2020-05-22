package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.Controller;
import it.polimi.ingsw.GUI.Database;
import it.polimi.ingsw.GUI.IslandLoader;
import it.polimi.ingsw.utilities.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GUI.Database.*;
import static it.polimi.ingsw.GUI.Database.getGodCard;



public class Running extends State{
    @FXML StackPane stackPane;
    @FXML BorderPane borderPane;
    @FXML Label user;
    @FXML Label desc;
    @FXML ImageView god;

    static private Map<Position, Cell> billboardStatus = new HashMap<>();

    @Override
    public void showPane(){
        Platform.runLater(() -> {
            Controller.replaceSceneContent("fxml_files/running.fxml");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(getNetworkHandler());
        getNetworkHandler().addObserver(this);
        user.getStylesheets().add("/css_files/placingWorkers.css");
        user.getStyleClass().add("player");
        user.setText(getNickname());
        god.setImage(new Image("images/gods_no_back/" + getGodCard() + ".png",120,140,false,true));


        if (getPlayerState() == PlayerState.ACTIVE){
            desc.setText("SELECT A WORKER");
        }
        else{
            desc.setText(getMatchPlayers().get(getCurrentPlayer()) + " is making its move");
        }

        setIslandLoader(new IslandLoader());
        try {
            IslandLoader.start(stackPane);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        billboardStatus = getBillboardStatus();

        for (Position position : billboardStatus.keySet()){
            if (billboardStatus.get(position).getPlayerID() != 0){
                getIslandLoader().putWorker(positionToPoint(position), getMatchColors().get(billboardStatus.get(position).getPlayerID()));
            }
        }
    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setStartPosition(getStartingPosition());
        message.setEndPosition(getEndPosition());
        notify(message);
    }

    @Override
    public void update(MessageEvent message){
        System.out.println("Message received: "+ message);
        if (message.getError()){
            System.out.println(message);
            showError();
        }
        else if (message.isFinished()){
            if (message.getWinner() != 0){
                //vincitore!!!
            }
            else if (message.getPlayerState() == PlayerState.LOST){
                //perdente!!!
            }
            else{
                //disconnessione di qualcuno <.<
                setCurrentState(new userDisconnected());
                getCurrentState().showPane();
            }
        }

        updateStandardData(message);
        setTerminateTurnAvailable(message.getTerminateTurnAvailable());
        setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
        setWorkersAvailableCells(message.getWorkersAvailableCells());
        setBillboardStatus(message.getBillboardStatus());

        System.out.println("PlayerState: "+ getPlayerState());
        Platform.runLater(()  -> {
            if (getPlayerState() == PlayerState.ACTIVE){
                switch (getTurnState()){
                    case MOVE:
                        if (getStartingPosition() == null)
                            desc.setText("SELECT A WORKER");
                        else
                            desc.setText("SELECT the cell where you want to move");
                        break;
                    case BUILD:
                        desc.setText("SELECT the cell where you want to build");
                }
            }
            else{
                desc.setText(getMatchPlayers().get(getCurrentPlayer()) + " is making its move");
                setStartingPosition(null);
                setEndPosition(null);
            }
        });
        updateBillboard();

    }


    public boolean workerClick(Point2D point){
        Position position = new Position((int) point.getX(), (int) point.getY());
        System.out.println("worker clicked + "+position);
        System.out.println(getWorkersAvailableCells());
        if (getPlayerState() == PlayerState.IDLE) {
            System.out.println("PLAYER NOT ACTIVE");
            return false;
        }
        else if (getWorkersAvailableCells().containsKey(position)){
            System.out.println("PLAYER ACTIVE. SETTED POSITION");
            setStartingPosition(position);
            return true;
            //lIGHTNING / LIGHT SU WORKER?
            //illuminare le celle dove può muoversi
        }
        return false;
    }


    public void boardClick(Point2D point){
        Position position = pointToPosition(point);
        System.out.println("Click on board/building detected on "+ position);
        if (getPlayerState() == PlayerState.IDLE || getStartingPosition() == null) {
            System.out.println("IDLE OR NOT SELECTED ANY WORKER");
            return;
        }

        if (getTurnState() == TurnState.MOVE){
            if (getWorkersAvailableCells().get(getStartingPosition()).contains(position)){
                System.out.println("MOVE OK. SENDING movement...");
                //getIslandLoader().moveWorker(positionToPoint(getStartingPosition()), point);
                setEndPosition(position);
                sendData();
                setStartingPosition(position);
            }
        }
        else if (getTurnState() == TurnState.BUILD) {

            if (getWorkersAvailableCells().get(getStartingPosition()).contains(position)) {
                System.out.println("BUILD OK. SENDING build...");
                setEndPosition(position);
                //getIslandLoader().build(point, false);
                sendData();
            }
        }
    }


    public void updateBillboard(){
        Map<Integer, Position> movedInPlayers = new HashMap<>();
        Map<Integer, Position> movedOutPlayers =  new HashMap<>();
        Map<Point2D, Point2D> playersMove = new HashMap<>();


        System.out.println(billboardStatus);

        System.out.println(getBillboardStatus());

        if (billboardStatus != getBillboardStatus()){
            System.out.println("Different billboard");

            Set<Position> changedPositions =  getBillboardStatus()
                    .keySet()
                    .stream()
                    .filter(position -> !getBillboardStatus().get(position).equals(billboardStatus.get(position)))
                    .collect(Collectors.toSet());


            for (Position position : changedPositions){
                if (getBillboardStatus().get(position).getTowerHeight() != billboardStatus.get(position).getTowerHeight()){
                    System.out.println("Different height in "+position);
                    for(int i=0; i<(getBillboardStatus().get(position).getTowerHeight() - billboardStatus.get(position).getTowerHeight()); i++){
                        getIslandLoader().build(positionToPoint(position), false);
                    }
                }
                if (getBillboardStatus().get(position).isDome() != billboardStatus.get(position).isDome()){
                    System.out.println("Different dome in "+position);
                    getIslandLoader().build(positionToPoint(position), true);
                }
                if (getBillboardStatus().get(position).getPlayerID() != billboardStatus.get(position).getPlayerID()){
                    //positionPlayers.put(Map.of(billboardStatus.get(position).getPlayerID(), getBillboardStatus().get(position).getPlayerID()), position);
                    System.out.println("Different player in "+position);
                    if (billboardStatus.get(position).getPlayerID() == 0) //se la cella era vuota, ed ora è piena
                        movedInPlayers.put(getBillboardStatus().get(position).getPlayerID(), position);
                    else
                        movedOutPlayers.put(billboardStatus.get(position).getPlayerID(), position);
                }
            }

            if (movedOutPlayers.size() != 0){
                System.out.println("Moved in players: ");
                movedInPlayers.keySet().forEach(player -> System.out.println("Player "+player +" - Position "+movedInPlayers.get(player)));
                System.out.println("\nMoved out players: ");

                movedOutPlayers.keySet().forEach(player -> System.out.println("Player "+player +" - Position "+movedOutPlayers.get(player)));

                movedOutPlayers.keySet().forEach(ID -> playersMove.put(positionToPoint(movedOutPlayers.get(ID)), positionToPoint(movedInPlayers.get(ID))));
                playersMove.keySet().forEach(startPosition -> getIslandLoader().moveWorker(startPosition, playersMove.get(startPosition)));
            }

        }

        billboardStatus = getBillboardStatus();
    }

    public Position pointToPosition(Point2D point){
        return new Position((int) point.getX(), (int) point.getY());
    }

    public Point2D positionToPoint(Position position){
        return new Point2D(position.getX(), position.getY());
    }


}
