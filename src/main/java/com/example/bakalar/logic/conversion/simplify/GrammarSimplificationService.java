package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
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
        removeObsoleteTerminalsAndNonTerminals(gc);
        return gc;
    }

    private void removeObsoleteTerminalsAndNonTerminals(GrammarComponents gc) {
        Set<NonTerminal> allowedNonTerminals = new HashSet<>();
        Set<MySymbol> allowedTerminals = new HashSet<>();
        List<CFGRule> allRules = gc.getRules();
        for (CFGRule rule : allRules) {
            allowedNonTerminals.add(rule.getLeftSide());
            allowedNonTerminals.addAll(rule.getRightSide());
            if (rule.getTerminal() != null) {
                allowedTerminals.add(rule.getTerminal());
            }
        }
        gc.setNonTerminals(allowedNonTerminals);
        gc.setTerminals(allowedTerminals);
    }


    private void removeAllNonTerminalsThatDontDeriveTerminals(GrammarComponents gc) {
        Set<NonTerminal> allowedNonTerminals = new HashSet<>();
        List<CFGRule> allRules = gc.getRules();
        for (CFGRule rule : allRules) {
            if (rule.getTerminal() != null) {
                allowedNonTerminals.add(rule.getLeftSide());
            }
        }
        int previousSize = 0;
        int currentSize = allowedNonTerminals.size();
        while (previousSize != currentSize) {
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
                                || !allowedNonTerminals.containsAll(rule.getRightSide())
                );
    }

    private void removeAllRulesThatCannotBeReachedFromStart(GrammarComponents gc) {
        List<CFGRule> allRules = gc.getRules();
        Set<NonTerminal> allowedNonTerminals = new HashSet<>();
        NonTerminal startSymbol = gc.getStartingSymbol();
        int currentSize = 1;
        int previousSize = 0;

        allowedNonTerminals.add(startSymbol);

        while (currentSize != previousSize) {
            previousSize = currentSize;
            Set<NonTerminal> toAdd = new HashSet<>();
            for (CFGRule rule : allRules) {
                if (allowedNonTerminals.contains(rule.getLeftSide())) {
                    toAdd.addAll(rule.getRightSide());
                }
            }
            allowedNonTerminals.addAll(toAdd);
            currentSize = allowedNonTerminals.size();
        }

        allRules
                .removeIf(
                        rule -> !allowedNonTerminals.contains(rule.getLeftSide())
                );


    }

    public GrammarComponents removalOfUnitProductions(GrammarComponents gc) {
        List<CFGRule> allRules = gc.getRules();
        int iteration = 0;
        while (iteration < allRules.size()) {
            List<CFGRule> rulesToAdd = new ArrayList<>();
            List<CFGRule> rulesToRemove = new ArrayList<>();
            CFGRule unitProductionRule = allRules.get(iteration++);
            if (!(unitProductionRule.getRightSide().size() == 1 && unitProductionRule.getTerminal() == null
                    || unitProductionRule.getRightSide().isEmpty() && unitProductionRule.getTerminal() != null)) {
                continue;
            }
            for (CFGRule rule : allRules) {
                if (rule.getRightSide().size() == 1 && rule.getRightSide().get(0).equals(unitProductionRule.getLeftSide()) && rule.getTerminal() == null) {
                    rulesToAdd.add(new CFGRule(rule.getLeftSide(), unitProductionRule.getTerminal(), unitProductionRule.getRightSide()));
                    rulesToRemove.add(rule);
                    rulesToRemove.add(unitProductionRule);
                    iteration = 0;
                    break;
                }
            }
            allRules.removeAll(rulesToRemove);
            allRules.addAll(rulesToAdd);
        }
        removeObsoleteTerminalsAndNonTerminals(gc);
        return gc;
    }

    public GrammarComponents removalOfNullProductions(GrammarComponents gc) {
        // Logic...
        return gc;
    }
}