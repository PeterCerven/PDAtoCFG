package com.example.bakalar.logic.utility;

import com.example.bakalar.logic.transitions.Transition;

import java.util.Comparator;

public class TransitionSorter implements Comparator<Transition> {

    /**
     * δ(q₀, 0, X) = (q₁, X) Transition
     * MySymbol currentState -> q₀ Based on the current state
     * MySymbol inputSymbolToRead -> 0 not used
     * MySymbol symbolToPop -> X not used
     * MySymbol nextState -> q₁ Based on the next state
     * List<MySymbol> symbolsToPush -> X not used
     * WindowType windowType -> NORMAL not used
     */
    @Override
    public int compare(Transition o1, Transition o2) {
        if (o1.getCurrentState().getName().compareTo(o2.getCurrentState().getName()) != 0) {
            return o1.getCurrentState().getName().compareTo(o2.getCurrentState().getName());
        }
        if (o1.getNextState().getName().compareTo(o2.getNextState().getName()) != 0) {
            return o1.getNextState().getName().compareTo(o2.getNextState().getName());
        }
        return 0;
    }
}
