package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CFGRule {
    private SpecialNonTerminal leftSide;
    private MySymbol mySymbolLeftSide;
    private MySymbol terminal;
    private List<SpecialNonTerminal> rightSide;
    private List<CFGRule> steps;
    private Transition transition;

    public CFGRule() {
        this.steps = new ArrayList<>();
        this.rightSide = new ArrayList<>();
    }

    public CFGRule(SpecialNonTerminal leftSide, MySymbol terminal, List<SpecialNonTerminal> rightSide, Transition transition) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
    }

    public CFGRule(MySymbol mySymbolLeftSide, MySymbol terminal, List<SpecialNonTerminal> rightSide, Transition transition) {
        this.mySymbolLeftSide = mySymbolLeftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
    }

    public SpecialNonTerminal copyLeftSide() {
        return new SpecialNonTerminal(leftSide.getStateSymbolFrom(), leftSide.getStateSymbolFrom().getColor(),
                leftSide.getStackSymbol(), leftSide.getStackSymbol().getColor(),
                leftSide.getStateSymbolTo(), leftSide.getStateSymbolTo().getColor());
    }

    public List<SpecialNonTerminal> copyRightSide() {
        List<SpecialNonTerminal> copy = new ArrayList<>();
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            copy.add(new SpecialNonTerminal(
                    specialNonTerminal.getStateSymbolFrom(), specialNonTerminal.getStateSymbolFrom().getColor(),
                    specialNonTerminal.getStackSymbol(), specialNonTerminal.getStackSymbol().getColor(),
                    specialNonTerminal.getStateSymbolTo(), specialNonTerminal.getStateSymbolTo().getColor()));
        }
        return copy;
    }

    public MySymbol copyTerminal() {
        return new MySymbol(terminal.getName(), terminal.getColor());
    }

    public void resetFontColor() {
        leftSide.getStateSymbolFrom().setColor(MySymbol.DEFAULT_COLOR);
        leftSide.getStackSymbol().setColor(MySymbol.DEFAULT_COLOR);
        leftSide.getStateSymbolTo().setColor(MySymbol.DEFAULT_COLOR);
        if (terminal != null) {
            terminal.setColor(MySymbol.DEFAULT_COLOR);
        }
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            specialNonTerminal.getStateSymbolFrom().setColor(MySymbol.DEFAULT_COLOR);
            specialNonTerminal.getStackSymbol().setColor(MySymbol.DEFAULT_COLOR);
            specialNonTerminal.getStateSymbolTo().setColor(MySymbol.DEFAULT_COLOR);
        }
    }

    public List<Text> createTextFromStep() {
        List<Text> texts = new ArrayList<>();
        if (mySymbolLeftSide == null) {
            texts.add(new CustomText("["));
            texts.add(new CustomText(leftSide.getStateSymbolFrom()));
            texts.add(new CustomText(", "));
            texts.add(new CustomText(leftSide.getStackSymbol()));
            texts.add(new CustomText(", "));
            texts.add(new CustomText(leftSide.getStateSymbolTo()));
            texts.add(new CustomText("]"));
        } else {
            texts.add(new CustomText(mySymbolLeftSide));
        }
        texts.add(new CustomText(" -> "));
        if (terminal != null) {
            texts.add(new CustomText(terminal));
        }
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            texts.add(new CustomText("["));
            texts.add(new CustomText(specialNonTerminal.getStateSymbolFrom()));
            texts.add(new CustomText(", "));
            texts.add(new CustomText(specialNonTerminal.getStackSymbol()));
            texts.add(new CustomText(", "));
            texts.add(new CustomText(specialNonTerminal.getStateSymbolTo()));
            texts.add(new CustomText("]"));
        }
        return texts;
    }


    @Override
    public String toString() {
        if (mySymbolLeftSide == null) {
            return leftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
        } else {
            return mySymbolLeftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
        }
    }
}
