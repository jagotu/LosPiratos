package com.vztekoverflow.lospiratos.webapp;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.model.ResourceM;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.BuyNewEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.RepairShip;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.RepairShipViaRepayment;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
import org.hildan.fxgson.FxGson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AvailableEnhancements {



    public static byte[] getJson(HttpExchange exchange, Game g, String teamToken) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Map<String, List<String>> params = Helpers.splitQuery(exchange.getRequestURI());
        Helpers.assertPresentOnce(params, "ship", "team");
        String shipId = params.get("ship").get(0);
        String teamId = params.get("team").get(0);

        Auth.assertAuthTeam(teamId, teamToken);

        Team t = g.getTeams().stream().filter(x -> x.getTeamModel().getId().equals(teamId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException("Team with id not found: " + teamId);
        });

        Ship s = t.getShips().values().stream().filter(x -> x.getShipModel().getId().equals(shipId)).findFirst().orElseThrow(() -> {
            return new WebAppServer.FriendlyException(String.format("Team %s has no ship %s", teamId, shipId));
        });

        BuyNewEnhancement bne = new BuyNewEnhancement();
        bne.setRelatedShip(s);


        List<String> availableEnhancements = EnhancementsCatalog.allPossibleEnhancements.stream().filter(x -> EnhancementsCatalog.isAcquirableBy(x, s.getShipType()))
        .map(EnhancementsCatalog::getPersistentName).collect(Collectors.toList());


        Gson gson = FxGson.create();
        String jsonString = gson.toJson( availableEnhancements);


        return jsonString.getBytes(StandardCharsets.UTF_8);


    }
}
