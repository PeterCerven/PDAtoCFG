package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.NonTerminal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter
public class SimplifyLogic {
    private List<GrammarComponents> grammarComponents;
    private final GrammarSimplificationService gss;

    public SimplifyLogic() {
        this.gss = new GrammarSimplificationService();
        this.grammarComponents = new ArrayList<>();
    }

    public GrammarComponents simplify(GrammarComponents gc) {
        this.grammarComponents = new ArrayList<>();
        grammarComponents.add(gc);

        gc = gss.changeSpecialTerminalsToNonTerminals(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = gss.reductionOfCFG(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = gss.removalOfUnitProductions(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = gss.removalOfNullProductions(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = gss.reductionOfCFG(deepCopyGrammarComponents(gc));
        return gc;
    }

    private GrammarComponents deepCopyGrammarComponents(GrammarComponents gc) {
        Set<CFGRule> rules = new TreeSet<>();
        for (CFGRule rule : gc.getRules()) {
            rules.add(rule.getDeepCopy());
        }
        NonTerminal startingSymbol = gc.getStartingSymbol().getDeepCopy();
        return new GrammarComponents(rules, startingSymbol);
    }


}
