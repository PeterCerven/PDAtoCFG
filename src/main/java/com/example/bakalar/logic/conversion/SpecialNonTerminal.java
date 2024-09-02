package com.example.bakalar.logic.conversion;


import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder(builderMethodName = "specialNonTerminalBuilder")
@Getter
@Setter
public class SpecialNonTerminal extends NonTerminal {
    private MySymbol stateFrom;
    private MySymbol stack;
    private MySymbol stateTo;

    public SpecialNonTerminal() {
        super();
        this.stateFrom = new MySymbol("_");
        this.stack = new MySymbol("_");
        this.stateTo = new MySymbol("_");
    }

    public SpecialNonTerminal(String stateFrom, String stack, String stateTo) {
        super();
        this.stateFrom = stateFrom == null ? new MySymbol("_") : new MySymbol(stateFrom);
        this.stack = stack == null ? new MySymbol("_") : new MySymbol(stack);
        this.stateTo = stateTo == null ? new MySymbol("_") : new MySymbol(stateTo);
    }

    public SpecialNonTerminal(MySymbol stateFrom, MySymbol stack, String stateTo) {
        super();
        this.stateFrom = stateFrom == null ? new MySymbol("_") : stateFrom;
        this.stack = stack == null ? new MySymbol("_") : stack;
        this.stateTo = stateTo == null ? new MySymbol("_") : new MySymbol(stateTo);
    }

    public SpecialNonTerminal(MySymbol stateFrom, MySymbol stack, MySymbol stateTo) {
        super();
        this.stateFrom = stateFrom == null ? new MySymbol("_") : stateFrom;
        this.stack = stack == null ? new MySymbol("_") : stack;
        this.stateTo = stateTo == null ? new MySymbol("_") : stateTo;
    }

    public SpecialNonTerminal(String symbol, Color color, MySymbol stateFrom, MySymbol stack, MySymbol stateTo) {
        super(symbol, color);
        this.stateFrom = stateFrom;
        this.stack = stack;
        this.stateTo = stateTo;
    }

    public SpecialNonTerminal(String[] array) {
    }

    @Override
    public NonTerminal getDeepCopy() {
        return new SpecialNonTerminal(new MySymbol(stateFrom.getName(), stateFrom.getColor()),
                new MySymbol(stack.getName(), stack.getColor()),
                new MySymbol(stateTo.getName(), stateTo.getColor()));
    }

    @Override
    public List<Text> createText(int fontSize) {
        List<Text> texts = new ArrayList<>();
        texts.add(new CustomText("[", fontSize));
        texts.add(new CustomText(this.getStateFrom(), fontSize));
        texts.add(new CustomText(", ", fontSize));
        texts.add(new CustomText(this.getStack(), fontSize));
        texts.add(new CustomText(", ", fontSize));
        texts.add(new CustomText(this.getStateTo(), fontSize));
        texts.add(new CustomText("]", fontSize));
        return texts;
    }

    @Override
    public void resetColor() {
        stateFrom.setColor(MySymbol.DEFAULT_COLOR);
        stack.setColor(MySymbol.DEFAULT_COLOR);
        stateTo.setColor(MySymbol.DEFAULT_COLOR);
    }

    @Override
    public String toString() {
        return "[" + stateFrom + ", " + stack + ", " + stateTo + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpecialNonTerminal that)) return false;

        if (getStateFrom() != null ? !getStateFrom().equals(that.getStateFrom()) : that.getStateFrom() != null)
            return false;
        if (getStack() != null ? !getStack().equals(that.getStack()) : that.getStack() != null)
            return false;
        return getStateTo() != null ? getStateTo().equals(that.getStateTo()) : that.getStateTo() == null;
    }

    @Override
    public int hashCode() {
        int result = getStateFrom() != null ? getStateFrom().hashCode() : 0;
        result = 31 * result + (getStack() != null ? getStack().hashCode() : 0);
        result = 31 * result + (getStateTo() != null ? getStateTo().hashCode() : 0);
        return result;
    }
}
