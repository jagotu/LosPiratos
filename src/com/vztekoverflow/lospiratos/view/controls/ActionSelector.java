package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionIcon;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
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
            Node graphic;
            if ((graphic = getGraphicFor(n.getIcon())) != null) {
                b.setGraphic(graphic);
            } else {
                //This is bullshit. Everything should have a distinct icon
                if (n.isLeaf()) {
                    b.setText(n.getAction().getČeskéJméno());
                } else {
                    b.setText(n.getIcon().toString());
                }
            }
            b.wrapTextProperty().set(true);
            b.textAlignmentProperty().set(TextAlignment.CENTER);

            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            b.setMinWidth(0);
            b.setMinHeight(0);
            //hack for improper long names
            b.wrapTextProperty().set(true);
            b.textAlignmentProperty().set(TextAlignment.CENTER);
            if (!n.isLeaf()) {
                //Show three dots
                b.setText(b.getText() + "...");
                b.setOnAction(e -> {
                    previousCenters.push(center);
                    center = new Point2D(b.getLayoutX() + b.getTranslateX(), b.getLayoutY() + b.getTranslateY());
                    currentNode.set(n);
                });
            } else {
                b.visibleProperty().bind(n.getAction().visibleProperty());
                b.visibleProperty().addListener(i -> requestLayout());
                b.disableProperty().bind(Bindings.not(n.getAction().plannableProperty()));

                b.setOnAction(e -> {
                    if (onActionSelectedListener != null) {
                        onActionSelectedListener.onActionSelected(n.getAction(), b);
                    }
                });
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
        Glyph g;
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
            /*case attackGenericIcon:
                g = new Glyph("FontAwesome", FontAwesome.Glyph.BOMB);
                g.setFontSize(32);
                return g;*/
            default:
                return null;
        }
    }
}
