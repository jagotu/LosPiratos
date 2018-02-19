package com.vztekoverflow.lospiratos.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.vztekoverflow.lospiratos.util.Warnings;
import org.hildan.fxgson.FxGson;

import java.io.*;


public class GameSerializer {
    /**
     * @return null if the loading was unsuccessful due to IOError or invalid JSON
     */
    public static Game LoadGameFromFile(File inputFile) {

        Game game = null;
        Gson gson = FxGson.create();

        try (Reader f = new FileReader(inputFile); Reader reader = new BufferedReader(f)) {
            game = gson.fromJson(reader, Game.class);
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
            Warnings.makeWarning("Game.LoadGameFromFile", e.getMessage());
        }
        return game;
    }

    /**
     * @return boolean indicating whether the game has been successfully saved
     */
    public static boolean SaveGameToFile(File outputFile, Game game, boolean append) {
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().create();
        String gameInGson;
        try {
            gameInGson = gson.toJson(game);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return false;
        }

        try (Writer f = new FileWriter(outputFile, append); Writer writer = new BufferedWriter(f)) {
            writer.write(gameInGson);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return null if the game could not be loaded
     */
    public static Game GameFromJson(String inputJson) {
        Game g;
        try {
            Gson gson = FxGson.create();
            g = gson.fromJson(inputJson, Game.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        }
        return g;
    }

    public static String JsonFromGame(Game game) {
        return JsonFromGame(game, false);
    }

    public static String JsonFromGame(Game game, boolean allOnOneLine) {
        Gson gson;
        if (allOnOneLine) {
            gson = FxGson.create();
        } else {
            gson = FxGson.coreBuilder().setPrettyPrinting().create();
        }
        return gson.toJson(game);
    }
}
