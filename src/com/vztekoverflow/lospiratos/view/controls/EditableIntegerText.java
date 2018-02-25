package com.vztekoverflow.lospiratos.view.controls;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import java.text.ParseException;
import java.util.Locale;

public class EditableIntegerText extends EditableText {

    private NumberStringConverter nsc = new NumberStringConverter(new Locale("cs"));
    private IntegerProperty animated = new SimpleIntegerProperty();

    public EditableIntegerText(@NamedArg(value = "rightToLeft", defaultValue = "false") boolean rightToLeft) {
        super(rightToLeft);
        text.bindBidirectional(animated, nsc);

        value.addListener((observable, oldValue, newValue) -> {
            if(getMode().equals(Mode.READONLY))
            {
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(animated, oldValue)), new KeyFrame(Duration.seconds(1), new KeyValue(animated, newValue)));
                timeline.play();
            } else {
                animated.setValue(newValue);
            }
        });
    }


    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    private IntegerProperty value = new SimpleIntegerProperty();

    @Override
    protected void edit() {
        valid.set(true);
        super.edit();
    }

    @Override
    protected void save() {
        try {
            value.setValue(nsc.fromString(contentEdit.getText()));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ParseException) {
                valid.set(false);
                return;
            }
            throw e;

        }
        valid.set(true);
        super.save();
    }
}
