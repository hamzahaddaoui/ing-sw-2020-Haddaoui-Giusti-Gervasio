package it.polimi.ingsw.gui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable{
    private final double glowLevel = 0.5;

    @FXML private ImageView santorini ;

    @FXML private TextField nickname;
    @FXML private TextField address;

    @FXML private BorderPane borderPane;

    @FXML private AnchorPane paneSS;
    @FXML private AnchorPane paneSN;
    @FXML private AnchorPane paneSC;
    @FXML private AnchorPane paneWFP;


    @FXML private ToggleButton Apollo;
    @FXML private ToggleButton Artemis;
    @FXML private ToggleButton Athena;
    @FXML private ToggleButton Atlas;
    @FXML private ToggleButton Demeter;
    @FXML private ToggleButton Hephaestus;
    @FXML private ToggleButton Minotaur;
    @FXML private ToggleButton Pan;
    @FXML private ToggleButton Prometheus;

    static Map <String, ToggleButton> buttonStringMap;
    static Stage stage;
    static String IP, nick;
    static int num;
    static List<String> godCards = new ArrayList<>();


    public void setStage (Stage stage){
        this.stage = stage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        buttonStringMap = Collections.unmodifiableMap(new HashMap<String, ToggleButton>(){{
            put("Apollo", Apollo);
            put("Artemis",Artemis);
            put("Athena", Athena);
            put("Atlas", Atlas);
            put("Demeter", Demeter);
            put("Hephaestus", Hephaestus);
            put("Minotaur", Minotaur);
            put("Pan", Pan);
            put("Prometheus",Prometheus);

        }});
        ScaleTransition rt = new ScaleTransition(Duration.seconds(0.0005), santorini);
        rt.setCycleCount(1);
        rt.setToX(0);
        rt.setToY(0);
        rt.play();

        ScaleTransition rt2 = new ScaleTransition(Duration.seconds(5), santorini);
        rt2.setCycleCount(1);
        rt2.setToX(1.1);
        rt2.setToY(1.1);
        rt2.play();

        ScaleTransition rt3 = new ScaleTransition(Duration.seconds(5), santorini);
        rt3.setCycleCount(1);
        rt3.setToX(1);
        rt3.setToY(1);

        rt2.setOnFinished((e) -> rt3.play());
    }

    public void play(MouseEvent mouseEvent) throws Exception{
        IP = address.getCharacters().toString();
        nick =  nickname.getCharacters().toString();

        System.out.println("Connection to: "+ IP +  " - Nickname: "+nick );
        replaceSceneContent("fxml_files/selectNum.fxml");
    }

    public void selectedTwoPlayers(MouseEvent mouseEvent){
        num = 2;
        System.out.println("Match players: 2");
        replaceSceneContent("fxml_files/selectCards.fxml");
    }

    public void selectedThreePlayers(MouseEvent mouseEvent){
        num = 3;
        System.out.println("Match players: 3");
        replaceSceneContent("fxml_files/selectCards.fxml");
    }

    public void selectedCards(){
        replaceSceneContent("fxml_files/waitForPlayers.fxml");
    }


    private void replaceSceneContent(String fxml) {
        Parent page = null;
        try {
            page = (Parent) FXMLLoader.load((Objects.requireNonNull(getClass().getClassLoader().getResource(fxml))));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        stage.getScene().setRoot(page);
        stage.show();
    }



    public void selectedApollo(){
        if (Apollo.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Apollo");

        }
        else{
            godCards.remove("Apollo");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedArtemis(){
        if (Artemis.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Artemis");
        }
        else{
            godCards.remove("Artemis");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedAthena(){
        if (Athena.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Athena");
        }
        else{
            godCards.remove("Athena");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedAtlas(){
        if (Atlas.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Atlas");
        }
        else{
            godCards.remove("Atlas");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedDemeter(){
        if (Demeter.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Demeter");
        }
        else{
            godCards.remove("Demeter");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedHephaestus(){
        if (Hephaestus.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Hephaestus");
        }
        else{
            godCards.remove("Hephaestus");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedMinotaur(){
        if (Minotaur.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Minotaur");
        }
        else{
            godCards.remove("Minotaur");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedPan(){
        if (Pan.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Pan");
        }
        else{
            godCards.remove("Pan");
        }
        System.out.println("selected cards: "+godCards);
    }

    public void selectedPrometheus(){
        if (Prometheus.isSelected()) {
            if (godCards.size() == num)
                buttonStringMap.get(godCards.remove(0)).setSelected(false);
            godCards.add("Prometheus");
        }
        else{
            godCards.remove("Prometheus");
        }
        System.out.println("selected cards: "+godCards);
    }


    public void hoverApollo(){
        Glow glow=new Glow();
        Apollo.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/apollo_desc.png",872,497,false,true)));
    }

    public void hoverArtemis(){
        Glow glow=new Glow();
        Artemis.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/artemis_desc.png",872,497,false,true)));
    }

    public void hoverAthena(){
        Glow glow=new Glow();
        Athena.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/athena_desc.png",872,497,false,true)));
    }

    public void hoverAtlas(){
        Glow glow=new Glow();
        Atlas.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/atlas_desc.png",872,497,false,true)));
    }

    public void hoverDemeter(){
        Glow glow=new Glow();
        Demeter.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/demeter_desc.png",872,497,false,true)));
    }

    public void hoverHephaestus(){
        Glow glow=new Glow();
        Hephaestus.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/hephaestus_desc.png",872,497,false,true)));
    }

    public void hoverMinotaur(){
        Glow glow=new Glow();
        Minotaur.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/minotaur_desc.png", 872, 497, false, true)));
    }

    public void hoverPan(){
        Glow glow=new Glow();
        Pan.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/pan_desc.png",872,497,false,true)));
    }

    public void hoverPrometheus(){
        Glow glow=new Glow();
        Prometheus.setEffect(glow);
        glow.setLevel(glowLevel);
        borderPane.setCenter(new ImageView(new Image("images/god_desc/prometheus_desc.png",872,497,false,true)));
    }

    public void hoverNone(MouseEvent event){
        Node source= (Node) event.getSource();
        Glow glow=(Glow) source.getEffect();
        source.setEffect(glow);
        glow.setLevel(0.0);
        if (godCards.size()==num) {
            Button button = new Button();
            button.setId("ok_button");
            borderPane.setCenter(button);
            button.setOnAction(actionEvent -> selectedCards());
        }
        else
            borderPane.setCenter(new Pane());
    }
}