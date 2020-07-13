package com.vztekoverflow.lospiratos.webapp;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShipDetail {
    public com.vztekoverflow.lospiratos.model.Ship ship;
    public List<String> plannableActions;
    public List<String> visibleActions;
    public List<String> plannedActions;

    public ShipDetail(Ship s) {
        ship = s.getShipModel();


        List<Action> relatedActions = Helpers.allActionsRelatedToShip(s);
        plannableActions = relatedActions.stream().filter(Action::getPlannable).map(x -> x.getClass().getSimpleName()).collect(Collectors.toList());
        visibleActions = relatedActions.stream().filter(Action::getVisible).map(x -> x.getClass().getSimpleName()).collect(Collectors.toList());

        plannedActions = s.getPlannedActions().stream().map(x -> x.getClass().getSimpleName()).collect(Collectors.toList());

    }

    public static byte[] getJson(HttpExchange exchange, Game g)
    {

        //Todo security

        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        String shipId = params.get("ship").get(0);
        String teamId = params.get("team").get(0);

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().get();
        Ship s = t.getShips().values().stream().filter(x -> x.getShipModel().getId().equals(shipId)).findFirst().get();


        ShipDetail shipDetail = new ShipDetail(s);

        Gson gson = new Gson();
        String jsonString = gson.toJson(shipDetail);



        return jsonString.getBytes(StandardCharsets.UTF_8);



    }
}
