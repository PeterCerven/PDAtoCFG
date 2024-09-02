package com.example.bakalar.logic.utility.sorters;

import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;

import java.util.Comparator;

public class SpecialNonTerminalSorter implements Comparator<NonTerminal> {
    /**
        * [q₀, X, q₁] = SpecialNonTerminal leftSide
     */
    @Override
    public int compare(NonTerminal o1, NonTerminal o2) {
        if (o1 instanceof SpecialNonTerminal so1 && o2 instanceof SpecialNonTerminal so2) {
            if(so1.getStack().getName().compareTo(so2.getStack().getName()) != 0) {
                return so1.getStack().getName().compareTo(so2.getStack().getName());
            }
            if (so1.getStateFrom().getName().compareTo(so2.getStateFrom().getName()) != 0) {
                return so1.getStateFrom().getName().compareTo(so2.getStateFrom().getName());
            }
            if (so1.getStateTo().getName().compareTo(so2.getStateTo().getName()) != 0) {
                return so1.getStateTo().getName().compareTo(so2.getStateTo().getName());
            }
        }
        return o1.getSymbol().getName().compareTo(o2.getSymbol().getName());
    }
}
