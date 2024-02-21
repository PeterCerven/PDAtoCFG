package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CFGRuleStep {
    private CFGRule templateRule;
    private List<CFGRule> cfgRulesSteps;


    public CFGRuleStep(Transition transition) {
        templateRule= new CFGRule();
        templateRule.setLeftSide(new SpecialNonTerminal("_", "_", "_"));
        templateRule.setTerminal("_");
        for (MySymbol symbol : transition.getSymbolsToPush()) {
            templateRule.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        }
        cfgRulesSteps = new ArrayList<>();
    }

    public void addLeftSideStep(SpecialNonTerminal leftSide) {
        templateRule.setLeftSide(leftSide);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.getTerminal(), templateRule.copyRightSide()));
    }

    public void addTerminal(String terminal) {
        templateRule.setTerminal(terminal);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.getTerminal(), templateRule.copyRightSide()));
    }

    public void addRightSideStepForTableOption(String tableOption, int index) {
        templateRule.getRightSide().get(index).setStateSymbolTo(tableOption);
        templateRule.getRightSide().get(index + 1).setStateSymbolFrom(tableOption);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.getTerminal(), templateRule.copyRightSide()));
    }

    public void addRightSideStackSymbols(List<MySymbol> stackSymbols) {
        for (int i = 0; i < stackSymbols.size(); i++) {
            templateRule.getRightSide().get(i).setStackSymbol(stackSymbols.get(i).getName());
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.getTerminal(), templateRule.copyRightSide()));
    }

    public void addFirstAndLastRightSideStep(String name) {
        templateRule.getRightSide().get(0).setStateSymbolFrom(name);
        templateRule.getRightSide().get(templateRule.getRightSide().size() - 1).setStateSymbolTo(templateRule.getLeftSide().getStateSymbolTo());
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.getTerminal(), templateRule.copyRightSide()));
    }
}
