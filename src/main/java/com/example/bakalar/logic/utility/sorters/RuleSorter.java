package com.example.bakalar.logic.utility.sorters;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.SpecialNonTerminal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.bakalar.logic.Board.STARTING_S;

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
        if (!leftSpecial1.getStateSymbolFrom().equals(leftSpecial2.getStateSymbolFrom())) {
            return leftSpecial1.getStateSymbolFrom()
                    .compareTo(leftSpecial2.getStateSymbolFrom());
        }
        if (!rightList1.isEmpty() && !rightList2.isEmpty()) {
            if (!rightSpecialList1.get(0).getStateSymbolFrom().equals(rightSpecialList2.get(0).getStateSymbolFrom())) {
                return rightSpecialList1.get(0).getStateSymbolFrom()
                        .compareTo(rightSpecialList2.get(0).getStateSymbolFrom());
            }
        }
        if (rightSpecialList1.size() != rightSpecialList2.size()) {
            return rightSpecialList2.size() - rightSpecialList1.size();
        }
        if (!terminal1.equals(terminal2)) {
            return rule1.getTerminal().compareTo(rule2.getTerminal());
        }
        if (!leftSpecial1.getStackSymbol().equals(leftSpecial2.getStackSymbol())) {
            return leftSpecial1.getStackSymbol()
                    .compareTo(leftSpecial2.getStackSymbol());
        }
        if (!leftSpecial1.getStateSymbolTo().equals(leftSpecial2.getStateSymbolTo())) {
            return leftSpecial1.getStateSymbolTo()
                    .compareTo(leftSpecial2.getStateSymbolTo());
        }
        for (int i = 0; i < rightSpecialList1.size(); i++) {
            if (!rightSpecialList1.get(i).getStackSymbol().equals(rightSpecialList2.get(i).getStackSymbol())) {
                return rightSpecialList1.get(i).getStackSymbol()
                        .compareTo(rightSpecialList2.get(i).getStackSymbol());
            }
        }
        for (int i = 1; i < rightSpecialList1.size(); i++) {
            if (!rightSpecialList1.get(i).getStateSymbolFrom().equals(rightSpecialList2.get(i).getStateSymbolFrom())) {
                return rightSpecialList1.get(i).getStateSymbolFrom()
                        .compareTo(rightSpecialList2.get(i).getStateSymbolFrom());
            }
        }
        return 0;
    }
}
