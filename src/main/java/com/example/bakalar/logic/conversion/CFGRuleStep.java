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
    private List<String> helpingComments;


    public CFGRuleStep(Transition transition) {
        this.helpingComments = new ArrayList<>();
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

        this.helpingComments.add("Počet pravidiel závisí od počtu stavov zásobníkového automatu a od počtu novo pridaných symbolov na zásobník." +
                " Kde m je počet nových symbolov na zásobník a n je počet stavov zásobníkového automatu. Počet pravidiel je n ^ m.");
    }

    public CFGRuleStep() {
        cfgRulesSteps = new ArrayList<>();
        stepsTransitions = new ArrayList<>();
        helpingComments = new ArrayList<>();
    }

    // terminal step
    public void createTerminalSteps(Transition transition) {
        this.transition = transition;

        CFGRule firstStep = new CFGRule();
        firstStep.setLeftSide(new SpecialNonTerminal("_", "_", "_"));
        firstStep.setTerminal(new MySymbol("_"));
        cfgRulesSteps.add(firstStep);

        Transition firstTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        stepsTransitions.add(firstTransition);

        this.helpingComments.add("Keď nepridávame žiadne nové symboly do zásobníka, vytvoríme terminálne pravidlo.");

        CFGRule secondStep = new CFGRule();
        secondStep.setLeftSide(new SpecialNonTerminal(new MySymbol(transition.getCurrentState(), Color.RED), new MySymbol("_"), "_"));
        secondStep.setTerminal(new MySymbol("_"));
        cfgRulesSteps.add(secondStep);

        Transition secondTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        secondTransition.setCurrentState(new MySymbol(transition.getCurrentState().getName(), Color.RED));
        stepsTransitions.add(secondTransition);

        this.helpingComments.add("Pridávame začiatočný stav automatu.");

        CFGRule thirdStep = new CFGRule();
        thirdStep.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), new MySymbol(transition.getSymbolToPop(), Color.RED), "_"));
        thirdStep.setTerminal(new MySymbol("_"));
        cfgRulesSteps.add(thirdStep);

        Transition thirdTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        thirdTransition.setSymbolToPop(new MySymbol(transition.getSymbolToPop(), Color.RED));
        stepsTransitions.add(thirdTransition);

        this.helpingComments.add("Pridávame symbol zásobníka, ktorý automat vyžiera.");

        CFGRule fourthStep = new CFGRule();
        fourthStep.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), new MySymbol(transition.getNextState(), Color.RED)));
        fourthStep.setTerminal(new MySymbol("_"));
        cfgRulesSteps.add(fourthStep);

        Transition fourthTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        fourthTransition.setNextState(new MySymbol(transition.getNextState().getName(), Color.RED));
        stepsTransitions.add(fourthTransition);

        this.helpingComments.add("Pridávame stav, do ktorého prechádzame.");

        CFGRule fifthStep = new CFGRule();
        fifthStep.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), transition.getNextState()));
        fifthStep.setTerminal(new MySymbol(transition.getInputSymbolToRead(), Color.RED));
        cfgRulesSteps.add(fifthStep);

        Transition fifthTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        fifthTransition.setInputSymbolToRead(new MySymbol(transition.getInputSymbolToRead().getName(), Color.RED));
        stepsTransitions.add(fifthTransition);

        if (transition.getInputSymbolToRead().getName().equals("ε")) {
            this.helpingComments.add("Pridávame prázdny symbol epsilon.");
        } else {
            this.helpingComments.add("Pridávame terminálny symbol, ktorý čítame z pásky.");
        }
    }

    // starting step
    public void createStartingSteps(String startingSymbol, String startingStackSymbol, String startingStateSymbol, String stateSymbol, Transition transition) {
        this.transition = transition;

        CFGRule firstStep = new CFGRule();
        firstStep.setMySymbolLeftSide(new MySymbol("_"));
        firstStep.setRightSide(new ArrayList<>());
        firstStep.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        cfgRulesSteps.add(firstStep);

        this.helpingComments.add("Počet pravidiel závisí od počtu stavov zásobníkového automatu.");

        CFGRule secondStep = new CFGRule();
        secondStep.setMySymbolLeftSide(new MySymbol(startingSymbol, Color.RED));
        secondStep.setRightSide(new ArrayList<>());
        secondStep.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        cfgRulesSteps.add(secondStep);

        this.helpingComments.add("Pridávame začiatočný symbol S.");

        CFGRule thirdStep = new CFGRule();
        thirdStep.setMySymbolLeftSide(new MySymbol(startingSymbol));
        thirdStep.setRightSide(new ArrayList<>());
        thirdStep.getRightSide().add(new SpecialNonTerminal(new MySymbol(startingStateSymbol, Color.RED), new MySymbol("_"), "_"));
        cfgRulesSteps.add(thirdStep);

        this.helpingComments.add("Pridáme začiatočný stav automatu.");

        CFGRule fourthStep = new CFGRule();
        fourthStep.setMySymbolLeftSide(new MySymbol(startingSymbol));
        fourthStep.setRightSide(new ArrayList<>());
        fourthStep.getRightSide().add(new SpecialNonTerminal(new MySymbol(startingStateSymbol), new MySymbol(startingStackSymbol, Color.RED), "_"));
        cfgRulesSteps.add(fourthStep);

        this.helpingComments.add("Pridáme začiatočný symbol zásobníka.");

        CFGRule fifthStep = new CFGRule();
        fifthStep.setMySymbolLeftSide(new MySymbol(startingSymbol));
        fifthStep.setRightSide(new ArrayList<>());
        fifthStep.getRightSide().add(new SpecialNonTerminal(new MySymbol(startingStateSymbol), new MySymbol(startingStackSymbol), new MySymbol(stateSymbol, Color.RED)));
        cfgRulesSteps.add(fifthStep);

        this.helpingComments.add("Postupne pridávame všetky možné stavy zásobníkového automatu.");
    }

    // normal step
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

        this.helpingComments.add("Do ľavej strany pravidla pridávame nový symbol súčastného stavu.");
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

        this.helpingComments.add("Na začiatok pravej strany pridáme terminálny symbol, ktorý čítame z pásky.");
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

        this.helpingComments.add("Počet Neterminálnych symbolov na pravej strane pravidla, závisí od počtu nových symbolov na zásobníku.");
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

        this.helpingComments.add("Na začiatok pravej strany pravidla pridáme symbol stavu do ktorého prechádzame.");
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

        this.helpingComments.add("Tu pridáme všetky možnosti ako môžeme prechádzať stavmi.");
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

        this.helpingComments.add("Zkopírujeme koncoví stave predošlého neterminálu na začiatok ďalšieho neterminálu.");
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

        this.helpingComments.add("Na koniec pravej strany pravidla pridáme symbol stavu, ktorý sa nachádza na poslednom mieste v neterminále naľavo.");
    }


}
