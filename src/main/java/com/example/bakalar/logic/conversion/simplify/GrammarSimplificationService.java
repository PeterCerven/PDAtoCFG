package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;

import java.util.*;

public class GrammarSimplificationService {

    public GrammarComponents changeSpecialTerminalsToNonTerminals(GrammarComponents gc) {
        Set<NonTerminal> allNonTerminals = gc.getNonTerminals();
        List<CFGRule> allRules = gc.getRules();

        char namingLetter = 'A';
        String prohibitedLetters = "SZ";

        Map<SpecialNonTerminal, NonTerminal> nameForTerminals = new HashMap<>();
        Set<NonTerminal> newNonTerminals = new HashSet<>();

        for (NonTerminal nonTerminal : allNonTerminals) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                if (prohibitedLetters.contains(Character.toString(namingLetter))) {
                    namingLetter++;
                }
                nameForTerminals.put(snt, new NonTerminal(String.valueOf(namingLetter++)));
            } else {
                newNonTerminals.add(nonTerminal);
            }
        }
        newNonTerminals.addAll(nameForTerminals.values());
        gc.setNonTerminals(newNonTerminals);

        // rules
        for (CFGRule rule : allRules) {
            // right side
            List<NonTerminal> newRightSide = new ArrayList<>();
            for (NonTerminal nonTerminal : rule.getRightSide()) {
                if (nonTerminal instanceof SpecialNonTerminal snt) {
                    NonTerminal newNonTerminal = nameForTerminals.get(snt);
                    if (newNonTerminal != null) {
                        newRightSide.add(newNonTerminal);
                    }

                }
                rule.setRightSide(newRightSide);
            }
            // left side
            if (rule.getLeftSide() instanceof SpecialNonTerminal snt) {
                NonTerminal newNonTerminal = nameForTerminals.get(snt);
                if (newNonTerminal != null) {
                    rule.setLeftSide(new NonTerminal(newNonTerminal.toString()));
                }
            }
        }
        return gc;
    }

    public GrammarComponents reductionOfCFG(GrammarComponents gc) {
        removeAllNonTerminalsThatDontDeriveTerminals(gc);
        removeAllRulesThatCannotBeReachedFromStart(gc);
        return gc;
    }

    private void removeAllRulesThatCannotBeReachedFromStart(GrammarComponents gc) {
        List<CFGRule> allRules = gc.getRules();
        NonTerminal startSymbol = gc.getStartingSymbol();
        Set<NonTerminal> allowedNonTerminals = new HashSet<>();
        int currentSize = 1;
        int previousSize = 0;

        allowedNonTerminals.add(startSymbol);

        while (currentSize > previousSize) {
            previousSize = currentSize;
            for (CFGRule rule : allRules) {
                if (allowedNonTerminals.contains(rule.getLeftSide())) {
                    allowedNonTerminals.addAll(rule.getRightSide());
                }
            }
            currentSize = allowedNonTerminals.size();
        }

        allRules
                .removeIf(
                        rule -> !allowedNonTerminals.contains(rule.getLeftSide())
                );


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

    public GrammarComponents removalOfUnitProductions(GrammarComponents gc) {
        // Logic...
        return gc;
    }

    public GrammarComponents removalOfNullProductions(GrammarComponents gc) {
        // Logic...
        return gc;
    }
}