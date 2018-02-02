package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
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
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ActionSelector extends Pane {

    private Stack<Point2D> previousCenters = new Stack<>();
    public static final double BUTTON_SIZE = 100;
    private static final int layoutRadius = 170;
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
        currentNode.addListener(x -> updateActionRoster());
        setPickOnBounds(false);
    }

    private Button back = null;

    public interface OnActionSelectedListener {
        void onActionSelected(PlannableAction action);
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
            if (n.isLeaf()) {
                b.setText(n.getAction().getČeskéJméno());
            } else {
                b.setText(n.getIcon().toString());
            }
            b.wrapTextProperty().set(true);
            b.textAlignmentProperty().set(TextAlignment.CENTER);

            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            b.setMinWidth(0);
            b.setMinHeight(0);
            if (!n.isLeaf()) {
                //Show three dots
                b.setText(b.getText() + "...");
                b.setOnAction(e -> {
                    previousCenters.push(center);
                    center = new Point2D(b.getLayoutX(), b.getLayoutY());
                    currentNode.set(n);
                });
            } else {
                b.visibleProperty().bind(n.getAction().visibleProperty());
                b.visibleProperty().addListener(i -> requestLayout());
                b.disableProperty().bind(Bindings.not(n.getAction().plannableProperty()));

                b.setOnAction(e -> {
                    if (onActionSelectedListener != null) {
                        onActionSelectedListener.onActionSelected(n.getAction());
                    }
                });
            }
            getChildren().add(b);
        }
        back = new Button();
        if (currentNode.get().getParent() != null) {
            back.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.UNDO));
        } else {
            back.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
        }
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
            currentNode.set(parent);
            if (parent == null) {
                ActionsCatalog.relatedShip.set(null);
            }
        });

        getChildren().add(back);

    }

    private Point2D center = new Point2D(-BUTTON_SIZE / 2, -BUTTON_SIZE / 2);

    @Override
    protected void layoutChildren() {
        List<Node> visibleChildren = getChildren().stream().filter(Node::isVisible).collect(Collectors.toList());

        int childCount = visibleChildren.size() - 1;


        double angle = 0;
        double toAdd = (2 * Math.PI) / childCount;

        for (Node c : visibleChildren) {
            layoutInArea(c, center.getX() + layoutRadius * Math.sin(angle), center.getY() + layoutRadius * Math.cos(angle), BUTTON_SIZE, BUTTON_SIZE, 0, HPos.CENTER, VPos.CENTER);
            angle += toAdd;
        }

        if (back != null) {
            layoutInArea(back, center.getX(), center.getY(), BUTTON_SIZE, BUTTON_SIZE, 0, HPos.CENTER, VPos.CENTER);
        }

    }
}
