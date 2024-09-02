package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.NonTerminal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SimplifyLogic {
    private List<GrammarComponents> grammarComponents;
    private final GrammarSimplificationService gss;

    public SimplifyLogic() {
        this.gss = new GrammarSimplificationService();
        this.grammarComponents = new ArrayList<>();
    }

    public void simplify(GrammarComponents gc) {
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
    }

    private GrammarComponents deepCopyGrammarComponents(GrammarComponents gc) {
        List<CFGRule> rules = new ArrayList<>();
        for (CFGRule rule : gc.getRules()) {
            rules.add(rule.getDeepCopy());
        }
        NonTerminal startingSymbol = gc.getStartingSymbol().getDeepCopy();
        return new GrammarComponents(rules, startingSymbol);
    }


}
