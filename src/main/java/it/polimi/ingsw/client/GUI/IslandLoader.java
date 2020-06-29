package it.polimi.ingsw.client.GUI;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import it.polimi.ingsw.client.GUI.controller.Running;
import it.polimi.ingsw.utilities.Position;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class IslandLoader{

    private static double anchorX, anchorY;
    private static double anchorAngleX = 0;
    private static double anchorAngleY = 0;
    private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private static final DoubleProperty angleY = new SimpleDoubleProperty(0);

    //altezza ideale di ogni cella 0 1 2 3
    private static Map<Point2D, Integer> boardCells = new HashMap<>();

    //altezza reale di ogni cella
    private static final Map<Integer, Double> cellHeight = Map.of(0, 0.3, 1, - 0.9, 2, - 2.2, 3, - 3.0);

    //posizione ideale dei worker nella mappa
    private static Map<Group, Point3D> workers = new HashMap<>();

    static SequentialTransition sequentialTransition ;

    static Group group;
    static Set<Group> cells = new HashSet<>();

    static Group arrow;

    static AtomicBoolean drag = new AtomicBoolean(false);


    //posizione reale di ogni punto
    private static final Map<Point2D, Point2D> Point2DMap = Collections.unmodifiableMap(new HashMap<Point2D, Point2D>(){{

        /*
         * coordinate primo punto: indice di riga, indice di colonna, altezza
         * coordinate secondo punto: riga, altezza, colonna
         *
         * */

        put(new Point2D(0,0), new Point2D(-5, -5.1));
        put(new Point2D(0,1), new Point2D(-5, -2.5));
        put(new Point2D(0,2), new Point2D(-5, 0));
        put(new Point2D(0,3), new Point2D(-5, 2.5));
        put(new Point2D(0,4), new Point2D(-5, 5));

        put(new Point2D(1,0), new Point2D(-2.5, -5.1));
        put(new Point2D(1,1), new Point2D(-2.5, -2.5));
        put(new Point2D(1,2), new Point2D(-2.5, 0));
        put(new Point2D(1,3), new Point2D(-2.5, 2.5));
        put(new Point2D(1,4), new Point2D(-2.5, 5));

        put(new Point2D(2,0), new Point2D(0, -5.1));
        put(new Point2D(2,1), new Point2D(0, -2.5));
        put(new Point2D(2,2), new Point2D(0, +0));
        put(new Point2D(2,3), new Point2D(0, +2.5));
        put(new Point2D(2,4), new Point2D(0, +5));

        put(new Point2D(3,0), new Point2D(2.5, -5.1));
        put(new Point2D(3,1), new Point2D(2.5, -2.5));
        put(new Point2D(3,2), new Point2D(2.5, 0));
        put(new Point2D(3,3), new Point2D(2.5, 2.5));
        put(new Point2D(3,4), new Point2D(2.5, 5));

        put(new Point2D(4,0), new Point2D(5.1, -5.1));
        put(new Point2D(4,1), new Point2D(5.1, -2.5));
        put(new Point2D(4,2), new Point2D(5.1, 0));
        put(new Point2D(4,3), new Point2D(5.1, 2.5));
        put(new Point2D(4,4), new Point2D(5.1,5));
    }});

    /**
     * Load an obj file, with its own mesh
     * @param url the location of the obj file
     * @return the loaded model Group object
     */
    private static Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }


    /**
     * Initializating the world and all the graphical objects.
     * @param stackPane The parent pane on which initializing the world
     */
    public static void start(StackPane stackPane) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardCells.put(new Point2D(i, j), 0);
            }
        }
        drag.set(false);
        arrow = null;
        cells = new HashSet<>();
        workers = new HashMap<>();
        if (sequentialTransition != null && sequentialTransition.getStatus() == Animation.Status.RUNNING)
            sequentialTransition.stop();

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setLightOn(true);
        ambientLight.setColor(Color.WHITE);
        ambientLight.setOpacity(90);
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.DARKSLATEGREY);

        pointLight.getTransforms().add(new Translate(65000, - 250000, 65000));
        pointLight.getTransforms().add(new Rotate(180, Rotate.Y_AXIS));

        group = new Group();
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(3000.0);
        camera.setTranslateZ(- 40);
        angleX.set(90);
        angleY.set(90);

        Group cliff = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Cliff.obj"));
        Group board = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Board.obj"));
        Group innerWalls = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/InnerWalls.obj"));
        Group outerWalls = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/OuterWall.obj"));
        Group sea = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Sea.obj"));
        Group islands = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Islands.obj"));
        Group boat = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Boat.obj"));
        innerWalls.setMouseTransparent(true);
        outerWalls.setMouseTransparent(true);
        Group tree1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree1.obj"));
        Group tree2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree2.obj"));
        Group tree3 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree3.obj"));
        Group tree4 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree4.obj"));
        Group tree5 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree5.obj"));
        Group tree6 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/tree6.obj"));
        Group sheep1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/sheep1.obj"));
        Group sheep2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/sheep2.obj"));
        Group sheep3 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/sheep3.obj"));
        Group wolf1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/wolf1.obj"));
        Group wolf2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/wolf2.obj"));
        Group bird1 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/bird1.obj"));
        Group bird2 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/bird2.obj"));
        Group bird3 = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/bird3.obj"));

        //BOAT ANIMATION
        {
            Timeline timelineBoat = new Timeline(
                    new KeyFrame(
                            Duration.seconds(30),
                            new KeyValue(boat.translateXProperty(), 13)
                    )
            );

            RotateTransition rotate1 = new RotateTransition();
            rotate1.setAxis(Rotate.Y_AXIS);
            // setting the angle of rotation
            rotate1.setByAngle(180);
            //Setting duration of the transition
            rotate1.setDuration(Duration.seconds(10));
            rotate1.setNode(boat);


            Timeline timelineBoat3 = new Timeline(
                    new KeyFrame(
                            Duration.seconds(30),
                            new KeyValue(boat.translateXProperty(), 0)
                    )
            );

            RotateTransition rotate2 = new RotateTransition();
            rotate2.setAxis(Rotate.Y_AXIS);
            // setting the angle of rotation
            rotate2.setByAngle(- 180);
            //Setting duration of the transition
            rotate2.setDuration(Duration.seconds(10));
            rotate2.setNode(boat);

            SequentialTransition transition = new SequentialTransition(timelineBoat, rotate1, timelineBoat3, rotate2);
            //transition.setAutoReverse(true);
            transition.setCycleCount(- 1);
            transition.play();

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(4),
                            new KeyValue(angleX, 30),
                            new KeyValue(angleY, 450)
                    )
            );
            timeline.play();
        }


        group.getChildren().addAll(cliff,board, islands, wolf1, wolf2, tree1, tree2, tree3, tree4, sheep1, sheep2, sheep3, bird1, bird2, bird3, tree5, tree6,
                innerWalls,outerWalls, sea, ambientLight, pointLight, boat);


        SubScene subScene = new SubScene(new Group(group), Database.getStage().getMaxWidth(), Database.getStage().getMaxHeight(), true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.TRANSPARENT);
        stackPane.getChildren().add(subScene);
        subScene.toBack();
        stackPane.lookup("#background").toBack();

        board.setOnMouseEntered(e -> board.setCursor(Cursor.HAND));
        board.setOnMouseDragged(e -> drag.set(true));
        board.setOnMouseClicked(e -> {
            Point3D click = e.getPickResult().getIntersectedPoint();
            Point2D point = new Point2D(click.getX(), click.getZ());
            Point2D point2D = findCell(new Point2D(point.getX(), point.getY()));
            clickHandler(point2D);

        });

        //INIZIALIZZO MOVIMENTO E ZOOM TELECAMERA
        {
            initMouseControl(group, subScene);
            subScene.addEventHandler(ZoomEvent.ZOOM, zoomEvent -> {
                camera.setTranslateZ(camera.getTranslateZ()/zoomEvent.getZoomFactor());
                if(camera.getTranslateZ()>-15)
                    camera.setTranslateZ(-15);
                else if (camera.getTranslateZ() < -100)
                    camera.setTranslateZ(-100);
            });}
    }


    private static void initMouseControl(Group group, SubScene scene) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
            //System.out.println(angleX.get()+" "+angleY.get());
            if (angleX.get() > 90.0)
                angleX.set(90.0);
            if (angleX.get() < 15.0)
                angleX.set(15.0);
        });
    }

    private static Point2D findCell(Point2D clickPoint){
        final double limit = -6;
        final double offset = 2.4;
        Point2D upperPoint = new Point2D(limit, 0);
        Point2D lowerPoint = new Point2D(limit+offset, 0);

        int i, j;

        for(i=0;i<5;i++){
            if(upperPoint.subtract(lowerPoint).dotProduct(clickPoint.subtract(lowerPoint)) >= 0 && lowerPoint.subtract(upperPoint).dotProduct(clickPoint.subtract(upperPoint)) >= 0)
                break;
            upperPoint = new Point2D(upperPoint.getX()+offset,0);
            lowerPoint = new Point2D(lowerPoint.getX()+offset,0);
        }


        upperPoint = new Point2D(0, limit);
        lowerPoint = new Point2D(0, limit+offset);

        for (j=0; j<5;j++){
            if(upperPoint.subtract(lowerPoint).dotProduct(clickPoint.subtract(lowerPoint)) >= 0 && lowerPoint.subtract(upperPoint).dotProduct(clickPoint.subtract(upperPoint)) >= 0)
                break;
            upperPoint = new Point2D(0,upperPoint.getY()+offset);
            lowerPoint = new Point2D(0,lowerPoint.getY()+offset);
        }

        if (i>=4)
            i=4;

        if (j>=4)
            j=4;
        return new Point2D(i,j);
    }

    private static void clickHandler(Point2D point){
        if (drag.get()){
            drag.set(false);
            return;
        }


        System.out.println("Click on board/building detected on "+ point);
        ((Running) Database.getCurrentState()).boardClick(point);
    }

    public void putWorker(Point2D point, String color){
        if (boardCells.get(point) == 4)
            return;
        Group worker = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/worker_"+color.toLowerCase()+"_0.obj"));

        worker.setTranslateX(-worker.localToScene(worker.getBoundsInLocal()).getMaxX()+worker.localToScene(worker.getBoundsInLocal()).getWidth()/2);
        worker.setTranslateY(-worker.localToScene(worker.getBoundsInLocal()).getMaxY());
        worker.setTranslateZ(-worker.localToScene(worker.getBoundsInLocal()).getMaxZ()+worker.localToScene(worker.getBoundsInLocal()).getDepth()/2);

        worker.setTranslateX(Point2DMap.get(point).getX());
        //worker.setTranslateY(cellHeight.get(boardCells.get(point)));
        worker.setTranslateZ(Point2DMap.get(point).getY());
        //worker.setMouseTransparent(true);

        workers.put(worker, new Point3D(point.getX(), point.getY(), boardCells.get(point)));

        Translate translate = new Translate();
        double height = - 10;
        worker.setTranslateY(height);
        worker.getTransforms().add(translate);

        {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(5),
                            new KeyValue(translate.yProperty(), - height + cellHeight.get(boardCells.get(point)))
                    )
            );
            timeline.play();
        }

        worker.setOnMouseEntered(e -> worker.setCursor(Cursor.HAND));

        worker.setOnMouseClicked(e -> {
            Point2D point2D = new Point2D(workers.get(worker).getX(), workers.get(worker).getY());
            System.out.println("Mouse clicked in "+ point2D);
            if (((Running) Database.getCurrentState()).workerClick(point2D)){
                System.out.println("WORKER SELEZIONATO "+point2D);
            }
        });

        group.getChildren().add(worker);
    }

    private void buildBlockLevel1(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/BuildingBlock01.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);
        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());

        boardCells.replace(point, 1);

        {
            Translate translate = new Translate();
            double height = - 10;
            block.setTranslateY(height);
            block.getTransforms().add(translate);
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0.35),
                            new KeyValue(translate.yProperty(), - height)
                    )
            );
            timeline.play();
        }

        Platform.runLater( () -> group.getChildren().add(block));

        block.setOnMouseEntered(e -> block.setCursor(Cursor.HAND));

        block.setOnMouseClicked(e -> clickHandler(point));
    }

    private void buildBlockLevel2(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/BuildingBlock02.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);
        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());
        boardCells.replace(point, 2);
        {
            Translate translate = new Translate();
            double height = - 10;
            block.setTranslateY(height);
            block.getTransforms().add(translate);
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0.35),
                            new KeyValue(translate.yProperty(), - height)
                    )
            );
            timeline.play();
        }
        Platform.runLater( () -> group.getChildren().add(block));
        block.setOnMouseEntered(e -> block.setCursor(Cursor.HAND));
        block.setOnMouseClicked(e -> clickHandler(point));
    }

    private void buildBlockLevel3(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/BuildingBlock03.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);
        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());
        boardCells.replace(point, 3);

        Translate translate = new Translate();
        double height = -10;
        block.setTranslateY(height);
        block.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), -height)
                )
        );
        timeline.play();


        Platform.runLater( () -> group.getChildren().add(block));
        block.setOnMouseEntered(e -> block.setCursor(Cursor.HAND));
        block.setOnMouseClicked(e -> {
            clickHandler(point);
        });
    }

    private void buildDome(Point2D point){
        Group block = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/Dome.obj"));
        block.setTranslateX(-block.localToScene(block.getBoundsInLocal()).getMaxX()+block.localToScene(block.getBoundsInLocal()).getWidth()/2);

        //block.setTranslateY(-block.localToScene(block.getBoundsInLocal()).getMaxY());
        block.setTranslateZ(-block.localToScene(block.getBoundsInLocal()).getMaxZ()+block.localToScene(block.getBoundsInLocal()).getDepth()/2);
        //block.setMouseTransparent(true);
        block.setTranslateX(Point2DMap.get(new Point2D(point.getX(), point.getY())).getX());
        block.setTranslateZ(Point2DMap.get(new Point2D(point.getY(), point.getY())).getY());


        Translate translate = new Translate();
        double height = -10;
        block.setTranslateY(height);
        block.getTransforms().add(translate);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.35),
                        new KeyValue(translate.yProperty(), - height + cellHeight.get(boardCells.get(point)) -0.3)
                )
        );
        timeline.play();

        boardCells.replace(point, 4);
        Platform.runLater( () -> group.getChildren().add(block));
    }


    /**
     * Build a block on a certain point in the billboard
     * @param point where to build a block
     * @param dome build a dome, instead of a block
     */
    public void build(Point2D point, boolean dome){
        if (dome){
            //correggere l'altezza a seconda
            buildDome(point);
            return;
        }

        switch (boardCells.get(point)) {
            case 0:

                buildBlockLevel1(point);
                break;
            case 1:

                buildBlockLevel2(point);
                break;
            case 2:

                buildBlockLevel3(point);
                break;
            case 3:

                buildDome(point);
                break;
        }
    }

    /**
     * Move a worker from a position to another
     * @param startPos starting position
     * @param endPos end position
     */
    public void moveWorker(Point2D startPos, Point2D endPos){
        Optional<Group> optionalWorker =  workers.keySet().stream()
                .filter(w -> new Point2D(workers.get(w).getX(), workers.get(w).getY()).equals(startPos))
                .findAny();

        if (!optionalWorker.isPresent()){
            System.out.println("worker non trovato");
            return;
        }


        Group worker = optionalWorker.get();

        Point3D prev = new Point3D(Point2DMap.get(startPos).getX(), cellHeight.get(boardCells.get(startPos)), Point2DMap.get(startPos).getY());

        Point3D next = new Point3D(Point2DMap.get(endPos).getX(), cellHeight.get(boardCells.get(endPos)), Point2DMap.get(endPos).getY());

        Point3D trasl = next.subtract(prev);

        System.out.println("worker: " + startPos + endPos);
        System.out.println("prev: "+prev+" - next: "+ next+" - trasl"+trasl);

        Translate translate = new Translate();
        worker.getTransforms().add(translate);


        SequentialTransition sequence;

        if (boardCells.get(endPos) - boardCells.get(startPos) >= 1) {
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15 * Math.abs(boardCells.get(endPos) - boardCells.get(startPos))),
                            new KeyValue(translate.yProperty(), trasl.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
            KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
            KeyValue yKV = new KeyValue(translate.yProperty(),  trasl.getY()- 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence = new SequentialTransition(timeline1, timeline2);
        }
        else if (boardCells.get(endPos) - boardCells.get(startPos) == 0) {
            Timeline timeline = new Timeline();

            KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
            KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
            KeyValue yKV = new KeyValue(translate.yProperty(),  - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence = new SequentialTransition(timeline);
        }
        else {
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15 * Math.abs(boardCells.get(endPos) - boardCells.get(startPos))),
                            new KeyValue(translate.yProperty(), trasl.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate.xProperty(), trasl.getX());
            KeyValue zKV = new KeyValue(translate.zProperty(), trasl.getZ());
            KeyValue yKV = new KeyValue(translate.yProperty(), - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence = new SequentialTransition(timeline2, timeline1);
        }

        sequence.setOnFinished(event -> System.out.println("animation finished: "+sequence.statusProperty().toString()+" "+sequence.getStatus()));

        sequence.play();




        //workers.remove(worker);
        workers.replace(worker, new Point3D(endPos.getX(), endPos.getY(), boardCells.get(endPos)));
        workers.keySet().forEach(w -> System.out.println(workers.get(w)));
        System.out.println(startPos + "  to  " + workers.get(worker));
    }

    /**
     * Move two workers at the same time.
     * @param startPos1 starting position of the 1st worker
     * @param startPos2 starting position of the 2nd worker
     * @param endPos1 ending position of the 1st worker
     * @param endPos2 ending position of the 2nd worker
     */
    public void swapWorkers(Point2D startPos1, Point2D startPos2, Point2D endPos1, Point2D endPos2){
        Optional<Group> optionalWorker1 = workers.keySet().stream()
                .filter(w -> new Point2D(workers.get(w).getX(), workers.get(w).getY()).equals(startPos1))
                .findAny();

        if (! optionalWorker1.isPresent()) {
            System.out.println("worker 1 non trovato");
            return;
        }

        Optional<Group> optionalWorker2 = workers.keySet().stream()
                .filter(w -> new Point2D(workers.get(w).getX(), workers.get(w).getY()).equals(startPos2))
                .findAny();

        if (! optionalWorker2.isPresent()) {
            System.out.println("worker 2 non trovato");
            return;
        }

        System.out.println("worker1: " + startPos1 + endPos1);
        System.out.println("worker2: " + startPos2 + endPos2);


/*
*  Group worker1 = optionalWorker1.get();

        Point3D prev1 = new Point3D(Point2DMap.get(new Point2D(workers.get(worker1).getX(), workers.get(worker1).getY())).getX(), cellHeight.get((int) workers.get(worker1).getZ()), Point2DMap.get(new Point2D(workers.get(worker1).getX(), workers.get(worker1).getY())).getY());

        Point3D next1 = new Point3D(Point2DMap.get(endPos1).getX(), cellHeight.get(boardCells.get(endPos1)), Point2DMap.get(endPos1).getY());

        Point3D trasl1 = next1.subtract(prev1);
        *
        * */


        Group worker1 = optionalWorker1.get();

        Point3D prev1 = new Point3D(Point2DMap.get(startPos1).getX(), cellHeight.get(boardCells.get(startPos1)), Point2DMap.get(startPos1).getY());

        Point3D next1 = new Point3D(Point2DMap.get(endPos1).getX(), cellHeight.get(boardCells.get(endPos1)), Point2DMap.get(endPos1).getY());

        Point3D trasl1 = next1.subtract(prev1);


        Group worker2 = optionalWorker2.get();

        Point3D prev2 = new Point3D(Point2DMap.get(startPos2).getX(), cellHeight.get(boardCells.get(startPos2)), Point2DMap.get(startPos2).getY());

        Point3D next2 = new Point3D(Point2DMap.get(endPos2).getX(), cellHeight.get(boardCells.get(endPos2)), Point2DMap.get(endPos2).getY());

        Point3D trasl2 = next2.subtract(prev2);




        Translate translate1 = new Translate();
        worker1.getTransforms().add(translate1);


        SequentialTransition sequence1;

        if (boardCells.get(endPos1) - boardCells.get(startPos1) >= 1) {
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15 * (boardCells.get(endPos1) - boardCells.get(startPos1))),
                            new KeyValue(translate1.yProperty(), trasl1.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate1.xProperty(), trasl1.getX());
            KeyValue zKV = new KeyValue(translate1.zProperty(), trasl1.getZ());
            KeyValue yKV = new KeyValue(translate1.yProperty(), trasl1.getY() - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence1 = new SequentialTransition(timeline1, timeline2);
        } else if (boardCells.get(endPos1) - boardCells.get(startPos1) == 0) {
            Timeline timeline = new Timeline();

            KeyValue xKV = new KeyValue(translate1.xProperty(), trasl1.getX());
            KeyValue zKV = new KeyValue(translate1.zProperty(), trasl1.getZ());
            KeyValue yKV = new KeyValue(translate1.yProperty(), trasl1.getY() - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence1 = new SequentialTransition(timeline);
        } else {
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15 * Math.abs(boardCells.get(endPos1) - boardCells.get(startPos1))),
                            new KeyValue(translate1.yProperty(), trasl1.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate1.xProperty(), trasl1.getX());
            KeyValue zKV = new KeyValue(translate1.zProperty(), trasl1.getZ());
            KeyValue yKV = new KeyValue(translate1.yProperty(), - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence1 = new SequentialTransition(timeline2, timeline1);
        }

        sequence1.play();
        sequence1.setOnFinished(event -> System.out.println("animation finished: "+sequence1.statusProperty().toString()+" "+sequence1.getStatus()));


        //workers.remove(worker1);
        workers.put(worker1, new Point3D(endPos1.getX(), endPos1.getY(), boardCells.get(endPos1)));

        System.out.println(startPos1 + "  to  " + workers.get(worker1));


        Translate translate2 = new Translate();
        worker2.getTransforms().add(translate2);


        SequentialTransition sequence2;

        if (boardCells.get(endPos2) - boardCells.get(startPos2) >= 1) {
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15 * (boardCells.get(endPos2) - boardCells.get(startPos2))),
                            new KeyValue(translate2.yProperty(), trasl2.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate2.xProperty(), trasl2.getX());
            KeyValue zKV = new KeyValue(translate2.zProperty(), trasl2.getZ());
            KeyValue yKV = new KeyValue(translate2.yProperty(), trasl2.getY() - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence2 = new SequentialTransition(timeline1, timeline2);
        } else if (boardCells.get(endPos2) - boardCells.get(startPos2) == 0) {
            Timeline timeline = new Timeline();

            KeyValue xKV = new KeyValue(translate2.xProperty(), trasl2.getX());
            KeyValue zKV = new KeyValue(translate2.zProperty(), trasl2.getZ());
            KeyValue yKV = new KeyValue(translate2.yProperty(), trasl2.getY() - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence2 = new SequentialTransition(timeline);
        } else {
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(.15 * Math.abs(boardCells.get(endPos2) - boardCells.get(startPos2))),
                            new KeyValue(translate2.yProperty(), trasl2.getY())
                    )
            );

            Timeline timeline2 = new Timeline();
            KeyValue xKV = new KeyValue(translate2.xProperty(), trasl2.getX());
            KeyValue zKV = new KeyValue(translate2.zProperty(), trasl2.getZ());
            KeyValue yKV = new KeyValue(translate2.yProperty(), - 1, new Interpolator() {
                @Override
                protected double curve(double t){
                    return - 4 * (t - .5) * (t - .5) + 1;
                }
            });
            KeyFrame xKF = new KeyFrame(Duration.seconds(0.3), xKV);
            KeyFrame yKF = new KeyFrame(Duration.seconds(0.3), yKV);
            KeyFrame zKF = new KeyFrame(Duration.seconds(0.3), zKV);
            timeline2.getKeyFrames().addAll(xKF, yKF, zKF);

            sequence2 = new SequentialTransition(timeline2, timeline1);
        }

        sequence2.play();

        sequence2.setOnFinished(event -> System.out.println("animation finished: "+sequence2.statusProperty().toString()+" "+sequence2.getStatus()));


        //workers.remove(worker2);
        workers.put(worker2, new Point3D(endPos2.getX(), endPos2.getY(), boardCells.get(endPos2)));
        System.out.println(startPos2 + "  to  " + workers.get(worker2));


    }


    /**
     * Remove the worker in a certain position from the billboard
     * @param pos the position on which the worker is situated
     */
    public void removeWorker(Point2D pos){
        Optional<Group> optionalWorker =  workers.keySet().stream()
                .filter(w -> new Point2D(workers.get(w).getX(), workers.get(w).getY()).equals(pos))
                .findAny();

        if (!optionalWorker.isPresent()) {
            System.out.println("worker non trovato");
            return;
        }

        Group worker = optionalWorker.get();
        workers.remove(worker);
        Platform.runLater(() -> group.getChildren().remove(worker));
    }

    /**
     * Start the animation of ending at the end of the game
     */
    public void endAnimation(){

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0.0001),
                        new KeyValue(angleX, 30)
                )
        );
        //timeline.play();

        Timeline timeline2 = new Timeline(
                new KeyFrame(
                        Duration.seconds(60),
                        new KeyValue(angleY, 1080)
                )
        );
        //timeline2.play();

        sequentialTransition = new SequentialTransition(timeline, timeline2);
        sequentialTransition.play();

    }

    /**
     * Show a yellow halo on every position of the set
     * @param positionSet the set of the positions to glow
     */
    public void showCells(Set<Position> positionSet){
        Platform.runLater( () -> {
            if(cells.size() != 0)
                cells.forEach(cell -> group.getChildren().remove(cell));
            cells = new HashSet<>();
            if (positionSet == null){
                return;
            }
            positionSet.stream().map(Running::positionToPoint).forEach(point2D -> {
                Group cell = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/pose.obj"));

                cell.setTranslateX(Point2DMap.get(point2D).getX());
                cell.setTranslateY(cellHeight.get(boardCells.get(point2D))-0.3);
                cell.setTranslateZ(Point2DMap.get(point2D).getY());
                cell.setMouseTransparent(true);

                cells.add(cell);

                //illuminare???
            });
            cells.forEach(cell -> group.getChildren().add(cell));
        });





    }

    /**
     * Hide the arrow which indicates a certain worker selected
     */
    public void hideArrow(){
        Platform.runLater(() -> group.getChildren().remove(arrow));
    }

    /**
     * Shows an arrow on the selected worker
     * @param color the color of the selected worker
     * @param point2D the position of the selected worker
     */
    public void showArrow(String color, Point2D point2D){
        Group temp = loadModel(IslandLoader.class.getClassLoader().getResource("3D_files/arrow_"+color.toLowerCase()+".obj"));
        //arrow.setTranslateX(-arrow.localToScene(arrow.getBoundsInLocal()).getMaxX()+arrow.localToScene(arrow.getBoundsInLocal()).getWidth()/2);
        //arrow.setTranslateY(-arrow.localToScene(arrow.getBoundsInLocal()).getMaxY());
        //arrow.setTranslateZ(-arrow.localToScene(arrow.getBoundsInLocal()).getMaxZ()+arrow.localToScene(arrow.getBoundsInLocal()).getDepth()/2);

        temp.setTranslateX(Point2DMap.get(point2D).getX());
        temp.setTranslateY(cellHeight.get(boardCells.get(point2D)));
        temp.setTranslateZ(Point2DMap.get(point2D).getY());

        Platform.runLater(() -> {
            group.getChildren().remove(arrow);
            arrow = temp;
            group.getChildren().add(arrow);
            RotateTransition rotate1 = new RotateTransition();
            rotate1.setAxis(Rotate.Y_AXIS);
            // setting the angle of rotation
            rotate1.setByAngle(720);
            //Setting duration of the transition
            rotate1.setDuration(Duration.seconds(10));
            rotate1.setNode(arrow);
            rotate1.setCycleCount(- 1);
            rotate1.play();
        });
    }

    /**
     * Moves a worker up, in the same position.
     * @param point where the worker is situated.
     */
    public void moveUp(Point2D point){
        Optional<Group> optionalWorker =  workers.keySet().stream()
                .filter(w -> new Point2D(workers.get(w).getX(), workers.get(w).getY()).equals(point))
                .findAny();

        if (!optionalWorker.isPresent()){
            System.out.println("worker non trovato");
            return;
        }

        Group worker = optionalWorker.get();


        //Point3D prev = new Point3D(Point2DMap.get(point).getX(), cellHeight.get(boardCells.get(point)), Point2DMap.get(point).getY());

        //Point3D next = new Point3D(Point2DMap.get(point).getX(), cellHeight.get(boardCells.get(point)+1), Point2DMap.get(point).getY());

        //Point3D trasl = next.subtract(prev);

        double slope;

        switch(boardCells.get(point)){
            case 1:
                slope = -1.2;
                break;
            case 2:
                slope = -1.3;
                break;
            case 3:
                slope = -0.8;
                break;
            default:
                return;
        }

        Translate translate = new Translate();
        worker.getTransforms().add(translate);


        Timeline timeline = new Timeline();
        KeyValue yKV = new KeyValue(translate.yProperty(),  slope);
        KeyFrame yKF = new KeyFrame(Duration.seconds(1), yKV);
        timeline.getKeyFrames().addAll(yKF);

        timeline.play();

        workers.replace(worker, new Point3D(point.getX(), point.getY(), boardCells.get(point)+1));
    }
}





