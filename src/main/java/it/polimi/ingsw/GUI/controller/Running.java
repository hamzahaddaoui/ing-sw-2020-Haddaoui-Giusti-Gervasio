package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.Controller;
import it.polimi.ingsw.GUI.IslandLoader;
import it.polimi.ingsw.utilities.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GUI.Database.*;


public class Running extends State{
    @FXML
    StackPane stackPane;
    @FXML
    BorderPane borderPane;
    @FXML
    Label user;
    @FXML
    Label desc;
    @FXML
    ImageView god;

    @FXML ImageView specialFunction;
    @FXML ImageView function;

    static private Map<Position, Cell> billboardStatus = new HashMap<>();

    boolean confirmedStartPosition;
    boolean moved;
    boolean built;

    boolean sAvailable;
    boolean sFunction;

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

        billboardStatus = getBillboardStatus();

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

        for (Position position : billboardStatus.keySet()){
            if (billboardStatus.get(position).getPlayerID() != 0){
                getIslandLoader().putWorker(positionToPoint(position), getMatchColors().get(billboardStatus.get(position).getPlayerID()));
            }
        }
    }

    @Override
    public void showError(){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Server error");
            alert.setContentText("Error from the server");
            alert.showAndWait();
        });

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

        updateBillboard();

        System.out.println("PlayerState: "+ getPlayerState());
        Platform.runLater(()  -> {
            getIslandLoader().showCells(null);
            if (getPlayerState() == PlayerState.ACTIVE){

                specialFunctionHandler();

                switch (getTurnState()){
                    case MOVE:
                        moved = false;

                        if (!confirmedStartPosition)
                            desc.setText("SELECT A WORKER");
                        else {
                            updateLightenedCells();
                            desc.setText("SELECT the cell where you want to move");
                        }
                        break;
                    case BUILD:
                        built = false;
                        getIslandLoader().showCells(getWorkersAvailableCells().get(getStartingPosition()));
                        getIslandLoader().showArrow(getMatchColors().get(getPlayerID()), positionToPoint(getStartingPosition()));
                        desc.setText("SELECT the cell where you want to build");
                }
            }
            else{
                desc.setText(getMatchPlayers().get(getCurrentPlayer()) + " is making its move");
                setStartingPosition(null);
                setEndPosition(null);
                confirmedStartPosition = false;
                sFunction = false;
            }
        });
    }


    public boolean workerClick(Point2D point){
        Position position = pointToPosition(point);
        System.out.println("worker clicked + "+position);
        System.out.println(getWorkersAvailableCells());
        if (getPlayerState() == PlayerState.IDLE) {
            System.out.println("PLAYER NOT ACTIVE");
            return false;
        }
        else if (getWorkersAvailableCells().containsKey(position) && !confirmedStartPosition){
            System.out.println("PLAYER ACTIVE. SETTED POSITION");
            Platform.runLater(()  -> desc.setText("SELECT the cell where you want to move"));
            setStartingPosition(position);

            updateLightenedCells();
            specialFunctionHandler();

            return true;
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
        confirmedStartPosition = true;
        if (getTurnState() == TurnState.MOVE && !moved){
            if (getWorkersAvailableCells().get(getStartingPosition()).contains(position)){
                moved = true;
                System.out.println("MOVE OK. SENDING movement...");
                getIslandLoader().hideArrow();
                getIslandLoader().showCells(null);


                if (billboardStatus.get(position).getPlayerID() == 0) {

                    getIslandLoader().moveWorker(positionToPoint(getStartingPosition()), point);
                    billboardStatus.get(position).setPlayerID(getPlayerID());
                    billboardStatus.get(getStartingPosition()).setPlayerID(0);
                }


                setEndPosition(position);
                sendData();
                setStartingPosition(position);
            }
        }
        else if (getTurnState() == TurnState.BUILD) {

            if (getWorkersAvailableCells().get(getStartingPosition()).contains(position) && !built) {
                System.out.println("BUILD OK. SENDING build...");
                built = true;
                setEndPosition(position);
                getIslandLoader().showCells(null);
                getIslandLoader().hideArrow();



                //PER ATLAS -> VERIFICARE SPECIAL FUNCTION, E MODIFICARE BIT DI DOME

                getIslandLoader().build(point, false);
                int towerHeight = billboardStatus.get(position).getTowerHeight();
                if (towerHeight < 3)
                    billboardStatus.get(position).setTowerHeight(towerHeight + 1);
                else
                    billboardStatus.get(position).setDome(true);



                sendData();
            }
        }
    }


    public void updateBillboard(){
        Map<Integer, Position> movedInPlayers = new HashMap<>();
        Map<Integer, Position> movedOutPlayers =  new HashMap<>();
        Map<Point2D, Point2D> playersMove = new HashMap<>();


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
                    else {
                        movedOutPlayers.put(billboardStatus.get(position).getPlayerID(), position);
                        if (getBillboardStatus().get(position).getPlayerID() != 0)
                            movedInPlayers.put(getBillboardStatus().get(position).getPlayerID(), position);
                    }
                }
            }

            if (movedOutPlayers.size() != 0){
                System.out.println("Moved in players: ");
                movedInPlayers.keySet().forEach(player -> System.out.println("Player "+player +" - Position "+movedInPlayers.get(player)));
                System.out.println("Moved out players: ");

                movedOutPlayers.keySet().forEach(player -> System.out.println("Player "+player +" - Position "+movedOutPlayers.get(player)));

                movedOutPlayers.keySet().forEach(ID -> playersMove.put(positionToPoint(movedOutPlayers.get(ID)), positionToPoint(movedInPlayers.get(ID))));

                if(playersMove.size() == 1)
                    playersMove.keySet().forEach(startPosition -> getIslandLoader().moveWorker(startPosition, playersMove.get(startPosition)));
                else{
                    Iterator iterator = playersMove.keySet().iterator();
                    Point2D startPos1 = (Point2D) iterator.next();
                    Point2D startPos2 = (Point2D) iterator.next();
                    getIslandLoader().swapWorkers(startPos1, startPos2, playersMove.get(startPos1), playersMove.get(startPos2));
                }

                /*if (movedOutPlayers.containsKey(getPlayerID()) && movedOutPlayers.get(getPlayerID()) == getStartingPosition()) {
                    setStartingPosition(movedInPlayers.get(getPlayerID()));
                    System.out.println("CHANGED STARTING POSITION");
                }*/

            }

        }

        billboardStatus = getBillboardStatus();
    }

    public static Position pointToPosition(Point2D point){
        return new Position((int) point.getX(), (int) point.getY());
    }

    public static Point2D positionToPoint(Position position){
        return new Point2D(position.getX(), position.getY());
    }


    public void specialFunctionHandler(){
        System.out.println(getSpecialFunctionAvailable());
        System.out.println(isTerminateTurnAvailable());

        if ((getGodCard().equals("Demeter") || getGodCard().equals("Hephaestus")) && isTerminateTurnAvailable()){
            specialFunction.setVisible(true);
            function.setVisible(true);

            Platform.runLater(() -> specialFunction.setImage(new Image("images/specialpow/false.png",150,75,false,true)));
                Platform.runLater(() -> function.setImage(new Image("images/specialpow/"+getGodCard()+".png",60,47,false,true)));

            function.setOnMouseClicked(mouseOver -> function.setCursor(Cursor.HAND));

            function.setOnMouseClicked(mouseEvent -> {
                System.out.println("END TURN ACTIVATED");
                Platform.runLater(() -> {
                    specialFunction.setImage(new Image("images/specialpow/true.png", 150, 75, false, true));
                    function.translateXProperty().set(72);
                });
                MessageEvent message = new MessageEvent();
                message.setEndTurn(true);
                notify(message);
                specialFunction.setVisible(false);
                function.setVisible(false);
                getIslandLoader().hideArrow();
                getIslandLoader().showCells(null);
                function.translateXProperty().set(-72);


            });
        }


        else if (confirmedStartPosition && getSpecialFunctionAvailable() !=null && getSpecialFunctionAvailable().containsKey(getStartingPosition()) && getSpecialFunctionAvailable().get(getStartingPosition())){
            specialFunction.setVisible(true);
            function.setVisible(true);

            Platform.runLater(() -> specialFunction.setImage(new Image("images/specialpow/false.png",150,75,false,true)));
            Platform.runLater(() -> function.setImage(new Image("images/specialpow/"+getGodCard()+".png",60,47,false,true)));
            Platform.runLater(() -> function.translateXProperty().set(0));
            sFunction = false;

            function.setOnMouseClicked(mouseOver -> function.setCursor(Cursor.HAND));

            function.setOnMouseClicked(mouseEvent -> {
                sFunction ^= true;
                System.out.println("SPECIAL FUNCTION"+  sFunction);
                Platform.runLater(() -> {
                    if (sFunction) {
                        specialFunction.setImage(new Image("images/specialpow/true.png", 150, 75, false, true));
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(function.xProperty(), 72)));
                        timeline.play();

                    } else {
                        specialFunction.setImage(new Image("images/specialpow/false.png", 150, 75, false, true));
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(function.xProperty(), 0)));
                        timeline.play();
                    }
                });

                MessageEvent message = new MessageEvent();
                message.setSpecialFunction(sFunction);
                notify(message);
            });
        }
        else{
            specialFunction.setVisible(false);
            function.setVisible(false);
        }
    }

    public void updateLightenedCells(){
        getIslandLoader().hideArrow();
        getIslandLoader().showArrow(getMatchColors().get(billboardStatus.get(getStartingPosition()).getPlayerID()), positionToPoint(getStartingPosition()));
        getIslandLoader().showCells(getWorkersAvailableCells().get(getStartingPosition()));
    }


}
