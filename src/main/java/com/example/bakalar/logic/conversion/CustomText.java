package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.utility.MySymbol;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomText extends Text {

    public CustomText(MySymbol mySymbol) {
        super(mySymbol.toString());
        this.setFill(mySymbol.getColor());
        this.setFont(new Font("Courier New", 22));
    }

    public CustomText(String text) {
        super(text);
        this.setFont(new Font("Courier New", 22));
    }
}
