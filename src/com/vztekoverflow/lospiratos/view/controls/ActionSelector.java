package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ActionSelector extends Pane {

    private Stack<Point2D> previousCenters = new Stack<>();
    public static final double BUTTON_SIZE = 64;
    private static final Point2D defaultCenter = new Point2D(-BUTTON_SIZE / 2, -BUTTON_SIZE / 2);


    public ActionsCatalog.Node getCurrentNode() {
        return currentNode.get();
    }

    public void setCurrentNode(ActionsCatalog.Node currentNode) {
        center = defaultCenter;
        previousCenters.clear();
        this.currentNode.set(currentNode);
    }

    private ObjectProperty<ActionsCatalog.Node> currentNode = new SimpleObjectProperty<>(null);

    public ActionSelector() {
        getStyleClass().add("action-selector");
        currentNode.addListener(x -> updateActionRoster());
        setPickOnBounds(false);
    }

    private Button back = null;

    public interface OnActionSelectedListener {
        void onActionSelected(PlannableAction action, Node sender);
    }

    private OnActionSelectedListener onActionSelectedListener = null;

    public OnActionSelectedListener getOnActionSelectedListener() {
        return onActionSelectedListener;
    }

    public void setOnActionSelectedListener(OnActionSelectedListener onActionSelectedListener) {
        this.onActionSelectedListener = onActionSelectedListener;
    }

    private void updateActionRoster() {
        getChildren().clear();
        if (currentNode.get() == null) {
            return;
        }
        if (currentNode.get().isLeaf()) {
            throw new IllegalStateException();
        }
        for (ActionsCatalog.Node n : currentNode.get().getChildren()) {
            final Button b = new Button();
            StackPane sp = new StackPane();
            b.setGraphic(sp);
            Node graphic;
            if ((graphic = getGraphicFor(n.getIcon())) != null) {
                sp.getChildren().add(graphic);
            } else {
                //This is bullshit. Everything should have a distinct icon
                //hack for improper long names
                Label l = new Label();
                l.setWrapText(true);
                l.setTextAlignment(TextAlignment.CENTER);
                if (n.isLeaf()) {
                    l.setText(n.getAction().getČeskéJméno());
                } else {
                    l.setText(n.getIcon().toString());
                }
                sp.getChildren().add(l);
            }

            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            b.setMinWidth(0);
            b.setMinHeight(0);


            if (!n.isLeaf() || n.getAction() instanceof ParameterizedAction) {
                //Add ellipsis
                Glyph ellipsis = new Glyph("FontAwesome", FontAwesome.Glyph.ELLIPSIS_H);
                StackPane.setAlignment(ellipsis, Pos.BOTTOM_RIGHT);
                sp.getChildren().add(ellipsis);
            }

            if (!n.isLeaf()) {
                b.setOnAction(e -> {
                    previousCenters.push(center);
                    center = new Point2D(b.getLayoutX() + b.getTranslateX(), b.getLayoutY() + b.getTranslateY());
                    currentNode.set(n);
                });
            } else {
                b.visibleProperty().bind(n.getAction().visibleProperty());
                b.visibleProperty().addListener(i -> requestLayout());
                b.disableProperty().bind(n.getAction().plannableProperty().not());

                b.setOnAction(e -> {
                    if (onActionSelectedListener != null) {
                        onActionSelectedListener.onActionSelected(n.getAction(), b);
                    }
                });
                if (!(n.getAction() instanceof ParameterizedAction)) {
                    ResourceView costView = new ResourceView();
                    costView.resourceProperty().bind(n.getAction().costProperty());
                    sp.getChildren().add(costView);
                }
            }
            b.relocate(center.getX(), center.getY());
            getChildren().add(b);
        }
        back = new Button();
        back.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
        final ActionsCatalog.Node parent = currentNode.get().getParent();
        back.setMaxWidth(Double.MAX_VALUE);
        back.setMaxHeight(Double.MAX_VALUE);
        back.setMinWidth(0);
        back.setMinHeight(0);
        back.setOnAction(e -> {
            if (previousCenters.empty()) {
                center = defaultCenter;
            } else {
                center = previousCenters.pop();
            }
            boolean first = true;
            for (Node c : getChildren().stream().filter(Node::isVisible).collect(Collectors.toList())) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), c);
                tt.setToX(0);
                tt.setToY(0);
                if (first && c != back) {
                    first = false;
                    tt.setOnFinished(x -> {
                        currentNode.set(parent);
                        if (parent == null) {
                            ActionsCatalog.relatedShip.set(null);
                        }
                    });
                }
                tt.play();
            }

        });

        getChildren().add(back);

    }

    private Point2D center = defaultCenter;

    @Override
    protected void layoutChildren() {
        List<Node> visibleChildren = getChildren().stream().filter(Node::isVisible).collect(Collectors.toList());

        int childCount = visibleChildren.size() - 1;


        double angle = 0;
        double toAdd = (2 * Math.PI) / childCount;
        double radius = 100;

        for (Node c : visibleChildren) {
            final double cangle = angle;
            c.resize(BUTTON_SIZE, BUTTON_SIZE);
            if (c != back) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), c);
                tt.setToX(radius * Math.sin(angle));
                tt.setToY(-radius * Math.cos(angle));
                tt.play();
            }
            layoutInArea(c, center.getX(), center.getY(), BUTTON_SIZE, BUTTON_SIZE, 0, HPos.CENTER, VPos.CENTER);
            angle += toAdd;
        }

    }

    private Node getGraphicFor(ActionIcon icon) {
        Glyph g, g2;
        StackPane s;
        switch (icon) {

            case turnLeft:
                g = new Glyph("FontAwesome", FontAwesome.Glyph.UNDO);
                g.setFontSize(24);
                return g;
            case turnRight:
                g = new Glyph("FontAwesome", '\uf01e');
                g.setFontSize(24);
                return g;
            case moveForward:
                g = new Glyph("FontAwesome", FontAwesome.Glyph.ANGLE_DOUBLE_UP);
                g.setFontSize(32);
                return g;
            case mortar:
                g = new Glyph("piratos", 'K');
                g.setFontSize(40);
                return g;
            case ballLeft:
                s = new StackPane();
                g = new Glyph("piratos", 'I');
                g.setFontSize(36);
                s.getChildren().add(g);
                g2 = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_LEFT);
                StackPane.setAlignment(g2, Pos.TOP_LEFT);
                s.getChildren().add(g2);
                return s;
            case ballRight:
                s = new StackPane();
                g = new Glyph("piratos", 'I');
                g.setFontSize(36);
                s.getChildren().add(g);
                g2 = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_RIGHT);
                StackPane.setAlignment(g2, Pos.TOP_RIGHT);
                s.getChildren().add(g2);
                return s;
            case chainLeft:
                s = new StackPane();
                g = new Glyph("piratos", 'H');
                g.setFontSize(40);
                s.getChildren().add(g);
                g2 = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_LEFT);
                StackPane.setAlignment(g2, Pos.TOP_LEFT);
                s.getChildren().add(g2);
                return s;
            case chainRight:
                s = new StackPane();
                g = new Glyph("piratos", 'H');
                g.setFontSize(40);
                g.setScaleX(-1);
                s.getChildren().add(g);
                g2 = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_RIGHT);
                StackPane.setAlignment(g2, Pos.TOP_RIGHT);
                s.getChildren().add(g2);
                return s;
            case cannonLeft:
                s = new StackPane();
                g = new Glyph("piratos", 'E');
                g.setFontSize(40);
                g.setScaleX(-1);
                s.getChildren().add(g);
                g2 = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_LEFT);
                StackPane.setAlignment(g2, Pos.TOP_LEFT);
                s.getChildren().add(g2);
                return s;
            case cannonRight:
                s = new StackPane();
                g = new Glyph("piratos", 'E');
                g.setFontSize(40);
                s.getChildren().add(g);
                g2 = new Glyph("FontAwesome", FontAwesome.Glyph.ARROW_RIGHT);
                StackPane.setAlignment(g2, Pos.TOP_RIGHT);
                s.getChildren().add(g2);
                return s;
            case attackGenericIcon:
                g = new Glyph("piratos", 'E');
                g.setFontSize(32);
                return g;
            default:
                return null;
        }
    }
}
