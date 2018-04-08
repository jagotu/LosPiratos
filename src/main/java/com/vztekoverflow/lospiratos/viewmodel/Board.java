package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.model.Map;
import com.vztekoverflow.lospiratos.model.MapTile;
import com.vztekoverflow.lospiratos.model.ResourceM;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.util.Warnings;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Plantation;
import javafx.beans.property.*;
import javafx.collections.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The game board (aka map or world) that the game is played on.
 */
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
                //todo bug, this not only prevents circular calls, but also prevents updating a tiles content
                if (modelMap.tilesProperty().stream().anyMatch(tile -> tile.getLocation().equals(c.getKey())))
                    return;
                if (c.getValueAdded() instanceof Plantation) {
                    Plantation p = (Plantation) c.getValueAdded();
                    ResourceM r = new ResourceM();
                    p.getResource().bindBidirectionalFrom(r); //!!!!!
                    modelMap.tilesProperty().add(new MapTile(c.getKey(), BoardTile.getPersistentName(c.getValueAdded().getClass()), r));
                } else
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
        if (t instanceof Plantation) {
            ((Plantation) t).getResource().bindBidirectional(modelTile.plantationsResource);
        }
        tiles.put(modelTile.getLocation(), t);
        return true;
    }

    private Game game;
    private Map modelMap;

    //properties:

    private MapProperty<AxialCoordinate, BoardTile> tiles = new SimpleMapProperty<>(FXCollections.observableHashMap());
    private ListProperty<Figure> figures = new SimpleListProperty<>(FXCollections.observableArrayList());

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
    public ObservableList<Figure> getFigures() {
        return figures.get();
    }

    /**
     * !!! Adding a new figure to the figures list won't add it to the game.
     * Use add() method on the figures list only if you want to add an item just to the Board.
     */
    public ReadOnlyListProperty<Figure> figuresProperty() {
        return figures;
    }

    private List<Figure> findFigures(AxialCoordinate position) {
        return this.figures.stream().filter(p -> p.getCoordinate().equals(position)).collect(Collectors.toList());
    }

    private Figure findFigure(AxialCoordinate position) {
        //todo is this not too slow? It is a trivial O(figureCount) implementation
        List<Figure> f = findFigures(position);
        if (f.size() == 0)
            return null;
        if (f.size() > 1) {
            Warnings.makeWarning(toString(), "returning just first figure, when more (" + f.size() + ") are at the same position: " + position);
        }
        return f.get(0);
    }

    //public functions:

    public Game getGame() {
        return game;
    }

    public boolean canStepOn(AxialCoordinate location) {
        BoardTile b = tiles.get(location);
        return b != null && b.mayBeSteppedOn();
    }

    @Override
    public void onNextRoundStarted(int roundNo) {

    }

    /**
     * Returns a DamageableFigure located at given @position
     *
     * @return null if no figure is located at given @position or if the location does not allow fighting
     */
    public DamageableFigure getDamageableFigure(AxialCoordinate position) {
        Figure f = findFigure(position);
        if (f instanceof DamageableFigure) {
            if (!tiles.get(f.getCoordinate()).allowsFighting())
                return null;
            return (DamageableFigure) f;
        } else return null;
    }

    /**
     * Returns a Plunderable located at given @position
     * If more plunderables are located at the position, return just one of them
     *
     * @return null if no plunderable is located at given @position
     */
    public Plunderable getPlunderable(AxialCoordinate position) {
        if (tiles.get(position) instanceof Plunderable)
            return (Plunderable) tiles.get(position);

        List<Plunderable> plunderables = findFigures(position).stream().filter(f -> f instanceof Plunderable).map(f -> (Plunderable) f).collect(Collectors.toList());
        if (plunderables.size() >= 1) {
            if (plunderables.size() > 1)
                Warnings.makeWarning(toString() + ".getPlunderable()", "more (" + plunderables.size() + ") are at the same position: " + position + ")");
            return plunderables.get(0);
        } else return null;
    }

    /**
     * Returns a Ship located at given @position
     *
     * @return null if no ship is located at given @position
     */
    public Ship getShip(AxialCoordinate position) {
        Figure f = findFigure(position);
        if (f instanceof Ship)
            return (Ship) f;
        else return null;
    }

    /**
     * @param from
     * @param type
     * @return nearest Tile or null if there is no Tile of such type
     */
    public BoardTile getNearestTile(AxialCoordinate from, Class<? extends BoardTile> type) {
        Optional<BoardTile> o = tilesProperty().values().stream().filter(t -> type.isAssignableFrom(t.getClass())).
                min(Comparator.comparingInt(t -> from.distanceTo(t.getLocation())));
        return o.isPresent() ? o.get() : null;
    }
}
