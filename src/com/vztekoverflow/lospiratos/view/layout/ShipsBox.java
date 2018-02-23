package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.Main;
import com.vztekoverflow.lospiratos.view.controls.OnCenterShipListener;
import com.vztekoverflow.lospiratos.view.controls.OnShipDetailsListener;
import com.vztekoverflow.lospiratos.view.controls.ShipView;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;

public class ShipsBox extends FlowPane {

    private HashMap<Ship, ShipView> shipViews = new HashMap<>();
    private Game game;

    private OnCenterShipListener onCenterShipListener = null;

    public void setOnCenterShipListener(OnCenterShipListener onCenterShipListener) {
        this.onCenterShipListener = onCenterShipListener;
        for (ShipView tv : shipViews.values()) {
            tv.setOnCenterShipListener(onCenterShipListener);
        }
    }


    private OnShipDetailsListener onShipDetailsListener = null;

    public void setOnShipDetailsListener(OnShipDetailsListener onShipDetailsListener) {
        this.onShipDetailsListener = onShipDetailsListener;
        for (ShipView tv : shipViews.values()) {
            tv.setOnShipDetailsListener(onShipDetailsListener);
        }
    }

    public void bindToGame(Game game) {
        this.game = game;
        getChildren().clear();
        shipViews.clear();

        for (final Ship s : game.getAllShips()) {
            Main.viewCreator.submit(() -> addShipView(s));
        }

        game.getAllShips().addListener((ListChangeListener<? super Ship>) c -> {
            while (c.next()) {
                c.getRemoved().forEach(this::removeShipView);
                c.getAddedSubList().forEach(s ->
                        Main.viewCreator.submit(() -> addShipView(s)
                        ));
            }
        });
    }

    private void addShipView(Ship s) {
        ShipView sv = new ShipView(s);
        FlowPane.setMargin(sv, new Insets(2, 2, 2, 2));
        sv.setRequestDeleteListener(ship -> s.getTeam().removeShip(s.getName()));

        Platform.runLater(() -> {
            getChildren().add(sv);
//            getChildren().sort(
//                    Comparator.comparing(n -> ((ShipView) n).getShip().getTeam().getName())
//            );
        });
        sv.setOnCenterShipListener(onCenterShipListener);
        sv.setOnShipDetailsListener(onShipDetailsListener);

        shipViews.put(s, sv);
    }

    private void removeShipView(Ship s) {
        getChildren().remove(shipViews.get(s));
        shipViews.remove(s);
    }

    public ShipView getShipViewFor(Ship s) {
        return shipViews.get(s);
    }
}
