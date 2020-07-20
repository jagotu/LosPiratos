package com.vztekoverflow.lospiratos.webapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.logs.LogFormatter;
import javafx.application.Platform;
import org.hildan.fxgson.FxGson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CombatLog {

    public static byte[] getJson(HttpExchange exchange, Game g) throws InterruptedException {

        AsyncWrapper wr = new AsyncWrapper();

        Platform.runLater(() ->
        {
            List<String> logEntries = g.getLogger().getLoggedEvents().stream().map(x -> x.getTextualDescription(LogFormatter.hezkyČesky())).collect(Collectors.toList());

            Gson gson=new GsonBuilder().create();
            wr.setResult(gson.toJson(logEntries));
        });

        while(!wr.isDone())
        {
            synchronized (wr.getNotifier())
            {
                wr.getNotifier().wait();
            }

        }

        return wr.getResult().getBytes(StandardCharsets.UTF_8);



    }
}
