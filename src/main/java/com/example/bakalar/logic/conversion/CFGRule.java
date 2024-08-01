package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CFGRule {
    private NonTerminal leftSide;
    private MySymbol terminal;
    private List<SpecialNonTerminal> rightSide;
    private List<StepRule> steps;
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
        this.leftSide = new NonTerminal(mySymbolLeftSide);
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
    }

    public NonTerminal copyLeftSide() {
        return leftSide.getDeepCopy();

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
        leftSide.resetColor();
        if (terminal != null) {
            terminal.setColor(MySymbol.DEFAULT_COLOR);
        }
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            specialNonTerminal.getStateSymbolFrom().setColor(MySymbol.DEFAULT_COLOR);
            specialNonTerminal.getStackSymbol().setColor(MySymbol.DEFAULT_COLOR);
            specialNonTerminal.getStateSymbolTo().setColor(MySymbol.DEFAULT_COLOR);
        }
    }

    @Override
    public String toString() {
        return leftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
    }
}
