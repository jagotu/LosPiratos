package com.vztekoverflow.lospiratos;

import com.vztekoverflow.lospiratos.model.Game;
import com.vztekoverflow.lospiratos.sample.SampleController;
import com.vztekoverflow.lospiratos.util.CubeCoordinateMutable;
import com.vztekoverflow.lospiratos.view.layout.HexTileContents;
import com.vztekoverflow.lospiratos.view.layout.HexTileContentsFactory;
import com.vztekoverflow.lospiratos.view.stages.OrgStageController;
import com.vztekoverflow.lospiratos.view.stages.PublicMapStageController;
import com.vztekoverflow.lospiratos.view.stages.PublicStatStageController;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int SIZE = 8;

    private Game g = Game.CreateNewMockGame();

    private HexTileContentsFactory fact = (coords, tileWidth, tileHeight) -> {

        CubeCoordinateMutable cube = coords.toCubeCoordinate();
        if(cube.getQ() < -SIZE || cube.getQ() > SIZE ||
                cube.getR() < -SIZE || cube.getR() > SIZE ||
                cube.getS() < -SIZE || cube.getS() > SIZE)
        {
            return null;
        }



        final Label l = new Label();
        l.setText(String.format("[%s,%s]", coords.getQ(), coords.getR()));

        final String cssClassName = coords.getR() % 2 == 0 ? "even" : "odd";

        return new HexTileContents() {

            ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(l);
            StringProperty cssClass = new ReadOnlyStringWrapper(cssClassName);

            @Override
            public ObjectProperty<Node> contentsProperty() {
                return contents;
            }

            @Override
            public StringProperty cssClassProperty() {
                return cssClass;
            }


        };
    };


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(OrgStageController.class.getResource("OrgStage.fxml"));
        loader.setController(new OrgStageController(fact));

        Parent orgStageParent = loader.load();
        primaryStage.setTitle("OrgStage");
        primaryStage.setScene(new Scene(orgStageParent, 1200, 600));

        /*Stage publicMapStage = new Stage();
        Parent publicMapStageParent = FXMLLoader.load(PublicMapStageController.class.getResource("PublicMapStage.fxml"));
        publicMapStage.setScene(new Scene(publicMapStageParent,300, 300));
        publicMapStage.setTitle("PublicMapStage");

        Stage publicStatStage = new Stage();
        Parent publicStatStageParent = FXMLLoader.load(PublicStatStageController.class.getResource("PublicStatStage.fxml"));
        publicStatStage.setScene(new Scene(publicStatStageParent, 300, 300));
        publicStatStage.setTitle("PublicStatStage");*/

        primaryStage.show();
        //publicMapStage.show();
        //publicStatStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
