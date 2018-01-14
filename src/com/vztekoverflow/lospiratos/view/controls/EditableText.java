package com.vztekoverflow.lospiratos.view.controls;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class EditableText extends StackPane {

    private BooleanProperty editing = new SimpleBooleanProperty(false);

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }

    private StringProperty text = new SimpleStringProperty("bablbam");


    @FXML
    private TextField contentEdit;
    @FXML
    private Label contentShow;
    @FXML
    private BorderPane editMode;
    @FXML
    private BorderPane showMode;
    @FXML
    private Button edit;


    public EditableText() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "EditableText.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        getStyleClass().add("editable-text");

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.getChildren().remove(editMode);
        contentShow.textProperty().bindBidirectional(text);
        contentEdit.textProperty().bindBidirectional(contentShow.textProperty());
        editing.addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                this.getChildren().add(editMode);
                this.getChildren().remove(showMode);
            } else {
                this.getChildren().remove(editMode);
                this.getChildren().add(showMode);
            }
        });

        contentEdit.textProperty().addListener((ov, prevText, currText) -> {
            // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
            Platform.runLater(() -> {
                Text text = new Text(currText);
                text.setFont(contentEdit.getFont()); // Set the same font, so the size is the same
                double width = text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                        + contentEdit.getPadding().getLeft() + contentEdit.getPadding().getRight() // Add the padding of the TextField
                        + 2d; // Add some spacing
                contentEdit.setPrefWidth(width); // Set the width
                contentEdit.positionCaret(contentEdit.getCaretPosition()); // If you remove this line, it flashes a little bit
            });
        });

        //contentShow.minWidthProperty().bind(contentShow.prefWidthProperty());
        showMode.setOnMouseEntered(e ->  edit.setVisible(true));
        showMode.setOnMouseExited(e ->  edit.setVisible(false));



    }

    @FXML
    protected void edit()
    {
        contentShow.textProperty().unbindBidirectional(text);
        editing.set(true);
    }

    @FXML
    protected void save()
    {

        text.set(contentEdit.getText());
        contentShow.textProperty().bindBidirectional(text);
        editing.set(false);
    }

    @FXML
    protected void cancel()
    {
        contentShow.textProperty().bindBidirectional(text);
        editing.set(false);
    }

}
