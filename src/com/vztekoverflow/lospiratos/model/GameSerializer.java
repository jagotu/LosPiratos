package com.vztekoverflow.lospiratos.model;

import com.google.gson.Gson;
import org.hildan.fxgson.FxGson;

import java.io.*;


public class GameSerializer {
    /*
     * @throws GsonExceptions
     * @returns null if the loading was unsuccessful due to IOError
     */
    public static Game LoadGameFromFile(String inputFilePath){

        //TODO jak reagovat na JsonException? odchytat a vracet null?
        Game game;
        Gson gson = FxGson.create();
        Reader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFilePath));
            game = gson.fromJson(reader, Game.class);
        }catch (IOException e){
            return null;
        }finally {
            try{
                if(reader != null) {
                    reader.close();
                }
            }catch (IOException e){
                return  null;
            }
        }
        return game;
    }

    /*
     * @returns boolean indicating whether the game has been successfully saved
     */
    public static boolean SaveGameToFile(String outputFilePath, Game game, boolean append){
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().create();
        String gameInGson = gson.toJson(game);

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(outputFilePath, append));
            writer.write(gameInGson);

        }catch (IOException e){
            return false;
        }finally {
            try{
                if(writer != null) {
                    writer.close();
                }
            }catch (IOException e){
                return  false;
            }
        }
        return true;
    }

    /*
     * @throws GsonExceptions
     */
    public static Game GameFromJson(String inputJson){
        //TODO jak reagovat na JsonException? odchytat a vracet null?
        Gson gson = FxGson.create();
        return gson.fromJson(inputJson, Game.class);
    }
    public static String JsonFromGame(Game game){
        Gson gson = FxGson.coreBuilder().setPrettyPrinting().create();
        return gson.toJson(game);
    }
    public static String JsonFromGame(Game game, boolean allOnOneLine){
        Gson gson;
        if(allOnOneLine){
         gson = FxGson.create();
        }
        else{
         gson = FxGson.coreBuilder().setPrettyPrinting().create();
        }
        return gson.toJson(game);
    }
}
