package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import impl.org.controlsfx.skin.BreadCrumbBarSkin;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
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

    private BreadCrumbBarSkin.BreadCrumbButton root = new BreadCrumbBarSkin.BreadCrumbButton("", new Glyph("FontAwesome", FontAwesome.Glyph.TRASH_ALT));

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
            double ins = n instanceof BreadCrumbBarSkin.BreadCrumbButton ? ((BreadCrumbBarSkin.BreadCrumbButton) n).getArrowWidth() : 0;
            x = snapPosition(x - ins);


            n.resize(nw, Math.max(nh, this.getHeight()));
            n.relocate(x, 0);
            x += nw;
        }
    }


    private Node getButtonFor(PlannableAction action) {
        Button b = new BreadCrumbBarSkin.BreadCrumbButton(action.getČeskéJméno());
        b.setOnAction(e -> ActionsCatalog.relatedShip.get().unplanActions(getChildren().indexOf(b) + 1));
        b.setOnMouseClicked(e -> {
            if (onActionDetailsListener != null && e.isStillSincePress() && e.getButton().equals(MouseButton.SECONDARY) && action instanceof ParameterizedAction) {
                onActionDetailsListener.onActionClicked(action, b);
            }
        });
        return b;
    }
}

