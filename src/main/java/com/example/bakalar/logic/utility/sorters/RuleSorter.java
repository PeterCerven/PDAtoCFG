package com.example.bakalar.logic.utility.sorters;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.bakalar.logic.MainLogic.STARTING_S;

public class RuleSorter implements Comparator<CFGRule> {
    /**
     * Option1:
     * [q₀, X, q₁] -> 1[q₀, X, q₁][q₁, X, q₁]
     * [q₀, X, q₁] = NonTerminal (SpecialNonTerminal) / S NonTerminal;
     * 1 = MySymbol terminal;
     * [q₀, X, q₁][q₁, X, q₁] = List<SpecialNonTerminal> rightSide;
     * Option2:
     * S -> 1AB
     * S,A,B = NonTerminal
     * 1 = MySymbol Terminal
     * AB = List<NonTerminal> rightSide;
     */
    @Override
    public int compare(CFGRule rule1, CFGRule rule2) {
        NonTerminal left1 = rule1.getLeftSide();
        NonTerminal left2 = rule2.getLeftSide();

        List<NonTerminal> rightList1 = rule1.getRightSide();
        List<NonTerminal> rightList2 = rule2.getRightSide();

        MySymbol terminal1 = rule1.getTerminal();
        MySymbol terminal2 = rule2.getTerminal();
        
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
            return rule1.getTerminal().compareTo(rule2.getTerminal());
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
