package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.client.GUI.IslandLoader;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.utilities.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


public class Running extends State{
    @FXML StackPane stackPane;
    @FXML BorderPane borderPane;
    @FXML Label user;
    @FXML Label desc;
    @FXML ImageView god;
    @FXML ImageView userPane;

    @FXML Button function;

    @FXML Button helper;

    static private Map<Position, Cell> billboardStatus = new HashMap<>();
    static private Map<Integer, String> matchPlayers = new HashMap<>();

    boolean confirmedStartPosition;
    boolean moved;
    boolean built;
    boolean sFunction;

    @Override
    public void showPane(){
        Platform.runLater(() -> {
            Controller.replaceSceneContent("fxml_files/running.fxml");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        this.addObserver(Database.getNetworkHandler());
        Database.getNetworkHandler().addObserver(this);

        billboardStatus = Database.getBillboardStatus();
        matchPlayers = Database.getMatchPlayers();

        //inizializzo gli elementi grafici del layer superiore
        user.getStylesheets().add("/css_files/placingWorkers.css");
        userPane.setImage(new Image("images/user_" + Database.getMatchColors().get(Database.getPlayerID()) + ".png", 150, 75, false, true));
        user.getStyleClass().add("player");
        user.setText(Database.getNickname());
        god.setImage(new Image("images/gods_no_back/" + Database.getGodCard() + ".png",120,140,false,true));

        //inizializzo l'isola
        Database.setIslandLoader(new IslandLoader());
        try {
            IslandLoader.start(stackPane);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        //inizializzo i giocatori
        for (Position position : billboardStatus.keySet()){
            if (billboardStatus.get(position).getPlayerID() != 0){
                Database.getIslandLoader().putWorker(positionToPoint(position), Database.getMatchColors().get(billboardStatus.get(position).getPlayerID()));
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
        message.setStartPosition(Database.getStartingPosition());
        message.setEndPosition(Database.getEndPosition());
        notify(message);
    }

    @Override
    public void update(MessageEvent message){

        System.out.println(Database.getNetworkHandler().getObservers());
        System.out.println("Message received: "+ message);

        if (message.getError()){
            System.out.println(message);
            showError();
            return;
        }
        else if (message.getPlayerState() == PlayerState.LOST){
            Database.updateStandardData(message);
            Database.setBillboardStatus(message.getBillboardStatus());
            updateBillboard(true);

            System.out.println("LOSER");
            //getIslandLoader().endAnimation();
            lost();
            return;

        }
        else if (message.getPlayerState() == PlayerState.WIN){
            Database.updateStandardData(message);
            Database.setBillboardStatus(message.getBillboardStatus());
            updateBillboard(true);
            Database.getIslandLoader().endAnimation();
            System.out.println("WINNER");
            win();
            return;
        }
        else if (message.isFinished()){
            //disconnessione di qualcuno <.<
            Database.setCurrentState(new userDisconnected());
            Database.getCurrentState().showPane();
            return;
        }



        Database.updateStandardData(message);
        Database.setTerminateTurnAvailable(message.getTerminateTurnAvailable());
        Database.setSpecialFunctionAvailable(message.getSpecialFunctionAvailable());
        Database.setWorkersAvailableCells(message.getWorkersAvailableCells());
        Database.setBillboardStatus(message.getBillboardStatus());

        Platform.runLater(() -> updateBillboard(true));

        updateGame();
    }


    public boolean workerClick(Point2D point){
        Position position = pointToPosition(point);
        System.out.println("worker clicked + "+position);
        System.out.println(Database.getWorkersAvailableCells());

        if (Database.getPlayerState() == PlayerState.IDLE) {
            System.out.println("PLAYER NOT ACTIVE");
            return false;
        }
        else if (Database.getWorkersAvailableCells().containsKey(position) && !confirmedStartPosition){
            System.out.println("PLAYER ACTIVE. SETTED POSITION");
            Platform.runLater(()  -> {
                if (Database.getTurnState() == TurnState.MOVE)
                    desc.setText("SELECT the cell where you want to move");
                if (Database.getTurnState() == TurnState.BUILD)
                    desc.setText("SELECT the cell where you want to build");
            });
            Database.setStartingPosition(position);
            updateLightenedCells();


            specialFunctionHandler();
            return true;
        }
        return false;
    }


    public void boardClick(Point2D point){
        Position position = pointToPosition(point);
        System.out.println("Click on board/building detected on "+ position);

        if (Database.getPlayerState() == PlayerState.IDLE || Database.getStartingPosition() == null) {
            System.out.println("IDLE OR NOT SELECTED ANY WORKER");
            return;
        }



        if (Database.getTurnState() == TurnState.MOVE && !moved){
            if (Database.getWorkersAvailableCells().get(Database.getStartingPosition()).contains(position)){
                confirmedStartPosition = true;
                moved = true;
                System.out.println("MOVE OK. SENDING movement...");

                resetCells();

                /*if (billboardStatus.get(position).getPlayerID() == 0) {
                    //getIslandLoader().moveWorker(positionToPoint(getStartingPosition()), point);
                    billboardStatus.get(position).setPlayerID(getPlayerID());
                    billboardStatus.get(getStartingPosition()).setPlayerID(0);
                    updateBillboard(false);
                }*/
                Database.setEndPosition(position);
                sendData();
                Database.setStartingPosition(position);
            }
            else if(moved)
                System.out.println("Already moved");
            else
                System.out.println("position not contained in available cells");
        }
        else if (Database.getTurnState() == TurnState.BUILD) {

            if (Database.getWorkersAvailableCells().get(Database.getStartingPosition()).contains(position) && !built) {
                System.out.println("BUILD OK. SENDING build...");
                built = true;
                Database.setEndPosition(position);

                resetCells();

                /*if(!getGodCard().equals("Atlas")){
                    getIslandLoader().build(point, false);
                    int towerHeight = billboardStatus.get(position).getTowerHeight();
                    if (towerHeight < 3)
                        billboardStatus.get(position).setTowerHeight(towerHeight + 1);
                    else
                        billboardStatus.get(position).setDome(true);

                    updateBillboard(false);
                }*/
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
            if (Database.getPlayerState() == PlayerState.ACTIVE){

                specialFunctionHandler();

                if (!confirmedStartPosition && Database.getStartingPosition() == null)
                    desc.setText("SELECT A WORKER");
                else {
                    switch (Database.getTurnState()) {
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
                desc.setText("it's " + Database.getMatchPlayers().get(Database.getCurrentPlayer()) + "'s turn");

                resetCells();
                Database.setStartingPosition(null);
                Database.setEndPosition(null);
                confirmedStartPosition = false;

                function.setEffect(new Glow(0));
                function.setVisible(false);

                moved = false;
                built = false;

                sFunction = false;
            }
        });



    }

    public void updateBillboard(boolean bool){
        Map<Integer, Position> movedInPlayers = new HashMap<>();
        Map<Integer, Position> movedOutPlayers =  new HashMap<>();
        Map<Point2D, Point2D> playersMove = new HashMap<>();


        if (billboardStatus != Database.getBillboardStatus()){
            System.out.println("Different billboard");


            if (matchPlayers.size() != Database.getMatchPlayers().size()) {
                for (int player : matchPlayers.keySet()) {
                    if (Database.getBillboardStatus().values().stream().noneMatch(cell -> cell.getPlayerID() == player)) {
                        Set<Position> positions = billboardStatus.keySet().stream().filter(pos -> billboardStatus.get(pos).getPlayerID() == player).collect(Collectors.toSet());
                        positions.forEach(pos -> billboardStatus.get(pos).setPlayerID(0));
                        positions.forEach(pos -> Database.getIslandLoader().removeWorker(positionToPoint(pos)));
                    }
                }
            }

            matchPlayers = Database.getMatchPlayers();

            Set<Position> changedPositions =  Database.getBillboardStatus()
                    .keySet()
                    .stream()
                    .filter(position -> ! Database.getBillboardStatus().get(position).equals(billboardStatus.get(position)))
                    .collect(Collectors.toSet());




            for (Position position : changedPositions){
                if (Database.getBillboardStatus().get(position).getTowerHeight() != billboardStatus.get(position).getTowerHeight()){
                    System.out.println("Different height in "+position);
                    for(int i = 0; i<(Database.getBillboardStatus().get(position).getTowerHeight() - billboardStatus.get(position).getTowerHeight()); i++){
                        Database.getIslandLoader().build(positionToPoint(position), false);
                    }
                }
                if (Database.getBillboardStatus().get(position).isDome() != billboardStatus.get(position).isDome()){
                    System.out.println("Different dome in "+position);
                    Database.getIslandLoader().build(positionToPoint(position), true);
                }
                if (Database.getBillboardStatus().get(position).getPlayerID() != billboardStatus.get(position).getPlayerID()){
                    //positionPlayers.put(Map.of(billboardStatus.get(position).getPlayerID(), getBillboardStatus().get(position).getPlayerID()), position);
                    System.out.println("Different player in "+position);
                    if (billboardStatus.get(position).getPlayerID() == 0) //se la cella era vuota, ed ora è piena
                        movedInPlayers.put(Database.getBillboardStatus().get(position).getPlayerID(), position);
                    else {
                        movedOutPlayers.put(billboardStatus.get(position).getPlayerID(), position);
                        if (Database.getBillboardStatus().get(position).getPlayerID() != 0)
                            movedInPlayers.put(Database.getBillboardStatus().get(position).getPlayerID(), position);
                    }
                }
            }



            if (movedOutPlayers.size() != 0){
                System.out.println("Moved in players: ");
                movedInPlayers.keySet().forEach(player -> System.out.println("Player "+player +" - Position "+movedInPlayers.get(player)));
                System.out.println("Moved out players: ");

                movedOutPlayers.keySet().forEach(player -> System.out.println("Player "+player +" - Position "+movedOutPlayers.get(player)));

                movedOutPlayers.keySet().forEach(ID -> playersMove.put(positionToPoint(movedOutPlayers.get(ID)), positionToPoint(movedInPlayers.get(ID))));

                if(playersMove.size() == 1) {
                    System.out.println("SINGLE MOVE");
                    playersMove.keySet().forEach(startPosition -> Database.getIslandLoader().moveWorker(startPosition, playersMove.get(startPosition)));
                }
                else{
                    System.out.println("DOUBLE MOVE");
                    Iterator<Point2D> iterator = playersMove.keySet().iterator();
                    Point2D startPos1 = iterator.next();
                    Point2D startPos2 = iterator.next();
                    Database.getIslandLoader().swapWorkers(startPos1, startPos2, playersMove.get(startPos1), playersMove.get(startPos2));
                }


            }

        }

        if (bool)
            billboardStatus = Database.getBillboardStatus();
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
        System.out.println(Database.getSpecialFunctionAvailable());
        System.out.println(Database.isTerminateTurnAvailable());

        if ((Database.getGodCard().equals("Demeter") || Database.getGodCard().equals("Hephaestus")) && Database.isTerminateTurnAvailable()){
            function.setVisible(true);

            Platform.runLater(() -> {
                String url = "url(images/specialpow/" + Database.getGodCard() + ".png)";
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


        else if (Database.getStartingPosition() != null && Database.getSpecialFunctionAvailable() != null && Database.getSpecialFunctionAvailable().containsKey(Database.getStartingPosition()) && Database.getSpecialFunctionAvailable().get(Database.getStartingPosition())){
            function.setVisible(true);

            Platform.runLater(() -> {
                String url = "url(images/specialpow/" + Database.getGodCard() + ".png)";
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
        System.out.println("LIGHTNING CELLS: You are on cell " + Database.getStartingPosition());
        System.out.println("AVAILABLE CELLS -> " + Database.getWorkersAvailableCells().get((Database.getStartingPosition())));
        Database.getIslandLoader().showCells(null);
        Database.getIslandLoader().hideArrow();
        if (Database.getPlayerState() == PlayerState.ACTIVE) {
            Database.getIslandLoader().showArrow(Database.getMatchColors().get(billboardStatus.get(Database.getStartingPosition()).getPlayerID()), positionToPoint(Database.getStartingPosition()));
            Database.getIslandLoader().showCells(Database.getWorkersAvailableCells().get(Database.getStartingPosition()));
        }
    }

    public void resetCells(){
        System.out.println("RESETTED CELLS");
        Database.getIslandLoader().showCells(null);
        Database.getIslandLoader().hideArrow();
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
            Database.getStage().show();
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
            Database.getStage().show();
        });
    }


    public void helperClick(MouseEvent event){
        Popup popup = new Popup();
        //ImageView imageView = new ImageView(new Image("images/helper/" + getGodCard() + ".png",450,225,true,true));
        final ImageView imgView = new ImageView();
        Image image = new Image("images/helper/" + Database.getGodCard() + ".png");
        imgView.setImage(image);
        imgView.setFitWidth(450);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);

        imgView.setOnMouseReleased(e -> popup.hide());
        //popup.getStylesheets().add("/css_files/running.css");
        //imageView.getStyleClass().add("popup");
        popup.getContent().add(imgView);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.setOnShown(e -> {
            popup.setX(Database.getStage().getX() + Database.getStage().getMaxWidth() - popup.getWidth());
            popup.setY(Database.getStage().getY() + 22);
            helper.setVisible(false);
        });

        popup.setOnHidden( e -> helper.setVisible(true));

        Platform.runLater(() -> popup.show(Database.getStage()));
    }


}
