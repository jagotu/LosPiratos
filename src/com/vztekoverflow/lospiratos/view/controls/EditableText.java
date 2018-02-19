package com.vztekoverflow.lospiratos.view.controls;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

public abstract class EditableText extends StackPane {

    private BooleanProperty editing = new SimpleBooleanProperty(false);

    protected StringProperty text = new SimpleStringProperty();
    protected BooleanProperty valid = new SimpleBooleanProperty(true);


    protected TextField contentEdit;
    private Label contentShow;
    private BorderPane editMode;
    private BorderPane showMode;
    private Button edit;
    private boolean rightToLeft;
    private ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.EDITABLE);

    public Mode getMode() {
        return mode.get();
    }

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode.set(mode);
    }

    public EditableText(boolean rightToLeft) {
        getStyleClass().add("editable-text");
        this.rightToLeft = rightToLeft;


        editing.addListener((observable, oldValue, newValue) -> {
            if(mode.get().equals(Mode.EDITABLE))
            {
                if (newValue) {
                    this.getChildren().add(editMode);
                    this.getChildren().remove(showMode);
                } else {
                    this.getChildren().remove(editMode);
                    this.getChildren().add(showMode);
                }
            }
        });

        mode.addListener(i -> {
            createControls(mode.get());
        });
        createControls(mode.get());

    }

    private void createControls(Mode mode) {
        getChildren().clear();
        if (mode.equals(Mode.EDITABLE)) {
            showMode = new BorderPane();
            showMode.setId("showMode");

            contentShow = new Label();
            contentShow.setId("contentShow");
            BorderPane.setAlignment(contentShow, Pos.CENTER_LEFT);
            showMode.setCenter(contentShow);

            edit = new Button();
            edit.setId("edit");
            edit.setOnAction(x -> edit());
            edit.setVisible(false);
            edit.getStyleClass().add("mini-button");
            edit.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.PENCIL));
            if (rightToLeft) {
                showMode.setLeft(edit);
            } else {
                showMode.setRight(edit);
            }


            getChildren().add(showMode);

            editMode = new BorderPane();
            editMode.setId("editMode");

            contentEdit = new TextField();
            contentEdit.setId("contentEdit");
            BorderPane.setAlignment(contentEdit, Pos.CENTER_LEFT);
            editMode.setCenter(contentEdit);

            HBox hb = new HBox();

            Button save = new Button();
            save.setOnAction(x -> save());
            save.getStyleClass().add("mini-button");
            save.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.CHECK));
            hb.getChildren().add(save);

            Button cancel = new Button();
            cancel.setOnAction(x -> cancel());
            cancel.getStyleClass().add("mini-button");
            cancel.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.TIMES));
            hb.getChildren().add(cancel);

            editMode.setRight(hb);


            contentEdit.textProperty().addListener((ov, prevText, currText) -> updateEditWidth(currText));

            contentEdit.fontProperty().addListener(e -> updateEditWidth(contentEdit.getText()));

            contentShow.textProperty().bindBidirectional(text);
            contentEdit.textProperty().bindBidirectional(contentShow.textProperty());

            showMode.setOnMouseEntered(e -> edit.setVisible(true));
            showMode.setOnMouseExited(e -> edit.setVisible(false));

            valid.addListener(c ->
            {
                if (!valid.get()) {
                    contentEdit.getStyleClass().add("invalid");
                } else {
                    contentEdit.getStyleClass().remove("invalid");
                }
            });

            contentEdit.setOnKeyPressed(e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    save();
                    e.consume();
                } else if (e.getCode().equals(KeyCode.ESCAPE)) {
                    cancel();
                    e.consume();
                }
            });
        } else if (mode.equals(Mode.READONLY)) {
            contentShow = new Label();
            contentShow.setId("contentShow");
            contentShow.textProperty().bindBidirectional(text);
            getChildren().add(contentShow);
        } else if (mode.equals(Mode.EDITOR)) {
            contentEdit = new TextField();
            contentEdit.setId("contentEdit");
            contentEdit.textProperty().bindBidirectional(text);
            contentEdit.textProperty().addListener((ov, prevText, currText) -> {
                updateEditWidth(currText);
                save();
            });
            contentEdit.fontProperty().addListener(e -> updateEditWidth(contentEdit.getText()));
            getChildren().add(contentEdit);
        }


    }

    private void updateEditWidth(String newText) {
        Platform.runLater(() -> {
            Text text = new Text(newText);
            text.setFont(contentEdit.getFont()); // Set the same font, so the size is the same
            double width = text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                    + contentEdit.getPadding().getLeft() + contentEdit.getPadding().getRight() // Add the padding of the TextField
                    + 2d; // Add some spacing
            contentEdit.setPrefWidth(width); // Set the width
            contentEdit.positionCaret(contentEdit.getCaretPosition()); // If you remove this line, it flashes a little bit
        });
    }

    @FXML
    protected void edit() {
        contentShow.textProperty().unbindBidirectional(text);
        contentEdit.selectAll();
        Platform.runLater(() -> contentEdit.requestFocus());
        editing.set(true);
    }

    @FXML
    protected void save() {

        if (mode.get().equals(Mode.EDITABLE)) {
            text.set(contentEdit.getText());
            contentShow.textProperty().bindBidirectional(text);
            editing.set(false);
        }

    }

    @FXML
    protected void cancel() {
        contentShow.textProperty().bindBidirectional(text);
        editing.set(false);
    }

    public enum Mode {
        READONLY,   //Just show value
        EDITABLE,   //Allow for editing
        EDITOR      //Default to editing
    }


}
