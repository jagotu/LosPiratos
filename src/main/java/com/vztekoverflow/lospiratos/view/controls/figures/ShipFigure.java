package com.vztekoverflow.lospiratos.view.controls.figures;

import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.shipMechanics.Chained;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

public class ShipFigure extends Pane {

    public ProgressBar hpBar;
    private Label shipType;
    private double widthMultiplier = 0.6;
    private StackPane sp;

    public Ship getShip() {
        return ship;
    }

    private Ship ship;
    private BooleanBinding isChainedBinding;

    public ShipFigure(Ship s, boolean pointy) {
        getStyleClass().add("ship-figure");
        this.ship = s;
        isChainedBinding = Bindings.createBooleanBinding(() -> s.getMechanics().stream().anyMatch(x -> x instanceof Chained), s.mechanicsProperty());


        sp = new StackPane();
        SVGPath shipPath = new SVGPath();
        shipPath.setContent("M 101.94141,0.00390625 C 82.955825,-0.02820042 73.403144,0.7164629 49.623047,3.1367188 30.561075,5.0767827 14.48945,7.1375116 13.910156,7.7167969 10.538929,20.679655 4.8289227,33.272392 2.4863281,44.966797 c 2.6343629,13.183182 7.6802159,26.144542 11.4238279,37.25 0.579294,0.579285 16.650919,2.641967 35.712891,4.582031 23.780097,2.420256 33.332778,3.162966 52.318363,3.13086 6.32853,-0.01071 13.70494,-0.106388 23.00586,-0.25586 66.80141,-1.073546 79.56858,-4.433913 120.22851,-31.634766 6.35872,-4.253877 15.05742,-8.779351 19.33203,-10.058593 10.00296,-0.961568 16.62468,-2.312776 30.96094,-3.013672 C 281.13249,44.265901 274.51077,42.916646 264.50781,41.955078 260.2332,40.675836 251.5345,36.148408 245.17578,31.894531 204.51585,4.6936788 191.74868,1.3352653 124.94727,0.26171875 115.64635,0.11224676 108.26994,0.01461558 101.94141,0.00390625 Z");
        shipPath.setFillRule(FillRule.EVEN_ODD);
        shipPath.fillProperty().bind(s.getTeam().colorProperty());
        shipPath.setStroke(Color.BLACK);
        shipPath.setStrokeWidth(7);
        shipPath.scaleXProperty().bind(Bindings.min(widthProperty().multiply(0.82).divide(shipPath.prefWidth(-1)), heightProperty().multiply(0.82).divide(shipPath.prefHeight(-1))));
        shipPath.scaleYProperty().bind(shipPath.scaleXProperty());
        sp.getChildren().add(shipPath);

        SVGPath chainPath = new SVGPath();
        chainPath.setContent("M461.184,38.641l-24.061-24.059c-19.111-19.128-52.448-19.128-71.559,0l-64.593,64.593c-9.556,9.555-14.816,22.258-14.816,35.778c0,13.522,5.261,26.225,14.816,35.781l0.163,0.162l-6.899,6.917l-0.18-0.18c-19.111-19.095-52.448-19.102-71.559,0.008l-64.595,64.585c-19.732,19.733-19.732,51.833,0,71.567l0.181,0.171l-6.949,6.95l-0.18-0.18c-19.111-19.128-52.448-19.128-71.559,0l-64.575,64.584C5.262,374.874,0,387.586,0,401.099c0,13.514,5.262,26.224,14.818,35.78l24.043,24.061c9.556,9.564,22.257,14.824,35.779,14.824c13.522,0,26.224-5.261,35.779-14.824l64.594-64.586c9.571-9.563,14.833-22.273,14.833-35.787c0-13.514-5.262-26.224-14.833-35.796l-0.131-0.139l6.934-6.934l0.145,0.156c9.557,9.563,22.258,14.825,35.78,14.825c13.521,0,26.225-5.262,35.779-14.825l64.592-64.586c9.557-9.555,14.834-22.265,14.834-35.787c0-13.522-5.277-26.232-14.834-35.778l-0.162-0.156l6.916-6.917l0.164,0.156c9.556,9.563,22.257,14.825,35.779,14.825c13.523,0,26.225-5.262,35.779-14.825l64.594-64.586C470.738,100.646,476,87.936,476,74.422C476,60.9,470.738,48.19,461.184,38.641z M151.28,348.511c3.181,3.181,5,7.573,5,12.056c0,4.483-1.819,8.875-5,12.056l-64.593,64.584c-6.359,6.343-17.734,6.343-24.094,0L38.55,413.146c-6.638-6.646-6.638-17.446,0-24.093l64.577-64.586c3.213-3.221,7.49-4.989,12.047-4.989c4.556,0,8.833,1.768,12.046,4.998l0.181,0.181l-7.638,7.637c-6.556,6.558-6.556,17.187,0,23.733c3.278,3.278,7.573,4.918,11.866,4.918c4.295,0,8.588-1.64,11.867-4.918l7.653-7.655L151.28,348.511z M294.382,205.443c3.213,3.213,4.998,7.491,4.998,12.039c0,4.557-1.785,8.835-4.998,12.056l-64.593,64.584c-6.36,6.343-17.734,6.343-24.094,0l-0.147-0.156l7.621-7.63c6.556-6.556,6.556-17.185,0-23.732c-6.556-6.556-17.176-6.556-23.731,0l-7.621,7.63l-0.182-0.173c-6.638-6.646-6.638-17.455,0-24.102l64.593-64.577c3.214-3.221,7.49-4.989,12.047-4.989c4.557,0,8.835,1.769,12.047,4.98l0.165,0.174l-7.655,7.661c-6.556,6.558-6.556,17.178,0,23.733c3.277,3.277,7.572,4.918,11.866,4.918c4.295,0,8.589-1.641,11.867-4.918l7.653-7.661L294.382,205.443z M437.45,86.469l-64.593,64.584c-6.424,6.441-17.669,6.441-24.094,0l-0.163-0.156l7.653-7.645c6.556-6.558,6.556-17.178,0-23.733c-6.556-6.556-17.176-6.556-23.733,0l-7.653,7.646L324.703,127c-3.213-3.212-4.981-7.49-4.981-12.047c0-4.557,1.768-8.833,4.981-12.047l64.594-64.593c3.179-3.17,7.571-4.989,12.046-4.989s8.868,1.819,12.047,4.989l24.061,24.069c3.212,3.213,4.983,7.49,4.983,12.039C442.434,78.97,440.662,83.248,437.45,86.469z");
        chainPath.scaleXProperty().bind(Bindings.min(widthProperty().multiply(0.4).divide(chainPath.prefWidth(-1)), heightProperty().multiply(0.4).divide(chainPath.prefHeight(-1))));
        chainPath.scaleYProperty().bind(chainPath.scaleXProperty());
        chainPath.visibleProperty().bind(isChainedBinding);
        sp.getChildren().add(chainPath);

        getChildren().add(sp);


        shipType = new Label();
        shipType.textProperty().bind(Bindings.createStringBinding(() -> s.getShipType().getČeskéJméno().substring(0, 1), s.shipTypeProperty()));
        shipType.setFont(new Font(38));
        shipType.getStyleClass().add("contrast");
        getChildren().add(shipType);

        if(isChainedBinding.get()) shipType.getStyleClass().add("forcewhite");

        isChainedBinding.addListener(((observable, oldValue, newValue) -> {
            if(oldValue)
            {
                shipType.getStyleClass().remove("forcewhite");
            }
            if(newValue)
            {
                shipType.getStyleClass().add("forcewhite");
            }
        }));


        s.getTeam().colorProperty().addListener(((observable, oldValue, newValue) -> updateColor(newValue)));
        updateColor(s.getTeam().getColor());

        hpBar = new ProgressBar();
        hpBar.progressProperty().bind(s.currentHPProperty().add(0.0).divide(s.maxHPProperty()));
        hpBar.prefWidthProperty().bind(widthProperty().multiply(widthMultiplier).multiply(s.maxHPProperty()).divide(120));
        hpBar.prefHeightProperty().set(18);
        getChildren().add(hpBar);

        int rotationNormalisation = 120;
        if (!pointy)
            rotationNormalisation -= 30;
        sp.rotateProperty().bind(s.getPosition().rotationProperty().subtract(rotationNormalisation));

    }

    private void updateColor(Color color) {
        String style = String.format("-team-color: rgba(%d, %d, %d, %f);",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
        setStyle(style);
    }


    @Override
    protected void layoutChildren() {
        layoutInArea(sp, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(shipType, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(hpBar, getWidth() * ((1 - widthMultiplier) / 2), getHeight() * 0.25, getWidth() * widthMultiplier, hpBar.prefHeight(-1), 0, HPos.LEFT, VPos.TOP);
    }

}
