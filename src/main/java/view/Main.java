package view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

    private Controller controller;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("SVG2Image");

        BorderPane borderPane = new BorderPane();
        borderPane.setId("MainPane");
        ToolBar toolBar = new ToolBar();

        int height = 30;
        toolBar.setPrefHeight(height);
        toolBar.setMinHeight(height);
        toolBar.setMaxHeight(height);
        toolBar.setId("MainToolbar");
        Text t = new Text("SVG2Image Converter");
        t.setId("WindowTitle");
        toolBar.getItems().add(t);
        Pane pane = new Pane();
        HBox.setHgrow(pane,Priority.ALWAYS);
        toolBar.getItems().add(pane);
        toolBar.getItems().add(new WindowButtons());

        borderPane.setTop(toolBar);
        borderPane.setCenter(root);
        root.requestFocus();
        scene = new Scene(borderPane, 300, 220);
        scene.getStylesheets().add("style.css");
        scene.setFill(Color.TRANSPARENT);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
        addDraggableNode(toolBar,primaryStage);

        primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("maximized");
                ((Stage)scene.getWindow()).setIconified(false);
                scene.getRoot().setOpacity(1);
                scene.getRoot().setScaleY(1);
            }
        });
    }

    @Override
    public void stop(){
        controller.exitController();
        // Save file
    }

    private double x,y;

    private void addDraggableNode(final Node node, Stage primaryStage) {

        node.setOnMousePressed((MouseEvent mouseEvent) -> {
            this.x = node.getScene().getWindow().getX() - mouseEvent.getScreenX();
            this.y = node.getScene().getWindow().getY() - mouseEvent.getScreenY();
        });

        node.setOnMouseDragged((MouseEvent mouseEvent) -> {
            primaryStage.setX(mouseEvent.getScreenX() + this.x);
            primaryStage.setY(mouseEvent.getScreenY() + this.y);
        });
    }

    class WindowButtons extends HBox {

        boolean infoOpen = false;
        ContextMenu contextMenu = new ContextMenu();

        public WindowButtons() {
            Button closeBtn = new Button("X");
            Button minBtn = new Button("_");
            Button infoBtn = new Button("?");
            closeBtn.getStyleClass().add("WinButton");
            minBtn.getStyleClass().add("WinButton");
            infoBtn.getStyleClass().add("WinButton");

            MenuItem titleItem = new MenuItem("SVG2Image");
            //titleItem.setDisable(true);
            titleItem.getStyleClass().add("contextTitle");
            contextMenu.getItems().add(titleItem);
            //contextMenu.getItems().add(new MenuItem(controller.getInfo()));
            contextMenu.getStyleClass().add("contextMenu");

            MenuItem verItem = new MenuItem("converts SVG files to Images");
            verItem.getStyleClass().add("verTitle");
            contextMenu.getItems().add(verItem);

            closeBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    Timeline timeline = new Timeline();
                    KeyFrame kf = new KeyFrame(Duration.millis(50),
                            new KeyValue(scene.getRoot().opacityProperty(), 0)
                    );
                    timeline.getKeyFrames().add(kf);
                    timeline.setOnFinished((ae) -> Platform.exit());
                    timeline.play();

                }
            });

            minBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    Timeline timeline = new Timeline();
                    KeyFrame kf = new KeyFrame(Duration.millis(100),
                            new KeyValue (scene.getRoot().opacityProperty(), 0.2f),
                            new KeyValue (scene.getRoot().scaleYProperty(), 0.2f)
                    );

                    timeline.getKeyFrames().add(kf);

                    timeline.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ((Stage)getScene().getWindow()).setIconified(true);
                            scene.getRoot().setOpacity(1);
                            scene.getRoot().setScaleY(1);
                        }
                    });

                    //timeline.setOnFinished((ae) -> ((Stage)getScene().getWindow()).setIconified(true));
                    timeline.play();

                }
            });

            infoBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!contextMenu.isShowing()){
                        contextMenu.show(infoBtn,event.getScreenX(),event.getScreenY());
                    }
                }
            });

            this.getChildren().add(minBtn);
            this.getChildren().add(infoBtn);
            this.getChildren().add(closeBtn);

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
