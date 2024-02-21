package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
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
    private String terminal;
    private List<SpecialNonTerminal> rightSide;
    private List<CFGRule> steps;

    public CFGRule() {
        this.steps = new ArrayList<>();
        this.rightSide = new ArrayList<>();
    }

    public CFGRule(SpecialNonTerminal leftSide, String terminal, List<SpecialNonTerminal> rightSide) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
    }

    public CFGRule(MySymbol mySymbolLeftSide, String terminal, List<SpecialNonTerminal> rightSide) {
        this.mySymbolLeftSide = mySymbolLeftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
    }

    public SpecialNonTerminal copyLeftSide() {
        return new SpecialNonTerminal(leftSide.getStateSymbolFrom(), leftSide.getStackSymbol(), leftSide.getStateSymbolTo());
    }

    public List<SpecialNonTerminal> copyRightSide() {
        List<SpecialNonTerminal> copy = new ArrayList<>();
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            copy.add(new SpecialNonTerminal(specialNonTerminal.getStateSymbolFrom(), specialNonTerminal.getStackSymbol(), specialNonTerminal.getStateSymbolTo()));
        }
        return copy;
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
