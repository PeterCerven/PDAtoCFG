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
            if(so1.getStackSymbol().getName().compareTo(so2.getStackSymbol().getName()) != 0) {
                return so1.getStackSymbol().getName().compareTo(so2.getStackSymbol().getName());
            }
            if (so1.getStateSymbolFrom().getName().compareTo(so2.getStateSymbolFrom().getName()) != 0) {
                return so1.getStateSymbolFrom().getName().compareTo(so2.getStateSymbolFrom().getName());
            }
            if (so1.getStateSymbolTo().getName().compareTo(so2.getStateSymbolTo().getName()) != 0) {
                return so1.getStateSymbolTo().getName().compareTo(so2.getStateSymbolTo().getName());
            }
        }
        return o1.getSymbol().getName().compareTo(o2.getSymbol().getName());
    }
}
