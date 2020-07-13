package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlannedActionsBar extends Region {


    public interface OnActionDetailsListener {
        void onActionClicked(PlannableAction action, Node sender);
    }

    private OnActionDetailsListener onActionDetailsListener;

    public OnActionDetailsListener getOnActionDetailsListener() {
        return onActionDetailsListener;
    }

    public void setOnActionDetailsListener(OnActionDetailsListener onActionDetailsListener) {
        this.onActionDetailsListener = onActionDetailsListener;
    }

    private ListChangeListener<PlannableAction> listchange = change -> {
        while (change.next()) {
            final int from = change.getFrom();
            final int to = change.getTo();
            if (change.wasPermutated()) {
                final ArrayList<Node> copy = new ArrayList<Node>(getChildren().subList(from, to));
                for (int oldIndex = from; oldIndex < to; oldIndex++) {
                    int newIndex = change.getPermutation(oldIndex);
                    getChildren().set(newIndex, copy.get(oldIndex - from));
                }
            } else if (change.wasUpdated()) {
                // do nothing
            } else {
                if (change.wasRemoved()) {
                    getChildren().remove(from, from + change.getRemoved().size());
                }
                if (change.wasAdded()) {
                    getChildren().addAll(from, change.getAddedSubList().stream().map(this::getButtonFor).collect(Collectors.toList()));
                }
            }
        }
    };

    private PlannedActionsBar.BreadCrumbButton root = new PlannedActionsBar.BreadCrumbButton("", new Glyph("FontAwesome", FontAwesome.Glyph.TRASH_ALT));

    public PlannedActionsBar() {
        root.getStyleClass().add("first");
        root.setOnAction(e -> ActionsCatalog.relatedShip.get().plannedActionsProperty().clear());
        getChildren().add(root);
        ActionsCatalog.relatedShip.addListener((observable, oldValue, newValue) -> {

            if (oldValue != null) {
                getChildren().remove(0, getChildren().size() - 1);
                oldValue.getPlannedActions().removeListener(listchange);
            }

            if (newValue != null) {
                newValue.getPlannedActions().addListener(listchange);
                getChildren().addAll(0, newValue.getPlannedActions().stream().map(this::getButtonFor).collect(Collectors.toList()));
            }
        });
    }

    @Override
    protected void layoutChildren() {
        //Layout root first
        double rw = snapSize(root.prefWidth(-1));
        double rh = snapSize(root.maxHeight(-1));
        root.resize(rw, Math.max(rh, this.getHeight()));
        root.relocate(0, 0);
        root.visibleProperty().bind(ActionsCatalog.relatedShip.isNotNull());

        double x = rw;

        for (int i = 0; i < getChildren().size() - 1; i++) {
            Node n = getChildren().get(i);

            double nw = snapSize(n.prefWidth(-1));
            double nh = snapSize(n.maxHeight(-1));


            // We have to position the bread crumbs slightly overlapping
            double ins = n instanceof PlannedActionsBar.BreadCrumbButton ? ((PlannedActionsBar.BreadCrumbButton) n).getArrowWidth() : 0;
            x = snapPosition(x - ins);


            n.resize(nw, Math.max(nh, this.getHeight()));
            n.relocate(x, 0);
            x += nw;
        }
    }


    private Node getButtonFor(PlannableAction action) {
        Button b = new PlannedActionsBar.BreadCrumbButton(action.getČeskéJméno());
        b.setOnAction(e -> ActionsCatalog.relatedShip.get().unplanActions(getChildren().indexOf(b) + 1));
        b.setOnMouseClicked(e -> {
            if (onActionDetailsListener != null && e.isStillSincePress() && e.getButton().equals(MouseButton.SECONDARY) && action instanceof ParameterizedAction) {
                onActionDetailsListener.onActionClicked(action, b);
            }
        });
        return b;
    }

    public static class BreadCrumbButton extends Button {
        private final ObjectProperty<Boolean> first;
        private final double arrowWidth;
        private final double arrowHeight;

        public BreadCrumbButton(String text) {
            this(text, (Node)null);
        }

        public BreadCrumbButton(String text, Node gfx) {
            super(text, gfx);
            this.first = new SimpleObjectProperty(this, "first");
            this.arrowWidth = 5.0D;
            this.arrowHeight = 20.0D;
            this.first.set(false);
            this.getStyleClass().addListener(new InvalidationListener() {
                public void invalidated(Observable arg0) {
                    PlannedActionsBar.BreadCrumbButton.this.updateShape();
                }
            });
            this.updateShape();
        }

        private void updateShape() {
            this.setShape(this.createButtonShape());
        }

        public double getArrowWidth() {
            return 5.0D;
        }

        private Path createButtonShape() {
            Path path = new Path();
            MoveTo e1 = new MoveTo(0.0D, 0.0D);
            path.getElements().add(e1);
            HLineTo e2 = new HLineTo();
            e2.xProperty().bind(this.widthProperty().subtract(5.0D));
            path.getElements().add(e2);
            LineTo e3 = new LineTo();
            e3.xProperty().bind(e2.xProperty().add(5.0D));
            e3.setY(10.0D);
            path.getElements().add(e3);
            LineTo e4 = new LineTo();
            e4.xProperty().bind(e2.xProperty());
            e4.setY(20.0D);
            path.getElements().add(e4);
            HLineTo e5 = new HLineTo(0.0D);
            path.getElements().add(e5);
            if (!this.getStyleClass().contains("first")) {
                LineTo e6 = new LineTo(5.0D, 10.0D);
                path.getElements().add(e6);
            } else {
                ArcTo arcTo = new ArcTo();
                arcTo.setSweepFlag(true);
                arcTo.setX(0.0D);
                arcTo.setY(0.0D);
                arcTo.setRadiusX(15.0D);
                arcTo.setRadiusY(15.0D);
                path.getElements().add(arcTo);
            }

            ClosePath e7 = new ClosePath();
            path.getElements().add(e7);
            path.setFill(Color.BLACK);
            return path;
        }
    }
}

