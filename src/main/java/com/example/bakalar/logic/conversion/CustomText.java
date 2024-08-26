package com.example.bakalar.logic.conversion;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomText extends Text {

    public CustomText(MySymbol mySymbol, int size) {
        super(mySymbol.toString());
        this.setFill(mySymbol.getColor());
        this.setFont(new Font(size));
    }

    public CustomText(String text, int size) {
        super(text);
        this.setFont(new Font(size));
    }
}
