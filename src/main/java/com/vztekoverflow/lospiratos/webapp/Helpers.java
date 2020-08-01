package com.vztekoverflow.lospiratos.webapp;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.*;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.*;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.MoveForward;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.TurnLeft;
import com.vztekoverflow.lospiratos.viewmodel.actions.maneuvers.TurnRight;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.*;

import javax.json.JsonObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class Helpers {


    public static List<Action> allActionsRelatedToShip(Ship s)
    {
        List<Action> allActions = new ArrayList<>();
        allActions.add(new FrontalAssault());
        allActions.add(new CannonsSimpleVolley(false));
        allActions.add(new CannonsHeavyBallVolley(false));
        allActions.add(new CannonsChainShotVolley(false));
        allActions.add(new MortarShot());
        allActions.add(new CannonsChainShotVolley(true));
        allActions.add(new CannonsHeavyBallVolley(true));
        allActions.add(new CannonsSimpleVolley(true));


        allActions.add(new UnloadStorage());
        allActions.add(new BuyCommodity());
        allActions.add(new SellCommodity());
        allActions.add(new BuyNewEnhancement());
        allActions.add(new RepairEnhancement());
        allActions.add(new RepairShipViaDowngrade());
        allActions.add(new RepairShipViaRepayment());
        allActions.add(new UpgradeShip());

        allActions.add(new MoveForward());
        allActions.add(new TurnRight());
        allActions.add(new Plunder());
        allActions.add(new TurnLeft());


        for(Action a : allActions)
        {
            a.setRelatedShip(s);
        }
        return allActions;
    }


    public static Map<String, List<String>> splitQuery(URI uri) {

        return splitUrlEncodedParams(uri.getRawQuery());

    }

    public static Map<String, List<String>> splitUrlEncodedParams(String urlEncodedParams)
    {
        if (urlEncodedParams == null || urlEncodedParams.isEmpty()) {
            return Collections.emptyMap();
        }

        return Arrays.stream(urlEncodedParams.split("&"))
                .map(Helpers::splitQueryParameter)
                .collect(Collectors.groupingBy(AbstractMap.SimpleImmutableEntry::getKey, LinkedHashMap::new, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    public static AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        try {
            return new AbstractMap.SimpleImmutableEntry<>(
                    URLDecoder.decode(key, "UTF-8"),
                    URLDecoder.decode(value, "UTF-8")
            );
        } catch (UnsupportedEncodingException e) {
            return new AbstractMap.SimpleImmutableEntry<>(
                    key, value
            );
        }
    }

    public static boolean isPresentOnce(Map<String, List<String>> params, String value)
    {
        return params.containsKey(value) && params.get(value).size() == 1;
    }

    public static void assertPresentOnce(Map<String, List<String>> params, String... values)
    {
        for(String value : values)
        {
            if(!isPresentOnce(params, value))
                throw new WebAppServer.FriendlyException("Required parameter missing or has multiple values: " + value);
        }
    }

    public static void assertHasAll(JsonObject json, String... values)
    {
        for(String value : values)
        {
            if(!json.containsKey(value))
                throw new WebAppServer.FriendlyException("Required parameter missing: " + value);
        }
    }

    public static void assertHasAll(JsonObject json, List<String> values)
    {
        for(String value : values)
        {
            if(!json.containsKey(value))
                throw new WebAppServer.FriendlyException("Required parameter missing: " + value);
        }
    }

    public static byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }

    public static String getActionName(Action a) {
        if (a instanceof CannonsAbstractVolley) {
            return (((CannonsAbstractVolley) a).isUseLeftCannons() ? "Left" : "Right") + a.getClass().getSimpleName();
        }
        return a.getClass().getSimpleName();
    }

}
