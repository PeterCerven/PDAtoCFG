package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class SimplifyLogic {
    private List<GrammarComponents> grammarComponents;

    public void simplify(GrammarComponents gc) {
        this.grammarComponents = new ArrayList<>();
        grammarComponents.add(gc);

        gc = changeSpecialTerminalsToNonTerminals(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = reductionOfCFG(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = removalOfUnitProductions(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
        gc = RemovalOfNullProductions(deepCopyGrammarComponents(gc));
        grammarComponents.add(gc);
    }

    private GrammarComponents deepCopyGrammarComponents(GrammarComponents gc) {
        List<CFGRule> rules = new ArrayList<>();
        for (CFGRule rule : gc.getRules()) {
            rules.add(rule.getDeepCopy());
        }
        MySymbol startingSymbol = gc.getStartingSymbol().getDeepCopy();
        Set<MySymbol> terminals = gc.getTerminals().stream().map(MySymbol::getDeepCopy).collect(Collectors.toSet());
        Set<NonTerminal> nonTerminals = gc.getNonTerminals().stream().map(NonTerminal::getDeepCopy).collect(Collectors.toSet());
        return new GrammarComponents(rules, startingSymbol, terminals, nonTerminals);
    }


    private GrammarComponents changeSpecialTerminalsToNonTerminals(GrammarComponents gc) {
        Set<NonTerminal> allNonTerminals = gc.getNonTerminals();
        List<CFGRule> allRules = gc.getRules();

        char namingLetter = 'A';
        String prohibitedLetters = "SZ";


        for (NonTerminal nonTerminal : allNonTerminals) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                if (prohibitedLetters.contains(namingLetter + "")) {
                    continue;
                }
                snt.setLetterName(namingLetter++ + "");
            }
        }

        // rules
        for (CFGRule rule : allRules) {
            // right side
            List<NonTerminal> newRightSide = new ArrayList<>();
            for (NonTerminal nonTerminal : rule.getRightSide()) {
                if (nonTerminal instanceof SpecialNonTerminal snt) {
                    NonTerminal setNonterminal = allNonTerminals.stream()
                            .filter(nt -> nt.equals(snt))
                            .findFirst()
                            .orElse(null);
                    if (setNonterminal instanceof SpecialNonTerminal snt2) {
                        newRightSide.add(new NonTerminal(new MySymbol(snt2.getLetterName())));
                    }

                }
                rule.setRightSide(newRightSide);
            }
            // left side
            if (rule.getLeftSide() instanceof SpecialNonTerminal snt) {
                NonTerminal setNonterminal = allNonTerminals.stream()
                        .filter(nt -> nt.equals(snt))
                        .findFirst()
                        .orElse(null);
                if (setNonterminal instanceof SpecialNonTerminal snt2) {
                    rule.setLeftSide(new NonTerminal(new MySymbol(snt2.getLetterName())));
                }
            }
        }
        return gc;
    }

    private GrammarComponents RemovalOfNullProductions(GrammarComponents gc) {
        return gc;
    }

    private GrammarComponents removalOfUnitProductions(GrammarComponents gc) {
        return gc;
    }

    private GrammarComponents reductionOfCFG(GrammarComponents gc) {
        return gc;
    }


}
