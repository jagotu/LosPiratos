package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.model.Map;
import com.vztekoverflow.lospiratos.model.MapTile;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Warnings;
import javafx.beans.property.*;
import javafx.collections.*;

import java.util.List;
import java.util.stream.Collectors;

public class Board /* I want my Burd */ implements OnNextRoundStartedListener {

    //initializers:

    public Board(Game game, Map modelMap) {
        this.game = game;
        this.modelMap = modelMap;
        bindToModel();
        game.addOnNextRoundStartedListener(this);
    }

    private void bindToModel() {

        for (MapTile t : modelMap.getTiles()) {
            tryAddingTile(t);
        }
        //tiles 2-way binding:
        modelMap.tilesProperty().addListener((ListChangeListener<MapTile>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (MapTile addedItem : c.getAddedSubList()) {
                        tryAddingTile(addedItem);
                    }
                } else if (c.wasRemoved()) {
                    for (MapTile removedItem : c.getRemoved()) {
                        if (tiles.containsKey(removedItem.getLocation()))
                            tiles.remove(removedItem.getLocation());
                    }
                }
            }
        });
        tiles.addListener((MapChangeListener<AxialCoordinate, BoardTile>) c -> {
            if (c.wasAdded()) {
                //if this is circular call:
                if (modelMap.tilesProperty().stream().anyMatch(tile -> tile.getLocation().equals(c.getKey())))
                    return;
                modelMap.tilesProperty().add(new MapTile(c.getKey(), BoardTile.getPersistentName(c.getValueAdded().getClass())));
            } else if (c.wasRemoved()) {
                modelMap.tilesProperty().removeIf(tile -> tile.getLocation().equals(c.getKey()));
            } else {
                Warnings.panic("Board.figuresProperty listener of " + toString(), "unreachable code?!");
            }
        });

    }

    private boolean tryAddingTile(MapTile modelTile) {
        BoardTile t = BoardTile.createInstanceFromPersistentName(modelTile.getContent(), modelTile.getLocation(), this);
        if (t == null) return false;
        if (tiles.containsKey(t.getLocation())) return false;
        tiles.put(modelTile.getLocation(), t);
        return true;
    }

    private Game game;
    private Map modelMap;

    //properties:

    private MapProperty<AxialCoordinate, BoardTile> tiles = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private ListProperty<MovableFigure> figures = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ObservableMap<AxialCoordinate, BoardTile> getTiles() {
        return tiles.get();
    }

    public ReadOnlyMapProperty<AxialCoordinate, BoardTile> tilesProperty() {
        return tiles;
    }

    /**
     * !!! Adding a new figure to the figures list won't add it to the game.
     * Use add() method on the figures list only if you want to add an item just to the Board.
     */
    public ObservableList<MovableFigure> getFigures() {
        return figures.get();
    }

    /**
     * !!! Adding a new figure to the figures list won't add it to the game.
     * Use add() method on the figures list only if you want to add an item just to the Board.
     */
    public ReadOnlyListProperty<MovableFigure> figuresProperty() {
        return figures;
    }

    private MovableFigure findFigure(AxialCoordinate position) {
        //todo is this not too slow? It is a trivial O(figureCount) implementation
        List<MovableFigure> f = this.figures.stream().filter(p -> p.getPosition().equals(position)).collect(Collectors.toList());
        if (f.size() == 0)
            return null;
        if (f.size() > 1) {
            Warnings.panic(toString(), "more figures (" + f.size() + ") at the same position: " + position);
        }
        return f.get(0);
    }

    //public functions:

    public Game getGame() {
        return game;
    }

    public boolean canStepOn(AxialCoordinate location){
        BoardTile b = tiles.get(location);
        return b != null && b.mayBeSteppedOn();
    }

    @Override
    public void onNextRoundStarted(int roundNo) {

    }

    /**
     * Returns a DamageableFigure located at given @position
     *
     * @return null if no figure is located at given @position
     */
    public DamageableFigure getDamageableFigure(AxialCoordinate position) {
        MovableFigure f = findFigure(position);
        if (f instanceof DamageableFigure)
            return (DamageableFigure) f;
        else return null;
    }

    /**
     * Returns a Plunderable located at given @position
     *
     * @return null if no plunderable is located at given @position
     */
    public Plunderable getPlunderable(AxialCoordinate position) {
        if (tiles.get(position) instanceof Plunderable)
            return (Plunderable) tiles.get(position);

        MovableFigure f = findFigure(position);
        if (f instanceof Plunderable)
            return (Plunderable) f;
        else return null;
    }

    /**
     * Returns a Ship located at given @position
     *
     * @return null if no ship is located at given @position
     */
    public Ship getShip(AxialCoordinate position) {
        MovableFigure f = findFigure(position);
        if (f instanceof Ship)
            return (Ship) f;
        else return null;
    }
}
