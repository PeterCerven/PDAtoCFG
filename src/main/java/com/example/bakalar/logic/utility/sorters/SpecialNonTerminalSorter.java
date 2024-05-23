package com.example.bakalar.logic.utility.sorters;

import com.example.bakalar.logic.utility.SpecialNonTerminal;

import java.util.Comparator;

public class SpecialNonTerminalSorter implements Comparator<SpecialNonTerminal> {
    /**
        * [q₀, X, q₁] = SpecialNonTerminal leftSide
     */
    @Override
    public int compare(SpecialNonTerminal o1, SpecialNonTerminal o2) {
        if(o1.getStackSymbol().getName().compareTo(o2.getStackSymbol().getName()) != 0) {
            return o1.getStackSymbol().getName().compareTo(o2.getStackSymbol().getName());
        }
        if (o1.getStateSymbolFrom().getName().compareTo(o2.getStateSymbolFrom().getName()) != 0) {
            return o1.getStateSymbolFrom().getName().compareTo(o2.getStateSymbolFrom().getName());
        }
        if (o1.getStateSymbolTo().getName().compareTo(o2.getStateSymbolTo().getName()) != 0) {
            return o1.getStateSymbolTo().getName().compareTo(o2.getStateSymbolTo().getName());
        }
        return 0;
    }
}
