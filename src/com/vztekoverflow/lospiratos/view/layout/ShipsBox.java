package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.Main;
import com.vztekoverflow.lospiratos.view.controls.OnCenterShipListener;
import com.vztekoverflow.lospiratos.view.controls.OnShipDetailsListener;
import com.vztekoverflow.lospiratos.view.controls.ShipView;
import com.vztekoverflow.lospiratos.view.controls.TeamView;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShipsBox extends FlowPane {

    private HashMap<Ship, ShipView> shipViews = new HashMap<>();
    private Game game;

    private OnCenterShipListener onCenterShipListener = null;
    public void setOnCenterShipListener(OnCenterShipListener onCenterShipListener)
    {
        this.onCenterShipListener = onCenterShipListener;
        for(ShipView tv : shipViews.values())
        {
            tv.setOnCenterShipListener(onCenterShipListener);
        }
    }


    private OnShipDetailsListener onShipDetailsListener = null;
    public void setOnShipDetailsListener(OnShipDetailsListener onShipDetailsListener) {
        this.onShipDetailsListener = onShipDetailsListener;
        for(ShipView tv : shipViews.values())
        {
            tv.setOnShipDetailsListener(onShipDetailsListener);
        }
    }

    public void bindToGame(Game game)
    {
        this.game = game;
        getChildren().clear();
        shipViews.clear();

        for (final Ship s : game.getAllShips().values()) {
            Main.viewCreator.submit(() -> addShipView(s));
        }

        game.allShipsProperty().addListener((MapChangeListener<? super String, ? super Ship>) change -> {
            if (change.wasRemoved()) {
                removeShipView(change.getValueRemoved());
            }
            if (change.wasAdded()) {
                Main.viewCreator.submit(() -> addShipView(change.getValueAdded()));
            }

        });
    }

    private void addShipView(Ship s) {
        ShipView sv = new ShipView(s);
        FlowPane.setMargin(sv, new Insets(2, 2, 2, 2));
        sv.setRequestDeleteListener(ship -> s.getTeam().removeShip(s.getName()));

        Platform.runLater(() -> getChildren().add(sv));
        sv.setOnCenterShipListener(onCenterShipListener);
        sv.setOnShipDetailsListener(onShipDetailsListener);

        ActionsCatalog.relatedShip.isEqualTo(s).addListener((observable, oldValue, newValue) -> {
            if(oldValue)
            {
                sv.getStyleClass().remove("related");
            }
            if(newValue)
            {
                sv.getStyleClass().add("related");
            }
        });

        shipViews.put(s, sv);
    }

    private void removeShipView(Ship s) {
        getChildren().remove(shipViews.get(s));
        shipViews.remove(s);
    }

    public ShipView getShipViewFor(Ship s)
    {
        return shipViews.get(s);
    }
}
