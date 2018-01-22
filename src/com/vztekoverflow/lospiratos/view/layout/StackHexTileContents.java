package com.vztekoverflow.lospiratos.view.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class StackHexTileContents implements HexTileContents {
    private StackPane s = new StackPane();
    private ObjectProperty<Node> contents = new ReadOnlyObjectWrapper<>(s);

    public void addNode(Node n) {
        s.getChildren().add(n);
    }

    public void removeNode(Node n)
    {
        s.getChildren().remove(n);
    }



    @Override
    public ObjectProperty<Node> contentsProperty() {
        return contents;
    }

    @Override
    public StringProperty cssClassProperty() {
        return null;
    }
}