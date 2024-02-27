package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CFGRuleStep {
    private CFGRule templateRule;
    private List<CFGRule> cfgRulesSteps;
    private TextFlow transitionLabel;
    private Transition transition;
    private Transition templateTransition;


    public CFGRuleStep(Transition transition, TextFlow transitionLabel) {
        this.transitionLabel = transitionLabel;
        this.transition = transition;
        templateRule = new CFGRule();
        templateRule.setLeftSide(new SpecialNonTerminal("_", "_", "_"));
        templateRule.setTerminal(new MySymbol("_"));
        for (MySymbol symbol : transition.getSymbolsToPush()) {
            templateRule.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        }
        cfgRulesSteps = new ArrayList<>();
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addLeftSideStep(SpecialNonTerminal leftSide) {
        templateRule.resetFontColor();
        templateRule.setLeftSide(leftSide);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addTerminal(MySymbol terminal) {
        templateRule.resetFontColor();
        templateRule.setTerminal(new MySymbol(terminal.getName(), Color.RED));
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addRightSideStepForTableOption(MySymbol tableOption, int index) {
        templateRule.resetFontColor();
        templateRule.getRightSide().get(index).setStateSymbolTo(tableOption);
        templateRule.getRightSide().get(index + 1).setStateSymbolFrom(tableOption);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addRightSideStackSymbols(List<MySymbol> stackSymbols, Color color) {
        templateRule.resetFontColor();
        for (int i = 0; i < stackSymbols.size(); i++) {
            MySymbol stackSymbol = stackSymbols.get(i);
            MySymbol newStackSymbol = new MySymbol(stackSymbol.getName(), color);
            templateRule.getRightSide().get(i).setStackSymbol(newStackSymbol);
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addFirstRightSideStep(MySymbol name) {
        templateRule.resetFontColor();
        templateRule.getRightSide().get(0).setStateSymbolFrom(name);
        templateRule.getRightSide().get(templateRule.getRightSide().size() - 1).setStateSymbolTo(templateRule.getLeftSide().getStateSymbolTo());
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addAllPossibilities(SpecialNonTerminal leftSide, List<MySymbol> tableOptions) {
        templateRule.resetFontColor();
        templateRule.setLeftSide(leftSide);
        for (MySymbol tableOption : tableOptions) {
            templateRule.getRightSide().get(tableOption.getIndex()).setStateSymbolTo(tableOption);
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addPossibilitiesAnotherSide(List<MySymbol> tableOptions) {
        templateRule.resetFontColor();
        for (MySymbol tableOption : tableOptions) {
            tableOption.setColor(Color.RED);
            templateRule.getRightSide().get(tableOption.getIndex() + 1).setStateSymbolFrom(tableOption);
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addLastRightStep(SpecialNonTerminal leftSide) {
        templateRule.resetFontColor();
        leftSide.getStateSymbolTo().setColor(Color.RED);
        templateRule.setLeftSide(leftSide);
        templateRule.getRightSide().get(templateRule.getRightSide().size() - 1).setStateSymbolTo(templateRule.getLeftSide().getStateSymbolTo());
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }
}
