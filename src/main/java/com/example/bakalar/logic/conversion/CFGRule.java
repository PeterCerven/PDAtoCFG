package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
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
    private List<NonTerminal> rightSide;
    private List<StepRule> steps;
    private Transition transition;

    public CFGRule() {
        this.steps = new ArrayList<>();
        this.rightSide = new ArrayList<>();
    }

    public CFGRule(NonTerminal leftSide, MySymbol terminal, List<NonTerminal> rightSide, Transition transition) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
    }

    public CFGRule(MySymbol mySymbolLeftSide, MySymbol terminal, List<NonTerminal> rightSide, Transition transition) {
        this.leftSide = new NonTerminal(mySymbolLeftSide);
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
    }

    public CFGRule getDeepCopy() {
        CFGRule rule = new CFGRule();
        rule.setLeftSide(this.copyLeftSide());
        rule.setTerminal(this.copyTerminal());
        rule.setRightSide(this.copyRightSide());
        rule.setTransition(this.getTransition());
        return rule;
    }

    public NonTerminal copyLeftSide() {
        return leftSide.getDeepCopy();

    }

    public List<NonTerminal> copyRightSide() {
        List<NonTerminal> copy = new ArrayList<>();
        for (NonTerminal nonTerminal : rightSide) {
            copy.add(nonTerminal.getDeepCopy());
        }
        return copy;
    }

    public MySymbol copyTerminal() {
        if (terminal == null) {
            return null;
        }
        return new MySymbol(terminal.getName(), terminal.getColor());
    }

    public void resetFontColor() {
        leftSide.resetColor();
        if (terminal != null) {
            terminal.setColor(MySymbol.DEFAULT_COLOR);
        }
        for (NonTerminal nonTerminal : rightSide) {
            nonTerminal.resetColor();
        }
    }

    @Override
    public String toString() {
        return leftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(NonTerminal::toString).collect(Collectors.joining());
    }
}
