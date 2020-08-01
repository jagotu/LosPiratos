package com.vztekoverflow.lospiratos.webapp;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.application.Platform;
import org.hildan.fxgson.FxGson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TeamIsReady {
    public static final HashSet<String> readyTeams = new HashSet<>();

    public static byte[] getJson(HttpExchange exchange, Game g, String teamToken) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        Helpers.assertPresentOnce(params, "team");
        String teamId = params.get("team").get(0);

        Auth.assertAuthTeam(teamId, teamToken);

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("Team with id not found: " + teamId);
        });

        return Boolean.toString(readyTeams.contains(t.getTeamModel().getId())).getBytes(UTF_8);

    }

    public static byte[] set(HttpExchange exchange, Game g, String teamToken, String postData) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        Helpers.assertPresentOnce(params, "team");
        String teamId = params.get("team").get(0);

        Auth.assertAuthTeam(teamId, teamToken);

        if(!postData.equals(Integer.toString(g.getRoundNo())))
        {
            throw new WebAppServer.FriendlyException("Od pokusu o nastavení připravenosti uběhlo kolo.");
        }

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("Team with id not found: " + teamId);
        });

        synchronized (readyTeams)
        {
            if(!readyTeams.contains(t.getTeamModel().getId()))
            {
                readyTeams.add(teamId);
                if(readyTeams.size() == g.getTeams().size())
                {
                    Platform.runLater(g::closeRoundAndEvaluate);
                }
            }

        }

        return "OK".getBytes(UTF_8);

    }

}
