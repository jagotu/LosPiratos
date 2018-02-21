package com.vztekoverflow.lospiratos;

import com.vztekoverflow.lospiratos.view.stages.OrgStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static final ExecutorService viewCreator = Executors.newSingleThreadExecutor(r -> {
        Thread thread = Executors.defaultThreadFactory().newThread(r);
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler((t, e) -> e.printStackTrace());
        return thread;
    });



    @Override
    public void start(Stage primaryStage) throws Exception{
        InputStream inputStream = getClass().getResourceAsStream("/fontawesome-webfont.ttf");
        FontAwesome fa = new FontAwesome(inputStream);
        GlyphFontRegistry.register(fa);

        inputStream = getClass().getResourceAsStream("/piratos.ttf");
        GlyphFont piratosFont = new GlyphFont("piratos", 16, inputStream);
        GlyphFontRegistry.register(piratosFont);

        inputStream = getClass().getResourceAsStream("/piratostrans.ttf");
        GlyphFont piratosTransFont = new GlyphFont("piratostrans", 16, inputStream);
        GlyphFontRegistry.register(piratosTransFont);

        Parent root = FXMLLoader.load(OrgStage.class.getResource("OrgStage.fxml"));
        primaryStage.setTitle("OrgStage");
        primaryStage.setScene(new Scene(root, 1024, 600));
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();


    }



    public static void main(String[] args) {
        launch(args);
    }
}
