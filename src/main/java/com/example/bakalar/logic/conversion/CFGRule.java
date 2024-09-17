package com.example.bakalar.logic.conversion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.bakalar.logic.MainLogic.STARTING_S;


@Builder
@Getter
@Setter
@AllArgsConstructor
public class CFGRule implements Comparable<CFGRule> {
    private NonTerminal leftSide;
    private MySymbol terminal;
    private List<NonTerminal> rightSide;
    private List<StepRule> steps;

    public  CFGRule() {
        this.steps = new ArrayList<>();
        this.rightSide = new ArrayList<>();
    }

    public CFGRule(NonTerminal leftSide, MySymbol terminal, List<NonTerminal> rightSide) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
    }

    public CFGRule getDeepCopy() {
        CFGRule rule = new CFGRule();
        rule.setLeftSide(this.copyLeftSide());
        rule.setTerminal(this.copyTerminal());
        rule.setRightSide(this.copyRightSide());
        rule.setSteps(new ArrayList<>(this.steps));
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
    public List<String> getRightSideString() {
        List<String> rightSideString = new ArrayList<>();
        rightSideString.add(terminal == null ? "" : terminal.getName());
        for (NonTerminal nonTerminal : rightSide) {
            rightSideString.add(nonTerminal.getSymbol().getName());
        }
        return rightSideString;
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


    @Override
    public int compareTo(CFGRule other) {
        NonTerminal left1 = this.getLeftSide();
        NonTerminal left2 = other.getLeftSide();

        List<NonTerminal> rightList1 = this.getRightSide();
        List<NonTerminal> rightList2 = other.getRightSide();

        MySymbol terminal1 = this.getTerminal();
        MySymbol terminal2 = other.getTerminal();

        // Left Side NonTerminal
        if (!(left1 instanceof SpecialNonTerminal) && !(left2 instanceof SpecialNonTerminal)) {
            // Same left Side
            if (left1.getSymbol().equals(left2.getSymbol())) {
                // empty check
                if (rightList1.isEmpty() && rightList2.isEmpty()) {
                    if (terminal1 == null) {
                        return 1;
                    }
                    if (terminal2 == null) {
                        return -1;
                    }
                    return terminal1.compareTo(terminal2);
                }


                // Different left Side    
            } else {
                if (left1.getSymbol().getName().equals(STARTING_S)) {
                    return 1;
                }
                if (left2.getSymbol().getName().equals(STARTING_S)) {
                    return -1;
                }
                return left1.getSymbol().compareTo(left2.getSymbol());
            }

        }

        // Comparing LeftSide between SpecialNonTerminal and NonTerminal
        if (!(left1 instanceof SpecialNonTerminal leftSpecial1)) {
            return -1;
        }
        if (!(left2 instanceof SpecialNonTerminal leftSpecial2)) {
            return 1;
        }

        // converting to SpecialNonTerminal
        List<SpecialNonTerminal> rightSpecialList1 = new ArrayList<>();
        List<SpecialNonTerminal> rightSpecialList2 = new ArrayList<>();


        for (NonTerminal nonTerminal : rightList1) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                rightSpecialList1.add(snt);
            }
        }

        for (NonTerminal nonTerminal : rightList2) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                rightSpecialList2.add(snt);
            }
        }

        // SpecialNonTerminal Comparison
        if (!leftSpecial1.getStateFrom().equals(leftSpecial2.getStateFrom())) {
            return leftSpecial1.getStateFrom()
                    .compareTo(leftSpecial2.getStateFrom());
        }
        if (!rightList1.isEmpty() && !rightList2.isEmpty()) {
            if (!rightSpecialList1.get(0).getStateFrom().equals(rightSpecialList2.get(0).getStateFrom())) {
                return rightSpecialList1.get(0).getStateFrom()
                        .compareTo(rightSpecialList2.get(0).getStateFrom());
            }
        }
        if (rightSpecialList1.size() != rightSpecialList2.size()) {
            return rightSpecialList2.size() - rightSpecialList1.size();
        }
        if (!terminal1.equals(terminal2)) {
            return this.getTerminal().compareTo(other.getTerminal());
        }
        if (!leftSpecial1.getStack().equals(leftSpecial2.getStack())) {
            return leftSpecial1.getStack()
                    .compareTo(leftSpecial2.getStack());
        }
        if (!leftSpecial1.getStateTo().equals(leftSpecial2.getStateTo())) {
            return leftSpecial1.getStateTo()
                    .compareTo(leftSpecial2.getStateTo());
        }
        for (int i = 0; i < rightSpecialList1.size(); i++) {
            if (!rightSpecialList1.get(i).getStack().equals(rightSpecialList2.get(i).getStack())) {
                return rightSpecialList1.get(i).getStack()
                        .compareTo(rightSpecialList2.get(i).getStack());
            }
        }
        for (int i = 1; i < rightSpecialList1.size(); i++) {
            if (!rightSpecialList1.get(i).getStateFrom().equals(rightSpecialList2.get(i).getStateFrom())) {
                return rightSpecialList1.get(i).getStateFrom()
                        .compareTo(rightSpecialList2.get(i).getStateFrom());
            }
        }
        return 0;
    }
}
