package com.example.bakalar.logic.conversion;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class CFGRule {
    private NonTerminal leftSide;
    private MySymbol terminal;
    private List<NonTerminal> rightSide;
    private List<StepRule> steps;

    public CFGRule() {
        this.steps = new ArrayList<>();
        this.rightSide = new ArrayList<>();
    }

    public CFGRule(NonTerminal leftSide, MySymbol terminal, List<NonTerminal> rightSide) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
    }

    public CFGRule(NonTerminal leftSide, MySymbol terminal, List<NonTerminal> rightSide, List<StepRule> steps) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = steps;
    }

    public CFGRule getDeepCopy() {
        CFGRule rule = new CFGRule();
        rule.setLeftSide(this.copyLeftSide());
        rule.setTerminal(this.copyTerminal());
        rule.setRightSide(this.copyRightSide());
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CFGRule cfgRule)) return false;

        return getLeftSide().equals(cfgRule.getLeftSide()) && Objects.equals(getTerminal(), cfgRule.getTerminal()) && getRightSide().equals(cfgRule.getRightSide());
    }

    @Override
    public int hashCode() {
        int result = getLeftSide().hashCode();
        result = 31 * result + Objects.hashCode(getTerminal());
        result = 31 * result + getRightSide().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return leftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(NonTerminal::toString).collect(Collectors.joining());
    }
}
