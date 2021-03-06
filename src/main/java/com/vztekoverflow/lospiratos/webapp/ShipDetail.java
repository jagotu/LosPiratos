package com.vztekoverflow.lospiratos.webapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vztekoverflow.lospiratos.model.ResourceM;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.CannonsAbstractVolley;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.RepairShip;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.RepairShipViaRepayment;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.UpgradeShip;
import org.hildan.fxgson.FxGson;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;

public class ShipDetail {
    public com.vztekoverflow.lospiratos.model.Ship ship;
    public List<String> plannableActions;
    public List<String> visibleActions;
    public List<String> plannedActions;
    public ResourceM upgradeCost;
    public ResourceM repairCost;

    public ShipDetail(Ship s) {
        ship = s.getShipModel();


        List<Action> relatedActions = Helpers.allActionsRelatedToShip(s);
        plannableActions = relatedActions.stream().filter(Action::getPlannable).map(Helpers::getActionName).collect(Collectors.toList());
        visibleActions = relatedActions.stream().filter(Action::getVisible).map(Helpers::getActionName).collect(Collectors.toList());

        plannedActions = s.getPlannedActions().stream().map(Helpers::getActionName).collect(Collectors.toList());

        upgradeCost = new ResourceM(s.getShipType().getUpgradeCost());

        RepairShip rs = new RepairShipViaRepayment();
        rs.setRelatedShip(s);
        repairCost = new ResourceM(rs.getCost());

    }



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


        ShipDetail shipDetail = new ShipDetail(s);

        Gson gson = FxGson.create();
        String jsonString = gson.toJson(shipDetail);


        return jsonString.getBytes(StandardCharsets.UTF_8);


    }
}
