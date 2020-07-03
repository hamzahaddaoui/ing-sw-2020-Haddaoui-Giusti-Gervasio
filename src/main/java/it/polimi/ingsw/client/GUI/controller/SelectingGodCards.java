package it.polimi.ingsw.client.GUI.controller;

import it.polimi.ingsw.client.GUI.Controller;
import it.polimi.ingsw.client.GUI.Database;
import it.polimi.ingsw.client.GUI.View;
import it.polimi.ingsw.utilities.MessageEvent;
import it.polimi.ingsw.utilities.PlayerState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

/**
 * @author: hamzahaddaoui
 *
 * Control/View class. Manages the GUI view of showing the cards and descriptions.
 * Manages the selection of the game cards and the submission to the server.
 */

public class SelectingGodCards extends State {
    Glow glowEffect = new Glow(0.5);


    @FXML
    private BorderPane borderPane;

    @FXML
    private ToggleButton Apollo;
    @FXML
    private ToggleButton Artemis;
    @FXML
    private ToggleButton Athena;
    @FXML
    private ToggleButton Atlas;
    @FXML
    private ToggleButton Demeter;
    @FXML
    private ToggleButton Hephaestus;
    @FXML
    private ToggleButton Minotaur;
    @FXML
    private ToggleButton Pan;
    @FXML
    private ToggleButton Prometheus;

    @FXML private ToggleButton Charon;
    @FXML private ToggleButton Eros;
    @FXML private ToggleButton Hestia;
    @FXML private ToggleButton Triton;
    @FXML private ToggleButton Zeus;


    static Map<String, ToggleButton> buttonStringMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.addObserver(Database.getNetworkHandler());
        Database.getNetworkHandler().addObserver(this);
        buttonStringMap = Collections.unmodifiableMap(new HashMap<String, ToggleButton>(){{
            put("Apollo", Apollo);
            put("Artemis",Artemis);
            put("Athena", Athena);
            put("Atlas", Atlas);
            put("Demeter", Demeter);
            put("Hephaestus", Hephaestus);
            put("Minotaur", Minotaur);
            put("Pan", Pan);
            put("Prometheus", Prometheus);
            put("Charon",Charon);
            put("Eros", Eros);
            put("Hestia", Hestia);
            put("Triton",Triton);
            put("Zeus", Zeus);



        }});
        if(Database.getPlayerState() != PlayerState.ACTIVE){
            Label text = new Label();
            text.getStylesheets().add("/css_files/selectingGodCards.css");
            text.getStyleClass().add("label_idle");
            text.setText(Database.getMatchPlayers().get(Database.getCurrentPlayer()) + " is selecting the cards for the match...");
            VBox vbox = new VBox(text);
            vbox.setAlignment(Pos.CENTER);
            borderPane.setTop(vbox);
        }
        else{
            buttonStringMap.values().forEach(toggleButton -> toggleButton.setOnAction(this::selectedCard));
        }
    }

    @Override
    public void showPane(){
            Platform.runLater(() -> Controller.replaceSceneContent("fxml_files/selectingGodCards.fxml"));
    }

    @Override
    public void showError(){

    }

    @Override
    public void sendData(){
        MessageEvent message = new MessageEvent();
        message.setMatchCards(new HashSet<>(Database.getSelectedGodCards()));
        notify(message);
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
            Database.setMatchCards(new ArrayList<>(message.getMatchCards()));
            View.updateView();
            Database.getCurrentState().showPane();
            new Thread(()->{
                Database.getNetworkHandler().removeObserver(this);
                this.removeObserver(Database.getNetworkHandler());
            }).start();
        }
    }


    /**
     * Adds/removes the card to/from the match deck
     */
    public void selectedCard(ActionEvent event){
        String card = ((Node) event.getSource()).getId();
        ToggleButton button =  buttonStringMap.get(card);

        button.setStyle("-fx-scale-x: 1.07;\n" +
                        "-fx-scale-y: 1.07;\n" +
                        "-fx-scale-z: 1.07;\n" +
                        "-fx-border-color: yellow;\n" +
                        "-fx-border-width: 450%;");

        if (button.isSelected()) {
            if (Database.getSelectedGodCards().size() == Database.getPlayersNum()) {
                ToggleButton removeButton = buttonStringMap.get(Database.removeSelectedGodCard());
                removeButton.setSelected(false);
                removeButton.setStyle("-fx-scale-x: 1.00;\n" +
                                      "-fx-scale-y: 1.00;\n" +
                                      "    -fx-scale-z: 1.00;\n" +
                                      "    -fx-border-color: transparent;\n" +
                                      "    -fx-border-width: 0%;");
            }
            Database.addSelectedGodCard(card);
        }
        else{
            button.setStyle("-fx-scale-x: 1.00;\n" +
                                  "-fx-scale-y: 1.00;\n" +
                                  "    -fx-scale-z: 1.00;\n" +
                                  "    -fx-border-color: transparent;\n" +
                                  "    -fx-border-width: 0%;");
            Database.removeSelectedGodCard(card);
        }
        System.out.println("selected cards: " + Database.getSelectedGodCards());
    }

    /**
     * Shows the card description when the mouse is over the match deck
     */
    public void hoverCard(MouseEvent mouseEvent){
        String card = ((Node) mouseEvent.getSource()).getId();
        ToggleButton button =  buttonStringMap.get(card);
        button.setEffect(glowEffect);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/"+card+".png",872,497,false,true)));
    }

    /**
     * Removes the description when the mouse is over no card
     */
    public void hoverNone(MouseEvent event){
        Node source= (Node) event.getSource();
        Glow glow=(Glow) source.getEffect();
        source.setEffect(glow);
        glow.setLevel(0.0);

        if (Database.getPlayerState() != PlayerState.ACTIVE){
            borderPane.setCenter(new Pane());
        }

        else if (Database.getSelectedGodCards().size() == Database.getPlayersNum() ) {
            Button button = new Button();
            button.setId("ok_button");
            borderPane.setCenter(button);
            button.setOnAction(actionEvent -> {
                sendData();
            });
        }
        else
            borderPane.setCenter(new Pane());
    }


}
