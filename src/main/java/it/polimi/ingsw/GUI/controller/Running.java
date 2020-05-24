package it.polimi.ingsw.GUI.controller;

import it.polimi.ingsw.GUI.Controller;
import it.polimi.ingsw.GUI.IslandLoader;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.Observer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.GUI.Database.*;


public class Running extends State{
    @FXML StackPane stackPane;
    @FXML BorderPane borderPane;
    @FXML Label user;
    @FXML Label desc;
    @FXML ImageView god;
    @FXML ImageView userPane;

    @FXML Button function;

    static private Map<Position, Cell> billboardStatus = new HashMap<>();
    static private Map<Integer, String> matchPlayers = new HashMap<>();

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
        matchPlayers = getMatchPlayers();

        //inizializzo gli elementi grafici del layer superiore
        user.getStylesheets().add("/css_files/placingWorkers.css");
        userPane.setImage(new Image("images/user_"+getMatchColors().get(getPlayerID())+".png", 150, 75, false, true));
        user.getStyleClass().add("player");
        user.setText(getNickname());
        god.setImage(new Image("images/gods_no_back/" + getGodCard() + ".png",120,140,false,true));

        //inizializzo l'isola
        setIslandLoader(new IslandLoader());
        try {
            IslandLoader.start(stackPane);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //inizializzo i giocatori
        for (Position position : billboardStatus.keySet()){
            if (billboardStatus.get(position).getPlayerID() != 0){
                getIslandLoader().putWorker(positionToPoint(position), getMatchColors().get(billboardStatus.get(position).getPlayerID()));
            }
        }

        updateGame();

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
            return;
        }
        else if (message.getPlayerState() == PlayerState.LOST){
            setBillboardStatus(message.getBillboardStatus());
            updateBillboard();
            getIslandLoader().endAnimation();
            System.out.println("LOSER");
            lost();
            return;
        }
        else if (message.getPlayerState() == PlayerState.WIN){
            getIslandLoader().endAnimation();
            System.out.println("WINNER");
            win();
            return;
        }
        else if (message.isFinished()){
            //disconnessione di qualcuno <.<
            setCurrentState(new userDisconnected());
            getCurrentState().showPane();
            return;
        }



        updateStandardData(message);
        setTerminateTurnAvailable(message.getTerminateTurnAvailable());
        setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
        setWorkersAvailableCells(message.getWorkersAvailableCells());
        setBillboardStatus(message.getBillboardStatus());

        updateBillboard();

        updateGame();
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
            Platform.runLater(()  -> {
                    if (getTurnState() == TurnState.MOVE)
                        desc.setText("SELECT the cell where you want to move");
                if (getTurnState() == TurnState.BUILD)
                    desc.setText("SELECT the cell where you want to build");
            });
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



