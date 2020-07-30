package com.vztekoverflow.lospiratos.webapp;

import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Resource;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.EnhancementActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.ResourceActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Port;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipType;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
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

import static java.nio.charset.StandardCharsets.UTF_8;

public class CreateShip {

    public static byte[] doit(HttpExchange exchange, Game g, String teamToken, String postData) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {

        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        Helpers.assertPresentOnce(params, "team");
        String teamId = params.get("team").get(0);

        Auth.assertAuthTeam(teamId, teamToken);

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("Team with id not found: " + teamId);
        });

        JsonReader reader = Json.createReader(new StringReader(postData));
        JsonObject jobj = reader.readObject();

        Helpers.assertHasAll(jobj, "shipName", "type", "port");

        String shipName = jobj.getString("shipName");
        String type = jobj.getString("type");
        String port = jobj.getString("port");

        Port p = (Port)g.getBoard().getTiles().values().stream().filter(x -> x instanceof Port && ((Port)x).getPortName().equals(port)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("Port not found: " + port);
        });

        Class<? extends ShipType> shipType = ShipType.createInstanceFromPersistentName(type).getClass();



        AsyncWrapper wr = new AsyncWrapper();

        Platform.runLater(() ->
        {
            Ship s = t.buyNewShip(shipType, shipName, "unused", p.getLocation());
            if(s == null)
            {
                wr.setResult("bad");
            } else {
                wr.setResult("");
            }
        });

        while (!wr.isDone()) {
            synchronized (wr.getNotifier()) {
                wr.getNotifier().wait();
            }

        }

        if(wr.getResult().equals("bad"))
        {
            throw new WebAppServer.FriendlyException("Nákup lodi se nezdařil. Máte dost prostředků?");
        }


        return new byte[0];


    }
}
