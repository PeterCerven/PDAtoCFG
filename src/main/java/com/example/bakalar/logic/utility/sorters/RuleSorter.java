package com.example.bakalar.logic.utility.sorters;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.utility.SpecialNonTerminal;

import java.util.Comparator;

public class RuleSorter implements Comparator<CFGRule> {
    /** [q₀, X, q₁] -> 1[q₀, X, q₁][q₁, X, q₁]
     *  [q₀, X, q₁] = SpecialNonTerminal leftSide / S MySymbol mySymbolLeftSide;
     * 1 = MySymbol terminal;
     * [q₀, X, q₁][q₁, X, q₁] = List<SpecialNonTerminal> rightSide;
     */
    @Override
    public int compare(CFGRule o1, CFGRule o2) {
        // starting
//        if (!(o1.getLeftSide() instanceof SpecialNonTerminal) && !(o2.getLeftSide() instanceof SpecialNonTerminal)) {
//            return o1.getRightSide().get(0).getStateSymbolTo().getName()
//                    .compareTo(o2.getRightSide().get(0).getStateSymbolTo().getName());
//        }
//        if (!(o1.getLeftSide() instanceof SpecialNonTerminal so1)) {
//            return -1;
//        }
//        if (!(o2.getLeftSide() instanceof SpecialNonTerminal so2)) {
//            return 1;
//        }
//
//        // normal
//        //
//        if (!so1.getStateSymbolFrom().getName().equals(so2.getStateSymbolFrom().getName())) {
//            return so1.getStateSymbolFrom().getName()
//                    .compareTo(so2.getStateSymbolFrom().getName());
//        }
//        if (!o1.getRightSide().isEmpty() && !o2.getRightSide().isEmpty()) {
//            if (!o1.getRightSide().get(0).getStateSymbolFrom().getName().equals(o2.getRightSide().get(0).getStateSymbolFrom().getName())) {
//                return o1.getRightSide().get(0).getStateSymbolFrom().getName()
//                        .compareTo(o2.getRightSide().get(0).getStateSymbolFrom().getName());
//            }
//        }
//        if (o1.getRightSide().size() != o2.getRightSide().size()) {
//            return o2.getRightSide().size() - o1.getRightSide().size();
//        }
//        if (!o1.getTerminal().getName().equals(o2.getTerminal().getName())) {
//            return o1.getTerminal().getName().compareTo(o2.getTerminal().getName());
//        }
//        if (!so1.getStackSymbol().getName().equals(so2.getStackSymbol().getName())) {
//            return so1.getStackSymbol().getName()
//                    .compareTo(so2.getStackSymbol().getName());
//        }
//        if (!so1.getStateSymbolTo().getName().equals(so2.getStateSymbolTo().getName())) {
//            return so1.getStateSymbolTo().getName()
//                    .compareTo(so2.getStateSymbolTo().getName());
//        }
//        for (int i = 0; i < o1.getRightSide().size(); i++) {
//            if (!o1.getRightSide().get(i).getStackSymbol().getName().equals(o2.getRightSide().get(i).getStackSymbol().getName())) {
//                return o1.getRightSide().get(i).getStackSymbol().getName()
//                        .compareTo(o2.getRightSide().get(i).getStackSymbol().getName());
//            }
//        }
//        for (int i = 1; i < o1.getRightSide().size(); i++) {
//            if (!o1.getRightSide().get(i).getStateSymbolFrom().getName().equals(o2.getRightSide().get(i).getStateSymbolFrom().getName())) {
//                return o1.getRightSide().get(i).getStateSymbolFrom().getName()
//                        .compareTo(o2.getRightSide().get(i).getStateSymbolFrom().getName());
//            }
//        }
        return 0;
    }
}