        if (getTurnState() == TurnState.MOVE && !moved){
            if (getWorkersAvailableCells().get(getStartingPosition()).contains(position)){
                confirmedStartPosition = true;
                moved = true;
                System.out.println("MOVE OK. SENDING movement...");

                resetCells();

                if (billboardStatus.get(position).getPlayerID() == 0) {
                    getIslandLoader().moveWorker(positionToPoint(getStartingPosition()), point);
                    billboardStatus.get(position).setPlayerID(getPlayerID());
                    billboardStatus.get(getStartingPosition()).setPlayerID(0);
                }
                setEndPosition(position);
                sendData();
                setStartingPosition(position);
            }
            else if(moved)
                System.out.println("Already moved");
            else
                System.out.println("position not contained in available cells");
        }
        else if (getTurnState() == TurnState.BUILD) {

            if (getWorkersAvailableCells().get(getStartingPosition()).contains(position) && !built) {
                System.out.println("BUILD OK. SENDING build...");
                built = true;
                setEndPosition(position);

                resetCells();

                if(!getGodCard().equals("Atlas")){
                    getIslandLoader().build(point, false);
                    int towerHeight = billboardStatus.get(position).getTowerHeight();
                    if (towerHeight < 3)
                        billboardStatus.get(position).setTowerHeight(towerHeight + 1);
                    else
                        billboardStatus.get(position).setDome(true);
                }
                sendData();
            }
            else if(built)
                System.out.println("Already built");
            else
                System.out.println("position not contained in available cells");
        }
    }

    public void updateGame(){
        Platform.runLater(()  -> {
            if (getPlayerState() == PlayerState.ACTIVE){

                specialFunctionHandler();

                if (!confirmedStartPosition &&  getStartingPosition() == null)
                    desc.setText("SELECT A WORKER");
                else {
                    switch (getTurnState()) {
                        case MOVE:
                            desc.setText("SELECT the cell where you want to move");
                            moved = false;
                            updateLightenedCells();
                            break;
                        case BUILD:
                            desc.setText("SELECT the cell where you want to build");
                            built = false;
                            updateLightenedCells();

                    }
                }
            }
            else{
                desc.setText("it's "+getMatchPlayers().get(getCurrentPlayer())+"'s turn");

                resetCells();
                setStartingPosition(null);
                setEndPosition(null);
                confirmedStartPosition = false;

                function.setEffect(new Glow(0));
                function.setVisible(false);

                moved = false;
                built = false;

                sFunction = false;
            }
        });



    }

    public void updateBillboard(){
        Map<Integer, Position> movedInPlayers = new HashMap<>();
        Map<Integer, Position> movedOutPlayers =  new HashMap<>();
        Map<Point2D, Point2D> playersMove = new HashMap<>();


        if (billboardStatus != getBillboardStatus()){
            System.out.println("Different billboard");



            for (int player : matchPlayers.keySet()) {
                if (getBillboardStatus().values().stream().noneMatch(cell -> cell.getPlayerID() == player)) {
                    Set<Position> positions = billboardStatus.keySet().stream().filter(pos -> billboardStatus.get(pos).getPlayerID() == player).collect(Collectors.toSet());
                    positions.forEach(pos -> billboardStatus.get(pos).setPlayerID(0));
                    positions.forEach(pos -> getIslandLoader().removeWorker(positionToPoint(pos)));
                }
            }

            matchPlayers = getMatchPlayers();

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
        if (point == null)
            return null;
        else
            return new Position((int) point.getX(), (int) point.getY());
    }

    public static Point2D positionToPoint(Position position){
        if (position == null)
            return null;
        else
            return new Point2D(position.getX(), position.getY());
    }


    public void specialFunctionHandler(){
        System.out.println(getSpecialFunctionAvailable());
        System.out.println(isTerminateTurnAvailable());

        if ((getGodCard().equals("Demeter") || getGodCard().equals("Hephaestus")) && isTerminateTurnAvailable()){
            function.setVisible(true);

            Platform.runLater(() -> {
                String url = "url(images/specialpow/"+getGodCard()+".png)";
                function.setStyle("-fx-background-image: "+url+";\n" +
                                  "-fx-background-size: 70;\n" +
                                  "-fx-background-repeat: no-repeat;\n" +
                                  "-fx-background-position: center;\n" +
                                  "-fx-cursor: hand;");
            });

            function.setOnMouseEntered (mouseOver -> {
                function.setCursor(Cursor.HAND);
                function.scaleXProperty().set(1.1);
                function.scaleYProperty().set(1.1);
                function.scaleZProperty().set(1.1);
            });

            function.setOnMouseExited (mouseOver -> {
                function.setCursor(Cursor.HAND);
                function.scaleXProperty().set(1.0);
                function.scaleYProperty().set(1.0);
                function.scaleZProperty().set(1.0);
            });

            function.setOnMouseReleased(mouseEvent -> {
                System.out.println("END TURN ACTIVATED");
                Platform.runLater(() -> {
                    function.setEffect(new Glow(0.5));
                });

                MessageEvent message = new MessageEvent();
                message.setEndTurn(true);
                notify(message);


            });
        }


        else if (getStartingPosition()!=null && getSpecialFunctionAvailable() !=null && getSpecialFunctionAvailable().containsKey(getStartingPosition()) && getSpecialFunctionAvailable().get(getStartingPosition())){
            function.setVisible(true);

            Platform.runLater(() -> {
                String url = "url(images/specialpow/"+getGodCard()+".png)";
                function.setStyle("-fx-background-image: "+url+";\n" +
                                  "-fx-background-size: 70;\n" +
                                  "-fx-background-repeat: no-repeat;\n" +
                                  "-fx-background-position: center;\n" +
                                  "-fx-cursor: hand;");
            });

            function.setOnMouseEntered (mouseOver -> {
                function.setCursor(Cursor.HAND);
                function.scaleXProperty().set(1.1);
                function.scaleYProperty().set(1.1);
                function.scaleZProperty().set(1.1);
            });

            function.setOnMouseExited (mouseOver -> {
                function.setCursor(Cursor.HAND);
                function.scaleXProperty().set(1.0);
                function.scaleYProperty().set(1.0);
                function.scaleZProperty().set(1.0);
            });

            function.setOnMouseReleased(mouseEvent -> {
                sFunction ^= true;
                System.out.println("SPECIAL FUNCTION"+  sFunction);
                Platform.runLater(() -> {
                    if (sFunction) {
                        function.setEffect(new Glow(0.5));

                    } else {
                        function.setEffect(new Glow(0));
                    }
                });

                MessageEvent message = new MessageEvent();
                message.setSpecialFunction(sFunction);
                notify(message);
                resetCells();
            });
        }
        else{
            function.setEffect(new Glow(0));
            function.setVisible(false);
            sFunction = false;
        }
    }

    public void updateLightenedCells(){
        System.out.println("LIGHTNING CELLS: You are on cell "+getStartingPosition());
        System.out.println("AVAILABLE CELLS -> "+getWorkersAvailableCells().get((getStartingPosition())));
        getIslandLoader().showCells(null);
        getIslandLoader().hideArrow();
        if (getPlayerState() == PlayerState.ACTIVE) {
            getIslandLoader().showArrow(getMatchColors().get(billboardStatus.get(getStartingPosition()).getPlayerID()), positionToPoint(getStartingPosition()));
            getIslandLoader().showCells(getWorkersAvailableCells().get(getStartingPosition()));
        }
    }

    public void resetCells(){
        System.out.println("RESETTED CELLS");
        getIslandLoader().showCells(null);
        getIslandLoader().hideArrow();
    }

    public void win(){
        AnchorPane page;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Controller.class.getClassLoader().getResource("fxml_files/winner.fxml")); //potrebbe dare problemi, ma non credo
        try {
            page = fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println("Failed to load fxml file.");
            exception.printStackTrace();
            return;
        }
        //stackPane.getChildren().clear();
        Platform.runLater(() -> {
            stackPane.getChildren().add(page);
            page.toFront();
            getStage().show();
        });
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
            stackPane.getChildren().add(page);
            page.toFront();
            getStage().show();
        });
    }
}

