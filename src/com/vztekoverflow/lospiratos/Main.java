package com.vztekoverflow.lospiratos;

import com.vztekoverflow.lospiratos.model.Game;
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



    private Game g = Game.CreateNewMockGame();
    private com.vztekoverflow.lospiratos.viewmodel.Game gv = com.vztekoverflow.lospiratos.viewmodel.Game.LoadFromModel(g);



    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(OrgStageController.class.getResource("OrgStage.fxml"));

        loader.setController(new OrgStageController(gv));

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
