package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class SimplifyLogic {
    private List<StepRule> steps;

    public void simplify(Set<NonTerminal> allNonTerminals, Set<MySymbol> allTerminals, List<CFGRule> allRules, String startingS) {
        changeSpecialTerminalsToNonTerminals(allNonTerminals, allRules);
        removeUnreachable(allNonTerminals, allRules, startingS);
        removeUnproductive(allNonTerminals, allRules);
        RemoveEpsilonProductions(allNonTerminals, allRules);
        RemoveUnitProductions(allNonTerminals, allRules);
        RemoveUselessProductions(allNonTerminals, allRules);
        ConvertToChomskyNormalForm(allNonTerminals, allTerminals, allRules);
    }

    private void changeSpecialTerminalsToNonTerminals(Set<NonTerminal> allNonTerminals, List<CFGRule> allRules) {
        char namingLetter = 'A';
        String prohibitedLetters = "SZ";

        List<CFGRule> newNonTerminalsNames = new ArrayList<>();

        for (NonTerminal nonTerminal : allNonTerminals) {
            if (nonTerminal instanceof SpecialNonTerminal snt) {
                if (prohibitedLetters.contains(namingLetter + "")) {
                    continue;
                }
                snt.setLetterName(namingLetter++ + "");
            }
        }



        for (CFGRule rule : allRules) {
            CFGRule newRule = rule.getDeepCopy();
            List<NonTerminal> newRightSide = new ArrayList<>();
            for (NonTerminal nonTerminal : newRule.getRightSide()) {
                if (nonTerminal instanceof SpecialNonTerminal spt) {
                    newRightSide.add(new NonTerminal(new MySymbol(spt.getLetterName())));
                }
                newRule.setRightSide(newRightSide);
            }
            newNonTerminalsNames.add(newRule);
            System.out.println(newRule);
        }

    }

    private void ConvertToChomskyNormalForm(Set<NonTerminal> allNonTerminals, Set<MySymbol> allTerminals, List<CFGRule> allRules) {
    }

    private void RemoveUselessProductions(Set<NonTerminal> allNonTerminals, List<CFGRule> allRules) {
    }

    private void RemoveUnitProductions(Set<NonTerminal> allNonTerminals, List<CFGRule> allRules) {
    }

    private void RemoveEpsilonProductions(Set<NonTerminal> allNonTerminals, List<CFGRule> allRules) {
    }

    private void removeUnproductive(Set<NonTerminal> allNonTerminals, List<CFGRule> allRules) {
    }

    private void removeUnreachable(Set<NonTerminal> allNonTerminals, List<CFGRule> allRules, String startingS) {

    }


}
