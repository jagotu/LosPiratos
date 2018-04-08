package com.vztekoverflow.lospiratos.viewmodel;

import com.vztekoverflow.lospiratos.model.MapTile;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Sea;
import com.vztekoverflow.lospiratos.viewmodel.boardTiles.Shore;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ships.Schooner;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyMapProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BoardTest {
    @Test
    void tilesProperty() {
        //Game g = Game.CreateNewMockGame();
        //Board b = g.getBoard();
        com.vztekoverflow.lospiratos.model.Map modelMap = new com.vztekoverflow.lospiratos.model.Map();
        Board b = new Board(null, modelMap);

        ReadOnlyMapProperty<AxialCoordinate, BoardTile> tiles = b.tilesProperty();
        ListProperty<MapTile> modelTiles = modelMap.tilesProperty();
        Assertions.assertEquals(0, tiles.size());
        Assertions.assertEquals(0, modelTiles.size());

        //add via viemodel:
        tiles.put(new AxialCoordinate(1,1),new Shore(new AxialCoordinate(1,1),b));
        Assertions.assertEquals(1, modelTiles.size());
        Assertions.assertEquals(1, tiles.size());
        MapTile modelTile = modelTiles.get(0);
        Assertions.assertEquals(modelTile.getLocation(), new AxialCoordinate(1,1));
        Assertions.assertEquals(modelTile.getContent(), BoardTile.getPersistentName(Shore.class));

        //add via model:
        modelTiles.add(new MapTile(new AxialCoordinate(3,4), BoardTile.getPersistentName(Sea.class)));
        Assertions.assertEquals(2, modelTiles.size());
        Assertions.assertEquals(2, tiles.size());
        BoardTile tile = tiles.get(new AxialCoordinate(3,4));
        Assertions.assertTrue(tile instanceof Sea);

        //remove via viewmodel:
        tiles.remove(new AxialCoordinate(3,4));
        Assertions.assertEquals(1, modelTiles.size());
        Assertions.assertEquals(1, tiles.size());

        //remove via model:
        modelTiles.removeIf(t -> t.getLocation().equals(new AxialCoordinate(1,1)));
        Assertions.assertEquals(0, modelTiles.size());
        Assertions.assertEquals(0, tiles.size());
    }

    @Test
    void figuresProperty() {
        Game g = Game.CreateNewMockGame();
        Board b = g.getBoard();
        Assertions.assertEquals(b.figuresProperty().size(), g.getAllShips().size());
        g.getTeams().get(0).createAndAddNewShip(Schooner.class, "randomName","randomCaptain", new AxialCoordinate(1,1));
        Assertions.assertEquals(b.figuresProperty().size(), g.getAllShips().size());
    }


}