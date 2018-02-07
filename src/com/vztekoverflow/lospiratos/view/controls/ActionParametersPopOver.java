package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

public class ActionParametersPopOver extends PopOver {


    public interface OnRequestAxialCoordinateListener {
        void onRequestAxialCoordinate(PlannableAction action, AxialCoordinateActionParameter par);
    }

    private OnRequestAxialCoordinateListener onRequestAxialCoordinateListener = null;

    public OnRequestAxialCoordinateListener getOnRequestAxialCoordinateListener() {
        return onRequestAxialCoordinateListener;
    }

    public void setOnRequestAxialCoordinateListener(OnRequestAxialCoordinateListener onRequestAxialCoordinateListener) {
        this.onRequestAxialCoordinateListener = onRequestAxialCoordinateListener;
    }

    public ObservableList<AxialCoordinate> getHighlightedTiles() {
        return highlightedTiles;
    }

    private ObservableList<AxialCoordinate> highlightedTiles = new SimpleListProperty<>(FXCollections.observableArrayList());

    private BooleanProperty readOnly = new SimpleBooleanProperty(false);

    public boolean getReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    private PlannableAction action;
    private GridPane gp = new GridPane();
    private Button okButton;

    public PlannableAction getAction() {
        return action;
    }

    public void setAction(PlannableAction action) {
        gp.getChildren().clear();
        highlightedTiles.clear();

        if (action == null) {
            return;
        }


        if (!(action instanceof ParameterizedAction)) {
            throw new IllegalArgumentException("Action is not Parameterized");
        }

        this.action = action;
        ParameterizedAction par = (ParameterizedAction) action;

        BooleanBinding okEnabledBinding = Bindings.createBooleanBinding(() -> true);

        int i = 0;
        for (ActionParameter a : par.getAvailableParameters()) {
            Label l = new Label(a.getČeskéJméno() + ":");
            l.setPadding(new Insets(0, 4, 0, 0));
            gp.add(l, 0, i);
            gp.add(getNodeFor(a), 1, i);
            //okEnabledBinding = okEnabledBinding.and(a.isSatisfied()); //todo rozlisit ActionParameter ParametrizedActionParameter
            i++;
        }

        okButton.disableProperty().bind(okEnabledBinding.not());

    }

    public ActionParametersPopOver() {

        ButtonBar buttons = new ButtonBar();

        Button cancelButton = new Button(ButtonType.CANCEL.getText());
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        cancelButton.setOnAction(e -> {
            setAction(null);
            hide();
        });

        buttons.getButtons().add(cancelButton);

        okButton = new Button("Plán!");
        ButtonBar.setButtonData(okButton, ButtonBar.ButtonData.OK_DONE);
        okButton.setOnAction(e -> {
            ActionsCatalog.relatedShip.get().planAction(action);
            setAction(null);
            hide();
        });
        okButton.visibleProperty().bind(readOnly.not());
        buttons.getButtons().add(okButton);


        gp = new GridPane();
        gp.setPadding(new Insets(0, 0, 6, 0));

        BorderPane root = new BorderPane();
        root.setBottom(buttons);
        root.setCenter(gp);
        root.setPadding(new Insets(6));


        setDetachable(false);
        setCloseButtonEnabled(true);
        setContentNode(root);
        autoHideProperty().bind(readOnly);
        setAutoFix(false);

        setOnAutoHide(e -> setAction(null));
    }

    private Node getNodeFor(ActionParameter p) {
        if (p instanceof AxialCoordinateActionParameter) {
            AxialCoordinateActionParameter ap = (AxialCoordinateActionParameter) p;
            HBox h = new HBox();
            Label l = new Label();
            h.setAlignment(Pos.CENTER_LEFT);
            l.setPadding(new Insets(0, 4, 0, 0));
            l.textProperty().bind(ap.property().asString());
            Button b = new Button();
            b.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.BULLSEYE));
            h.getChildren().addAll(l, b);
            b.setOnAction(e -> {
                if (onRequestAxialCoordinateListener != null) {
                    onRequestAxialCoordinateListener.onRequestAxialCoordinate(action, ap);
                    hide();
                }
            });
            b.visibleProperty().bind(readOnly.not());
            ap.property().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    highlightedTiles.remove(oldValue);
                }
                if (newValue != null) {
                    highlightedTiles.add(newValue);
                }
            });
            if (ap.property().get() != null) {
                highlightedTiles.add(ap.get());
            }
            return h;
        }
        return new Label("TODO");
    }

}
