package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class SimplifyLogic {
    private List<StepRule> steps;

    public void simplify(Set<NonTerminal> allNonTerminals, Set<MySymbol> allTerminals, List<CFGRule> allRules, String startingS) {
//        removeUnreachable(allNonTerminals, allRules, startingS);
//        removeUnproductive(allNonTerminals, allRules);
    }

    private void showReducedCFGAndSteps() {
    }


}
