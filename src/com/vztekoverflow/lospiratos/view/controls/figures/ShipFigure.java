package com.vztekoverflow.lospiratos.view.controls.figures;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

public class ShipFigure extends Pane {

    private SVGPath shipPath;
    private ProgressBar hpBar;

    public ShipFigure(Ship s) {
        getStyleClass().add("ship-figure");

        shipPath = new SVGPath();
        shipPath.setContent("M 101.94141,0.00390625 C 82.955825,-0.02820042 73.403144,0.7164629 49.623047,3.1367188 30.561075,5.0767827 14.48945,7.1375116 13.910156,7.7167969 10.538929,20.679655 4.8289227,33.272392 2.4863281,44.966797 c 2.6343629,13.183182 7.6802159,26.144542 11.4238279,37.25 0.579294,0.579285 16.650919,2.641967 35.712891,4.582031 23.780097,2.420256 33.332778,3.162966 52.318363,3.13086 6.32853,-0.01071 13.70494,-0.106388 23.00586,-0.25586 66.80141,-1.073546 79.56858,-4.433913 120.22851,-31.634766 6.35872,-4.253877 15.05742,-8.779351 19.33203,-10.058593 10.00296,-0.961568 16.62468,-2.312776 30.96094,-3.013672 C 281.13249,44.265901 274.51077,42.916646 264.50781,41.955078 260.2332,40.675836 251.5345,36.148408 245.17578,31.894531 204.51585,4.6936788 191.74868,1.3352653 124.94727,0.26171875 115.64635,0.11224676 108.26994,0.01461558 101.94141,0.00390625 Z");
        shipPath.setFillRule(FillRule.EVEN_ODD);
        shipPath.fillProperty().bind(s.getTeam().colorProperty());
        shipPath.setStroke(Color.BLACK);
        shipPath.setStrokeWidth(7);
        shipPath.scaleXProperty().bind(Bindings.min(widthProperty().multiply(0.82).divide(shipPath.prefWidth(-1)), heightProperty().multiply(0.82).divide(shipPath.prefHeight(-1))));
        shipPath.scaleYProperty().bind(shipPath.scaleXProperty());
        getChildren().add(shipPath);

        hpBar = new ProgressBar();
        hpBar.progressProperty().bind(s.currentHPProperty().add(0.0).divide(s.maxHPProperty()));
        hpBar.prefWidthProperty().bind(widthProperty().multiply(0.7).multiply(s.maxHPProperty()).divide(120));
        hpBar.prefHeightProperty().set(18);
        getChildren().add(hpBar);
        shipPath.rotateProperty().bind(s.getPosition().rotationProperty().subtract(120));
    }


    @Override
    protected void layoutChildren() {
        layoutInArea(shipPath, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(hpBar, 0, getHeight() * 0.25, getWidth(), hpBar.prefHeight(-1), 0, HPos.CENTER, VPos.TOP);
    }
}
