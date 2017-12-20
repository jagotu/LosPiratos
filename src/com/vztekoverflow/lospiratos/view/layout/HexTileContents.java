package com.vztekoverflow.lospiratos.view.layout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

public interface HexTileContents {
    ObjectProperty<Node> contentsProperty();
    ObjectProperty<Paint> backgroundProperty();
    BooleanProperty clipProperty();
}
