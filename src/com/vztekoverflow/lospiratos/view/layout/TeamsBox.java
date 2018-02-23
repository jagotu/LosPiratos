package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.Main;
import com.vztekoverflow.lospiratos.view.controls.OnCenterShipListener;
import com.vztekoverflow.lospiratos.view.controls.OnShipDetailsListener;
import com.vztekoverflow.lospiratos.view.controls.TeamView;
import com.vztekoverflow.lospiratos.viewmodel.Game;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;

public class TeamsBox extends FlowPane {

    private HashMap<Team, TeamView> teamViews = new HashMap<>();
    private Game game;

    private OnCenterShipListener onCenterShipListener = null;

    public void setOnCenterShipListener(OnCenterShipListener onCenterShipListener) {
        this.onCenterShipListener = onCenterShipListener;
        for (TeamView tv : teamViews.values()) {
            tv.setOnCenterShipListener(onCenterShipListener);
        }
    }


    private OnShipDetailsListener onShipDetailsListener = null;

    public void setOnShipDetailsListener(OnShipDetailsListener onShipDetailsListener) {
        this.onShipDetailsListener = onShipDetailsListener;
        for (TeamView tv : teamViews.values()) {
            tv.setOnShipDetailsListener(onShipDetailsListener);
        }
    }


    public void bindToGame(Game game) {
        this.game = game;
        getChildren().clear();
        teamViews.clear();

        for (final Team t : game.getTeams()) {
            Main.viewCreator.submit(() -> addTeamView(t));
        }

        game.getTeams().addListener((ListChangeListener.Change<? extends Team> c) -> {
            while (c.next()) {
                if (!(c.wasPermutated() || c.wasUpdated())) {
                    for (Team t : c.getAddedSubList()) {
                        Main.viewCreator.submit(() -> addTeamView(t));
                    }
                    for (Team t : c.getRemoved()) {
                        removeTeamView(t);
                    }
                }
            }
        });
    }


    private void addTeamView(Team t) {
        TeamView tv = new TeamView(t);
        FlowPane.setMargin(tv, new Insets(2, 2, 2, 2));
        tv.setRequestDeleteListener(team -> game.getTeams().remove(team));
        tv.setOnCenterShipListener(onCenterShipListener);
        tv.setOnShipDetailsListener(onShipDetailsListener);
        Platform.runLater(() -> getChildren().add(tv));

        teamViews.put(t, tv);
    }

    private void removeTeamView(Team t) {
        getChildren().remove(teamViews.get(t));
        teamViews.remove(t);

    }
}
