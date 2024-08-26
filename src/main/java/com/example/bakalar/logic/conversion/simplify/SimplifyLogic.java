package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        for (CFGRule rule : gc.rules()) {
            rules.add(rule.getDeepCopy());
        }
        NonTerminal startingSymbol = gc.startingSymbol().getDeepCopy();
        Set<MySymbol> terminals = gc.terminals().stream().map(MySymbol::getDeepCopy).collect(Collectors.toSet());
        Set<NonTerminal> nonTerminals = gc.nonTerminals().stream().map(NonTerminal::getDeepCopy).collect(Collectors.toSet());
        return new GrammarComponents(rules, startingSymbol, terminals, nonTerminals);
    }


}
