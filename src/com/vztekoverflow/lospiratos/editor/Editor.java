package com.vztekoverflow.lospiratos.editor;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.layout.VirtualizingHexGridPane;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Editor extends Application {

    public VBox rightPanel;
    public TextField imageURL;
    public Slider scaleSlide;
    public TextField scaleTxt;
    public Slider rotateSlide;
    public TextField rotateTxt;
    public Slider translateYSlide;
    public TextField translateYTxt;
    public TextField translateXTxt;
    public Slider translateXSlide;
    public CheckBox clipped;
    private VirtualizingHexGridPane hexPane;
    public BorderPane root;

    private Point2D lastMouse;
    private ObjectProperty<EditorHexTileContents> selected = new SimpleObjectProperty<>();
    private HashMap<AxialCoordinate, EditorHexTileContents> mapStorage = new HashMap<>();

    @FXML
    public void initialize() {

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open image file...");


        scaleTxt.textProperty().bindBidirectional(scaleSlide.valueProperty(), new NumberStringConverter());
        translateXTxt.textProperty().bindBidirectional(translateXSlide.valueProperty(), new NumberStringConverter());
        translateYTxt.textProperty().bindBidirectional(translateYSlide.valueProperty(), new NumberStringConverter());
        rotateTxt.textProperty().bindBidirectional(rotateSlide.valueProperty(), new NumberStringConverter());

        selected.addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                oldValue.removeClass("selected");
                scaleSlide.valueProperty().unbindBidirectional(oldValue.iv.scaleXProperty());
                translateXSlide.valueProperty().unbindBidirectional(oldValue.iv.translateXProperty());
                translateYSlide.valueProperty().unbindBidirectional(oldValue.iv.translateYProperty());
                rotateSlide.valueProperty().unbindBidirectional(oldValue.iv.rotateProperty());
                clipped.selectedProperty().unbindBidirectional(oldValue.clip);
            }
            if (newValue != null) {
                newValue.addClass("selected");
                imageURL.setText(newValue.imgURL.get());
                scaleSlide.valueProperty().bindBidirectional(newValue.iv.scaleXProperty());
                translateXSlide.valueProperty().bindBidirectional(newValue.iv.translateXProperty());
                translateYSlide.valueProperty().bindBidirectional(newValue.iv.translateYProperty());
                rotateSlide.valueProperty().bindBidirectional(newValue.iv.rotateProperty());
                clipped.selectedProperty().bindBidirectional(newValue.clip);
            } else {
                imageURL.setText(null);
            }
        });


        hexPane = new VirtualizingHexGridPane(40, true, new HexTileContentsFactory() {

            public HexTileContents getContentsFor(AxialCoordinate coords, double tileWidth, double tileHeight) {

                if (mapStorage.containsKey(coords)) {
                    return mapStorage.get(coords);
                }
                final String css = (coords.getR() == 0 && coords.getQ() == 0) ? "origin" : "";
                EditorHexTileContents e = new EditorHexTileContents(css, tileWidth, tileHeight);
                mapStorage.put(coords, e);
                return e;

            }
        });

        Rectangle clipRect = new Rectangle(hexPane.getWidth(), hexPane.getHeight());
        clipRect.widthProperty().bind(hexPane.widthProperty());
        clipRect.heightProperty().bind(hexPane.heightProperty());
        hexPane.setClip(clipRect);
        root.setCenter(hexPane);

        hexPane.setOnMousePressed(MouseEvent -> {
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnMouseDragged(MouseEvent -> {
            hexPane.setXOffset(Math.min(Math.max(hexPane.getXOffset() + (lastMouse.getX() - MouseEvent.getX()) * hexPane.getScale(), -1000), 1000));
            hexPane.setYOffset(Math.min(Math.max(hexPane.getYOffset() + (lastMouse.getY() - MouseEvent.getY()) * hexPane.getScale(), -1000), 1000));
            lastMouse = new Point2D(MouseEvent.getX(), MouseEvent.getY());
        });

        hexPane.setOnScroll(ScrollEvent -> {
            double scale = Math.pow(1.005, -ScrollEvent.getDeltaY());
            double newScale = scale * hexPane.getScale();
            if (newScale > 2) {
                newScale = 2;
                scale = 2 / hexPane.getScale();
            }
            if (newScale < 0.1) {
                newScale = 0.1;
                scale = 0.1 / hexPane.getScale();
            }

            Point2D mouse = new Point2D(hexPane.getXOffset() + (ScrollEvent.getX() * hexPane.getScale()), hexPane.getYOffset() + (ScrollEvent.getY() * hexPane.getScale()));

            hexPane.setXOffset(Math.min(Math.max(mouse.getX() - (mouse.getX() - hexPane.getXOffset()) * scale, -1000), 1000));
            hexPane.setYOffset(Math.min(Math.max(mouse.getY() - (mouse.getY() - hexPane.getYOffset()) * scale, -1000), 1000));
            //hexPane.setYOffset(hexPane.getYOffset() + (hexPane.getHeight() * hexPane.getScale()) * (1.0 - 1/scale) / 2);
            hexPane.setScale(newScale);
        });
    }

    class EditorHexTileContents implements HexTileContents {

        int premadeShipIdx = 0;
        StringProperty imgURL = new SimpleStringProperty();
        StringProperty cssClass = new SimpleStringProperty();
        ArrayList<String> cssClasses = new ArrayList<>();
        ImageView iv = new ImageView();
        ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(iv);
        BooleanProperty clip = new SimpleBooleanProperty(false);

        EditorHexTileContents(String css, double tileWidth, double tileHeight) {
            iv.setFitWidth(tileWidth);
            iv.setFitHeight(tileHeight);
            iv.setPreserveRatio(true);
            imgURL.addListener(Observable -> {
                if (imgURL.get() == null) {
                    iv.setImage(null);
                    return;
                }
                iv.setImage(new Image(imgURL.get()));
            });

            this.iv.scaleXProperty().bindBidirectional(this.iv.scaleYProperty());

            iv.setOnMouseClicked(MouseEvent -> {
                if (MouseEvent.isStillSincePress()) {
                    if (MouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        selected.set(this);
                    } else if (MouseEvent.getButton().equals(MouseButton.SECONDARY)) {

                        switch (premadeShipIdx) {
                            case 0:
                                imgURL.set("img/schooner.png");
                                this.iv.scaleXProperty().setValue(1);
                                break;
                            case 1:
                                imgURL.set("img/brig.png");
                                this.iv.scaleXProperty().setValue(1.1);
                                break;
                            case 2:
                                imgURL.set("img/frigate.png");
                                this.iv.scaleXProperty().setValue(0.9);
                                break;
                            case 3:
                                imgURL.set("img/galleon.png");
                                this.iv.scaleXProperty().setValue(1.39);
                                break;
                            case 4:
                                imgURL.set("lod.png");
                                this.iv.scaleXProperty().setValue(1);
                                break;
                        }
                        premadeShipIdx++;
                        premadeShipIdx %= 5;

                    }

                }
            });
            cssClasses.add(css);
            cssClass.set(String.join(" ", cssClasses));
            clip.addListener((Observable) -> {
                if (clip.get()) {
                    addClass("clipped");
                } else {
                    removeClass("clipped");
                }
            });
        }


        public void removeClass(String toRemove) {
            cssClasses.remove(toRemove);
            cssClass.set(String.join(" ", cssClasses));
        }

        public void addClass(String newClass) {
            cssClasses.add(newClass);
            cssClass.set(String.join(" ", cssClasses));
        }


        @Override
        public ObjectProperty<Node> contentsProperty() {
            return contents;
        }

        @Override
        public StringProperty cssClassProperty() {
            return cssClass;
        }
    }

    ;

    @FXML
    public void returnToOrigin() {
        hexPane.centerInParent(new AxialCoordinate(0, 0));
    }

    @FXML
    public void setImageButton() {
        if (selected.get() != null) {
            selected.get().imgURL.set(imageURL.getText());
        }
    }

    private Stage parentStage;
    FileChooser fileChooser;

    @FXML
    public void loadFileUrl() {

        File file = fileChooser.showOpenDialog(parentStage);
        if (file != null) {
            fileChooser.setInitialDirectory(file.getParentFile());
            imageURL.setText(file.toURI().toString());
        }
    }

    @FXML
    void scaleReset() {
        scaleSlide.setValue(1);
    }

    @FXML
    void translateReset() {
        translateXSlide.setValue(0);
        translateYSlide.setValue(0);
    }

    @FXML
    void rotateReset() {
        rotateSlide.setValue(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Editor.class.getResource("Editor.fxml"));
        parentStage = primaryStage;
        primaryStage.setTitle("Editor");
        primaryStage.setScene(new Scene(root, root.prefWidth(-1), root.prefHeight(-1)));
        primaryStage.setMinHeight(root.minHeight(-1) + 20);
        primaryStage.setMinWidth(root.minWidth(-1) + 20);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}