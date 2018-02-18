package com.vztekoverflow.lospiratos.view.controls;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.ActionsCatalog;
import com.vztekoverflow.lospiratos.viewmodel.actions.ParameterizedAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.PlannableAction;
import com.vztekoverflow.lospiratos.viewmodel.actions.attacks.AxialCoordinateActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.EnhancementActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.actions.transactions.ResourceActionParameter;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.ShipEnhancement;
import com.vztekoverflow.lospiratos.viewmodel.shipEntitites.enhancements.EnhancementsCatalog;
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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.util.stream.Collectors;

public class ActionParametersPopOver extends PopOver {


    public interface OnRequestAxialCoordinateListener {
        void onRequestAxialCoordinate(AxialCoordinateActionParameter par);
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

        int i = 0;
        for (ActionParameter a : par.getAvailableParameters()) {
            Label l = new Label(a.getČeskéJméno() + ":");
            l.setPadding(new Insets(0, 4, 0, 0));
            gp.add(l, 0, i);
            gp.add(getNodeFor(a), 1, i);
            i++;
        }

        okButton.disableProperty().bind(Bindings.not(par.satisfiedProperty()));
        costView.setPrefWrapLength(0);
        BooleanBinding notZeroPrice = Bindings.createBooleanBinding(() -> par.getCost() != null && !par.getCost().equals(ResourceReadOnly.ZERO), par.costProperty());
        cost.visibleProperty().bind(notZeroPrice);
        cost.managedProperty().bind(notZeroPrice);
        costView.resourceProperty().bind(par.costProperty());

    }

    private ResourceView costView;
    private HBox cost;

    public ActionParametersPopOver() {

        ButtonBar buttons = new ButtonBar();

        Button cancelButton = new Button(ButtonType.CANCEL.getText());
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        cancelButton.setOnAction(e -> {
            hide();
            setAction(null);
        });

        buttons.getButtons().add(cancelButton);

        okButton = new Button("Plán!");
        ButtonBar.setButtonData(okButton, ButtonBar.ButtonData.OK_DONE);
        okButton.setOnAction(e -> {
            ActionsCatalog.relatedShip.get().planAction(action);
            hide();
            setAction(null);
        });
        okButton.visibleProperty().bind(readOnly.not());
        buttons.getButtons().add(okButton);


        gp = new GridPane();
        gp.setPadding(new Insets(0, 0, 6, 0));


        VBox vb = new VBox();
        cost = new HBox();
        cost.getChildren().add(new Label("Cena:"));
        costView = new ResourceView();

        cost.getChildren().add(costView);
        vb.getChildren().add(cost);
        vb.getChildren().add(buttons);

        BorderPane root = new BorderPane();
        root.setBottom(vb);
        root.setCenter(gp);
        root.setPadding(new Insets(6));


        setDetachable(false);
        setCloseButtonEnabled(false);
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
                    onRequestAxialCoordinateListener.onRequestAxialCoordinate(ap);
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
        } else if (p instanceof EnhancementActionParameter) {
            HBox hb = new HBox();
            EnhancementActionParameter eap = (EnhancementActionParameter) p;
            ComboBox<Class<? extends ShipEnhancement>> cb = new ComboBox<Class<? extends ShipEnhancement>>(EnhancementsCatalog.allPossibleEnhancements.stream().filter(eap::isValidValue).collect((Collectors.collectingAndThen(Collectors.toList(), FXCollections::observableArrayList))));

            Callback<ListView<Class<? extends ShipEnhancement>>, ListCell<Class<? extends ShipEnhancement>>> cellFactory = new Callback<ListView<Class<? extends ShipEnhancement>>, ListCell<Class<? extends ShipEnhancement>>>() {
                @Override
                public ListCell<Class<? extends ShipEnhancement>> call(ListView<Class<? extends ShipEnhancement>> l) {
                    return new ListCell<Class<? extends ShipEnhancement>>() {
                        @Override
                        protected void updateItem(Class<? extends ShipEnhancement> item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setGraphic(null);
                                setText("");
                            } else {
                                setText(EnhancementsCatalog.createInstanceFromPersistentName(EnhancementsCatalog.getPersistentName(item)).getČeskéJméno());
                            }
                        }
                    };
                }
            };
            cb.setButtonCell(cellFactory.call(null));
            cb.setCellFactory(cellFactory);
            cb.getSelectionModel().select(eap.get());
            eap.property().bind(cb.getSelectionModel().selectedItemProperty());

            Label l = new Label();
            l.textProperty().bind(cb.getButtonCell().textProperty());
            l.visibleProperty().bind(readOnly);
            l.managedProperty().bind(readOnly);
            cb.visibleProperty().bind(readOnly.not());
            cb.managedProperty().bind(readOnly.not());


            hb.getChildren().add(cb);
            hb.getChildren().add(l);
            return hb;
        } else if (p instanceof ResourceActionParameter) {
            ResourceActionParameter rap = (ResourceActionParameter) p;
            HBox hb = new HBox();

            ResourceEdit re = new ResourceEdit(rap.get());
            ResourceView rw = new ResourceView();
            rw.resourceProperty().bind(rap.property());

            rw.visibleProperty().bind(readOnly);
            rw.managedProperty().bind(readOnly);
            re.visibleProperty().bind(readOnly.not());
            re.managedProperty().bind(readOnly.not());


            hb.getChildren().add(re);
            hb.getChildren().add(rw);
            return hb;

        }
        return new Label("TODO " + p.getClass().getSimpleName());
    }

}
