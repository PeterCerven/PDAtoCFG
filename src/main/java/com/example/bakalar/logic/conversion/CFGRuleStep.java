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
    private List<Transition> stepsTransitions;
    private Transition transition;


    public CFGRuleStep(Transition transition) {
        this.transition = transition;
        templateRule = new CFGRule();
        templateRule.setLeftSide(new SpecialNonTerminal("_", "_", "_"));
        templateRule.setTerminal(new MySymbol("_"));
        for (MySymbol symbol : transition.getSymbolsToPush()) {
            templateRule.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        }
        cfgRulesSteps = new ArrayList<>();
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
        stepsTransitions = new ArrayList<>();
        stepsTransitions.add(new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString()));
    }

    public CFGRuleStep(MySymbol startingSymbol, MySymbol startingStackSymbol, MySymbol startingStateSymbol, List<MySymbol> allStateSymbols) {

    }

    public void addLeftSideStep(SpecialNonTerminal leftSide, String symbol) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        if (symbol.equals("current")) {
            newTransition.setCurrentState(new MySymbol(leftSide.getStateSymbolFrom().getName(), Color.RED));
        } else if (symbol.equals("pop")) {
            newTransition.setSymbolToPop(new MySymbol(leftSide.getStackSymbol().getName(), Color.RED));
        }
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        templateRule.setLeftSide(leftSide);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addTerminal(MySymbol terminal) {
        // transition step
        MySymbol myTerminal = new MySymbol(terminal.getName(), Color.RED);
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        newTransition.setInputSymbolToRead(terminal);
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        templateRule.setTerminal(myTerminal);
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }


    public void addRightSideStackSymbols(List<MySymbol> stackSymbols, Color color) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        List<MySymbol> newStackSymbols = new ArrayList<>();
        for (MySymbol stackSymbol : stackSymbols) {
            MySymbol newStackSymbol = new MySymbol(stackSymbol.getName(), color);
            newStackSymbols.add(newStackSymbol);
        }
        newTransition.setSymbolsToPush(newStackSymbols);
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        for (int i = 0; i < stackSymbols.size(); i++) {
            MySymbol stackSymbol = stackSymbols.get(i);
            MySymbol newStackSymbol = new MySymbol(stackSymbol.getName(), color);
            templateRule.getRightSide().get(i).setStackSymbol(newStackSymbol);
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addFirstRightSideStep(MySymbol name) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        newTransition.setNextState(new MySymbol(name.getName(), Color.RED));
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        templateRule.getRightSide().get(0).setStateSymbolFrom(name);
        templateRule.getRightSide().get(templateRule.getRightSide().size() - 1).setStateSymbolTo(templateRule.getLeftSide().getStateSymbolTo());
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addAllPossibilities(SpecialNonTerminal leftSide, List<MySymbol> tableOptions) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        templateRule.setLeftSide(leftSide);
        for (MySymbol tableOption : tableOptions) {
            templateRule.getRightSide().get(tableOption.getIndex()).setStateSymbolTo(tableOption);
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addPossibilitiesAnotherSide(List<MySymbol> tableOptions) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        for (MySymbol tableOption : tableOptions) {
            tableOption.setColor(Color.RED);
            templateRule.getRightSide().get(tableOption.getIndex() + 1).setStateSymbolFrom(tableOption);
        }
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }

    public void addLastRightStep(SpecialNonTerminal leftSide) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        stepsTransitions.add(newTransition);

        // rule step
        templateRule.resetFontColor();
        leftSide.getStateSymbolTo().setColor(Color.RED);
        templateRule.setLeftSide(leftSide);
        templateRule.getRightSide().get(templateRule.getRightSide().size() - 1).setStateSymbolTo(templateRule.getLeftSide().getStateSymbolTo());
        cfgRulesSteps.add(new CFGRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), transition));
    }
}
