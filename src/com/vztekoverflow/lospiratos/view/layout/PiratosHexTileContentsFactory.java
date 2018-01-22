package com.vztekoverflow.lospiratos.view.layout;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.Board;
import com.vztekoverflow.lospiratos.viewmodel.BoardTile;
import com.vztekoverflow.lospiratos.viewmodel.MovableFigure;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class PiratosHexTileContentsFactory implements HexTileContentsFactory{

    private Board board;
    private HashMap<AxialCoordinate, StackHexTileContents> current = new HashMap<>();

    public PiratosHexTileContentsFactory(Board board) {
        this.board = board;
        for(AxialCoordinate coords : board.getTiles().keySet())
        {
            if (current.containsKey(coords))
            {
                throw new UnsupportedOperationException("Cannot have the same tile defined twice!");
            }
            StackHexTileContents sc = new StackHexTileContents();
            Label l = new Label(board.getTiles().get(coords).getClass().toString().replace("class com.vztekoverflow.lospiratos.viewmodel.BoardTiles.", ""));
            l.setPadding(new Insets(30, 0, 0, 0));
            sc.addNode(l);
            current.put(coords, sc);
        }
        for(MovableFigure mf : board.getFigures())
        {
            addFigure(mf);
        }
        board.figuresProperty().addListener((ListChangeListener<MovableFigure>) c -> {
            while (c.next()) {
                if (c.wasAdded()){
                    for (MovableFigure addedItem : c.getAddedSubList()) {
                        addFigure(addedItem);
                    }
                } else if(c.wasRemoved()) {
                    for (MovableFigure removedItem : c.getRemoved()) {
                        removeFigure(removedItem);
                    }
                }
            }
        });
    }

    private HashMap<MovableFigure, Node> cachedFigures = new HashMap<>();


    private void addFigure(MovableFigure f)
    {
        if(!current.containsKey(f.getPosition().getCoordinate()))
        {
            throw new IndexOutOfBoundsException("Axial coordinates out of defined tiles!");
        }

        Node n = null;
        if(f instanceof Ship)
        {
            n = new Label(((Ship)f).getName());
        }

        if(n != null)
        {
            current.get(f.getPosition().getCoordinate()).addNode(n);
            cachedFigures.put(f, n);
        }


    }

    private void removeFigure(MovableFigure f)
    {
        if(cachedFigures.containsKey(f) && current.containsKey(f.getPosition().getCoordinate()))
        {
            current.get(f.getPosition().getCoordinate()).removeNode(cachedFigures.get(f));
            cachedFigures.remove(f);
        }
    }



    @Override
    public HexTileContents getContentsFor(AxialCoordinate coords, double tileWidth, double tileHeight) {
        if (current.containsKey(coords))
        {
            return current.get(coords);
        }
        return null;
    }

}
