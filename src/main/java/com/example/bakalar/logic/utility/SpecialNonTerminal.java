package com.example.bakalar.logic.utility;


import com.example.bakalar.logic.conversion.CustomText;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SpecialNonTerminal extends NonTerminal {
    private MySymbol stateSymbolFrom;
    private MySymbol stackSymbol;
    private MySymbol stateSymbolTo;

    public SpecialNonTerminal() {
        super();
        this.stateSymbolFrom = new MySymbol("_");
        this.stackSymbol = new MySymbol("_");
        this.stateSymbolTo = new MySymbol("_");
    }

    public SpecialNonTerminal(String stateSymbolFrom, String stackSymbol, String stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? new MySymbol("_") : new MySymbol(stateSymbolFrom);
        this.stackSymbol = stackSymbol == null ? new MySymbol("_") : new MySymbol(stackSymbol);
        this.stateSymbolTo = stateSymbolTo == null ? new MySymbol("_") : new MySymbol(stateSymbolTo);
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, MySymbol stackSymbol, String stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? new MySymbol("_") : stateSymbolFrom;
        this.stackSymbol = stackSymbol == null ? new MySymbol("_") : stackSymbol;
        this.stateSymbolTo = stateSymbolTo == null ? new MySymbol("_") : new MySymbol(stateSymbolTo);
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, MySymbol stackSymbol, MySymbol stateSymbolTo) {
        this.stateSymbolFrom = stateSymbolFrom == null ? new MySymbol("_") : stateSymbolFrom;
        this.stackSymbol = stackSymbol == null ? new MySymbol("_") : stackSymbol;
        this.stateSymbolTo = stateSymbolTo == null ? new MySymbol("_") : stateSymbolTo;
    }

    public SpecialNonTerminal(MySymbol stateSymbolFrom, Color fromColor, MySymbol stackSymbol, Color stackColor, MySymbol stateSymbolTo, Color toColor) {
        this.stateSymbolFrom = stateSymbolFrom == null ? new MySymbol("_") : new MySymbol(stateSymbolFrom, fromColor);
        this.stackSymbol = stackSymbol == null ? new MySymbol("_") : new MySymbol(stackSymbol, stackColor);
        this.stateSymbolTo = stateSymbolTo == null ? new MySymbol("_") : new MySymbol(stateSymbolTo, toColor);
    }

    @Override
    public NonTerminal getDeepCopy() {
        return new SpecialNonTerminal(new MySymbol(stateSymbolFrom.getName(), stateSymbolFrom.getColor()),
                new MySymbol(stackSymbol.getName(), stackSymbol.getColor()),
                new MySymbol(stateSymbolTo.getName(), stateSymbolTo.getColor()));
    }

    @Override
    public List<Text> createText(int fontSize) {
        List<Text> texts = new ArrayList<>();
        texts.add(new CustomText("[", fontSize));
        texts.add(new CustomText(this.getStateSymbolFrom(), fontSize));
        texts.add(new CustomText(", ", fontSize));
        texts.add(new CustomText(this.getStackSymbol(), fontSize));
        texts.add(new CustomText(", ", fontSize));
        texts.add(new CustomText(this.getStateSymbolTo(), fontSize));
        texts.add(new CustomText("]", fontSize));
        return texts;
    }

    @Override
    public void resetColor() {
        stateSymbolFrom.setColor(MySymbol.DEFAULT_COLOR);
        stackSymbol.setColor(MySymbol.DEFAULT_COLOR);
        stateSymbolTo.setColor(MySymbol.DEFAULT_COLOR);
    }

    @Override
    public String toString() {
        return "[" + stateSymbolFrom + ", " + stackSymbol + ", " + stateSymbolTo + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialNonTerminal that)) return false;

        if (getStateSymbolFrom() != null ? !getStateSymbolFrom().equals(that.getStateSymbolFrom()) : that.getStateSymbolFrom() != null)
            return false;
        if (getStackSymbol() != null ? !getStackSymbol().equals(that.getStackSymbol()) : that.getStackSymbol() != null)
            return false;
        return getStateSymbolTo() != null ? getStateSymbolTo().equals(that.getStateSymbolTo()) : that.getStateSymbolTo() == null;
    }

    @Override
    public int hashCode() {
        int result = getStateSymbolFrom() != null ? getStateSymbolFrom().hashCode() : 0;
        result = 31 * result + (getStackSymbol() != null ? getStackSymbol().hashCode() : 0);
        result = 31 * result + (getStateSymbolTo() != null ? getStateSymbolTo().hashCode() : 0);
        return result;
    }
}
