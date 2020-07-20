package com.vztekoverflow.lospiratos.webapp;

import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import javafx.application.Platform;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DeleteActions {

    public static byte[] doit(HttpExchange exchange, Game g, String teamToken, String postData) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {

        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        Helpers.assertPresentOnce(params, "ship", "team");
        String shipId = params.get("ship").get(0);
        String teamId = params.get("team").get(0);

        Auth.assertAuthTeam(teamId, teamToken);

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("Team with id not found: " + teamId);
        });

        final Ship s = t.getShips().values().stream().filter(x -> x.getShipModel().getId().equals(shipId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException(String.format("Team %s has no ship %s", teamId, shipId));
        });

        int actionsToKeep = Integer.parseInt(postData);

        AsyncWrapper wr = new AsyncWrapper();

        Platform.runLater(() ->
        {
            s.unplanActions(actionsToKeep);
            wr.setResult("");
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
