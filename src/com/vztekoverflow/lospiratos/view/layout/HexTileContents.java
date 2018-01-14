package com.vztekoverflow.lospiratos.view.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

public interface HexTileContents {
    ObjectProperty<Node> contentsProperty();

    StringProperty cssClassProperty();
}
