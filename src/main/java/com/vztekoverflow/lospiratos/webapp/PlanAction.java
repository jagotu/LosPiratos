package com.vztekoverflow.lospiratos.webapp;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import org.hildan.fxgson.FxGson;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class PlanAction {

    public static byte[] doit(HttpExchange exchange, Game g) {

        //Todo security

        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        Helpers.assertPresentOnce(params, "ship", "team");
        String shipId = params.get("ship").get(0);
        String teamId = params.get("team").get(0);

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().get();
        Ship s = t.getShips().values().stream().filter(x -> x.getShipModel().getId().equals(shipId)).findFirst().get();


        ShipDetail shipDetail = new ShipDetail(s);

        Gson gson = FxGson.create();
        String jsonString = gson.toJson(shipDetail);



        return jsonString.getBytes(StandardCharsets.UTF_8);



    }
}
