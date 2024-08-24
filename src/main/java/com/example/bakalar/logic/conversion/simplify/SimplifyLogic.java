package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import lombok.Getter;

import java.util.*;
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

        Map<SpecialNonTerminal, Character> nameForTerminals = new HashMap<>();

        for (NonTerminal nonTerminal : allNonTerminals) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                if (prohibitedLetters.contains(Character.toString(namingLetter))) {
                    namingLetter++;
                }
                nameForTerminals.put(snt, namingLetter++);
            }
        }

        // rules
        for (CFGRule rule : allRules) {
            // right side
            List<NonTerminal> newRightSide = new ArrayList<>();
            for (NonTerminal nonTerminal : rule.getRightSide()) {
                if (nonTerminal instanceof SpecialNonTerminal snt) {
                    Character name = nameForTerminals.get(snt);
                    if (name != null) {
                        newRightSide.add(new NonTerminal(name.toString()));
                    }

                }
                rule.setRightSide(newRightSide);
            }
            // left side
            if (rule.getLeftSide() instanceof SpecialNonTerminal snt) {
                Character name = nameForTerminals.get(snt);
                if (name != null) {
                    rule.setLeftSide(new NonTerminal(name.toString()));
                }
            }
        }
        return gc;
    }

    private GrammarComponents reductionOfCFG(GrammarComponents gc) {
        removeAllNonTerminalsThatDontDeriveTerminals(gc);
        return gc;
    }

    private void removeAllNonTerminalsThatDontDeriveTerminals(GrammarComponents gc) {
        Set<NonTerminal> allowedNonTerminals = new HashSet<>();
        List<CFGRule> allRules = gc.getRules();
        for (CFGRule rule : allRules) {
            if (rule.getTerminal() != null) {
                allowedNonTerminals.add(rule.getLeftSide());
            }
        }
        int previousSize = allowedNonTerminals.size();
        int currentSize = previousSize++;
        while (previousSize < currentSize) {
            previousSize = currentSize;
            HashSet<NonTerminal> toAdd = new HashSet<>();
            for (CFGRule rule : allRules) {
                if (rule.getRightSide().stream().anyMatch(allowedNonTerminals::contains)) {
                    toAdd.add(rule.getLeftSide());
                }
            }
            allowedNonTerminals.addAll(toAdd);
            currentSize = allowedNonTerminals.size();
        }

        allRules
                .removeIf(
                        rule -> !allowedNonTerminals.contains(rule.getLeftSide())
                        && !allowedNonTerminals.containsAll(rule.getRightSide())
                );
    }

    private GrammarComponents RemovalOfNullProductions(GrammarComponents gc) {
        return gc;
    }

    private GrammarComponents removalOfUnitProductions(GrammarComponents gc) {
        return gc;
    }


}
