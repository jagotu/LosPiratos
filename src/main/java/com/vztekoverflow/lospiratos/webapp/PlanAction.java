package com.vztekoverflow.lospiratos.webapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.Plunder;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.ResourceActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.logs.LogFormatter;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import javafx.application.Platform;
import org.hildan.fxgson.FxGson;

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

public class PlanAction {

    public static byte[] doit(HttpExchange exchange, Game g, String teamToken, String postData, boolean commit) throws NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {

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

        JsonReader reader = Json.createReader(new StringReader(postData));
        JsonObject jobj = reader.readObject();

        Helpers.assertHasAll(jobj, "action", "actionPayload");
        String action = jobj.getString("action");

        Action a = Helpers.allActionsRelatedToShip(s).stream().filter(x -> Helpers.getActionName(x).equals(action)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("No such action: " + action);
        });

        JsonObject actionPayload = jobj.getJsonObject("actionPayload");

        if (!a.getPlannable()) {
            throw new WebAppServer.FriendlyException("Action not plannable.");
        }

        if (a instanceof ParameterizedAction) {
            ParameterizedAction pa = (ParameterizedAction) a;

            Helpers.assertHasAll(actionPayload,
                    StreamSupport.stream(pa.getAvailableParameters().spliterator(), false).map(ActionParameter::getJsonMapping).collect(Collectors.toList()));

            for (ActionParameter ap : pa.getAvailableParameters()) {

                JsonObject paramValue = actionPayload.getJsonObject(ap.getJsonMapping());

                if (ap instanceof AxialCoordinateActionParameter) {
                    Helpers.assertHasAll(paramValue, "Q", "R");
                    ap.set(new AxialCoordinate(
                            paramValue.getInt("Q"),
                            paramValue.getInt("R")
                    ));

                } else if (ap instanceof ResourceActionParameter) {
                    Resource r = ((ResourceActionParameter) ap).get();
                    if (paramValue.containsKey("money")) {
                        r.setMoney(paramValue.getInt("money"));
                    }
                    if (paramValue.containsKey("metal")) {
                        r.setMetal(paramValue.getInt("metal"));
                    }
                    if (paramValue.containsKey("wood")) {
                        r.setWood(paramValue.getInt("wood"));
                    }
                    if (paramValue.containsKey("cloth")) {
                        r.setCloth(paramValue.getInt("cloth"));
                    }
                    if (paramValue.containsKey("rum")) {
                        r.setRum(paramValue.getInt("rum"));
                    }

                } else if (ap instanceof EnhancementActionParameter) {
                    Helpers.assertHasAll(paramValue, "enhancement");
                    String enhancement = paramValue.getString("enhancement");
                    Class<? extends ShipEnhancement> enhancementClass = EnhancementsCatalog.allPossibleEnhancements.stream()
                            .filter(x -> EnhancementsCatalog.getPersistentName(x).equals(enhancement))
                            .findFirst().orElseThrow(() -> new WebAppServer.FriendlyException("No such enhancement: " + enhancement));

                    ap.set(enhancementClass);
                }

                //TODO others
            }

            if (!pa.isSatisfied()) {
                throw new WebAppServer.FriendlyException("Action is not satisfied - provided parameters are invalid.");
            }
        }

        AsyncWrapper wr = new AsyncWrapper();

        Platform.runLater(() ->
        {
            s.planAction(a);
            if (commit) {
                s.commitModifyingTransactions();
            }
            wr.setResult("");
        });

        while (!wr.isDone()) {
            synchronized (wr.getNotifier()) {
                wr.getNotifier().wait();
            }

        }

        return wr.getResult().getBytes(StandardCharsets.UTF_8);


    }
}
