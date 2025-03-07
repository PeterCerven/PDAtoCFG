package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.bakalar.logic.MainLogic.EPSILON;

public class GrammarSimplificationService {

    public GrammarComponents changeSpecialTerminalsToNonTerminals(GrammarComponents gc) {
        Set<NonTerminal> allNonTerminals = gc.getNonTerminals();
        Set<CFGRule> allRules = gc.getRules();

        int namingLetter = 65;
        String prohibitedLetters = "SZ";

        Map<SpecialNonTerminal, NonTerminal> nameForTerminals = new HashMap<>();
        Set<NonTerminal> newNonTerminals = new HashSet<>();

        for (NonTerminal nonTerminal : allNonTerminals) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                nameForTerminals.put(snt, new NonTerminal(getNewNonTerminalName(namingLetter++, prohibitedLetters)));
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

    private String getNewNonTerminalName(int namingLetter, String prohibitedLetters) {
        int index = 8320;
        if (prohibitedLetters.contains(Character.toString(namingLetter))) {
            namingLetter++;
        }
        int temp = namingLetter;
        int diff = 'Z' - 'A' + 1;
        while (temp > 90) {
            temp -= diff;
            index++;
            if (prohibitedLetters.contains(Character.toString(temp))) {
                temp++;
            }
        }

        return index == 8320 ? String.valueOf((char) temp) : (char) namingLetter + "" + (char) index;
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
        Set<CFGRule> allRules = gc.getRules();
        for (CFGRule rule : allRules) {
            allowedNonTerminals.add(rule.getLeftSide());
            allowedNonTerminals.addAll(rule.getRightSide());
            if (rule.getTerminal() != null && !rule.getTerminal().getName().equals(EPSILON)) {
                allowedTerminals.add(rule.getTerminal());
            }
        }
        gc.setNonTerminals(allowedNonTerminals);
        gc.setTerminals(allowedTerminals);
    }


    private void removeAllNonTerminalsThatDontDeriveTerminals(GrammarComponents gc) {
        Set<NonTerminal> allowedNonTerminals = new HashSet<>();
        Set<CFGRule> allRules = gc.getRules();
        for (CFGRule rule : allRules) {
            if (rule.getTerminal() != null && rule.getRightSide().isEmpty()) {
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
        Set<CFGRule> allRules = gc.getRules();
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
        Set<CFGRule> allRules = gc.getRules();
        List<CFGRule> allRulesList = new ArrayList<>(allRules);
        int iteration = 0;
        while (iteration < allRulesList.size()) {
            Set<CFGRule> rulesToAdd = new HashSet<>();
            Set<CFGRule> rulesToRemove = new HashSet<>();
            CFGRule unitProductionRule = allRulesList.get(iteration++);
            if (!(unitProductionRule.getRightSide().size() == 1 && unitProductionRule.getTerminal() == null
                    || unitProductionRule.getRightSide().isEmpty() && unitProductionRule.getTerminal() != null)) {
                continue;
            }
            for (CFGRule rule : allRulesList) {
                if (rule.getRightSide().size() == 1 && rule.getRightSide().getFirst().equals(unitProductionRule.getLeftSide()) && rule.getTerminal() == null) {
                    rulesToAdd.add(new CFGRule(rule.getLeftSide(), unitProductionRule.getTerminal(), unitProductionRule.getRightSide()));
                    rulesToRemove.add(rule);
                    rulesToRemove.add(unitProductionRule);
                    iteration = 0;
                    break;
                }
            }
            allRulesList.removeAll(rulesToRemove);
            allRulesList.addAll(rulesToAdd);
        }
        gc.setRules(new TreeSet<>(allRulesList));
        removeObsoleteTerminalsAndNonTerminals(gc);
        return gc;
    }

    public GrammarComponents removalOfNullProductions(GrammarComponents gc) {
        Set<CFGRule> allRules = gc.getRules();
        Set<CFGRule> nullRules = allRules.stream()
                .filter(rule ->
                        rule.getRightSide().isEmpty()
                                && rule.getTerminal() != null
                                && rule.getTerminal().getName().equals(EPSILON))
                .collect(Collectors.toSet());
        for (CFGRule rule : nullRules) {
            allRules.remove(rule);
            Set<CFGRule> newRules = new HashSet<>();
            for (CFGRule ruleToChange : allRules) {
                if (ruleToChange.getRightSide().contains(rule.getLeftSide())) {
                    newRules.addAll(createRulesFromNullProductions(rule.getLeftSide(), ruleToChange));
                }
            }
            allRules.addAll(newRules);
        }
        removeObsoleteTerminalsAndNonTerminals(gc);
        return gc;
    }

    private Set<CFGRule> createRulesFromNullProductions(NonTerminal letterToNull, CFGRule rule) {
        List<NonTerminal> rightSideToChange = rule.getRightSide();
        Set<CFGRule> rulesToAdd = new HashSet<>();
        List<Integer> indexes = new ArrayList<>();
        for (int i = rightSideToChange.size() - 1; i >= 0; i--) {
            if (rightSideToChange.get(i).equals(letterToNull)) {
                indexes.add(i);
            }
        }
        int numberOfPossibleCombinations = (int) Math.pow(2, indexes.size());
        for (int i = 0; i < numberOfPossibleCombinations; i++) {
            List<NonTerminal> newRightSide = new ArrayList<>(rightSideToChange);
            int index = 0;
            boolean changed = false;
            for (int j = indexes.size() - 1; j >= 0; j--) {
                if ((i & (1 << j)) == 0) {
                    newRightSide.remove((int) indexes.get(index));
                    changed = true;
                }
                index++;
            }
            if (changed) {
                rulesToAdd.add(new CFGRule(new NonTerminal(rule.getLeftSide().getSymbol().getName()), rule.getTerminal(), newRightSide));
            }
        }


        return rulesToAdd;
    }
}