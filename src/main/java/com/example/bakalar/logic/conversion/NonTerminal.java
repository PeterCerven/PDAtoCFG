package com.example.bakalar.logic.conversion;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

import static com.example.bakalar.logic.MainLogic.EPSILON;


@Builder
@Getter
public class NonTerminal {
    private MySymbol symbol;

    public NonTerminal(MySymbol symbol) {
        this.symbol = symbol;
    }

    public NonTerminal(String symbol) {
        this.symbol = new MySymbol(symbol);
    }

    public NonTerminal() {
        this.symbol = new MySymbol(EPSILON);
    }

    public NonTerminal(String symbol, Color color) {
        this.symbol = new MySymbol(symbol, color);
    }

    public NonTerminal getDeepCopy() {
        return new NonTerminal(new MySymbol(symbol.getName(), symbol.getColor(), symbol.getIndex()));
    }


    public void resetColor() {
        symbol.setColor(MySymbol.DEFAULT_COLOR);
    }

    public List<Text> createText(int fontSize) {
        return List.of(new CustomText(symbol, fontSize));
    }

    @Override
    public String toString() {
        return symbol.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NonTerminal that)) return false;

        return Objects.equals(getSymbol(), that.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSymbol());
    }
}
