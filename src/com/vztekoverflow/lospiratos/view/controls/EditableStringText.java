package com.vztekoverflow.lospiratos.view.controls;

import javafx.beans.NamedArg;
import javafx.beans.property.StringProperty;

public class EditableStringText extends EditableText {

    public EditableStringText(@NamedArg(value = "rightToLeft", defaultValue = "false") boolean rightToLeft) {
        super(rightToLeft);
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
    }
}
