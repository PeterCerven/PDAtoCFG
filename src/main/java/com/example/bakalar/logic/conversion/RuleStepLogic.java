package com.example.bakalar.logic.conversion;

import com.example.bakalar.exceptions.MyCustomException;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RuleStepLogic {
    private CFGRule templateRule;
    private List<StepRule> stepRules;
    private Transition transition;


    public void prepareSteps() {
        templateRule = new CFGRule();
        templateRule.setLeftSide(new SpecialNonTerminal("_", "_", "_"));
        templateRule.setTerminal(new MySymbol("_"));
        for (MySymbol symbol : transition.getSymbolsToPush()) {
            templateRule.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        }
        String helpingComment = "Počet pravidiel závisí od počtu stavov zásobníkového automatu a od počtu novo pridaných symbolov na zásobník." +
                " Kde m je počet nových symbolov na zásobník a n je počet stavov zásobníkového automatu. Počet pravidiel je n ^ m.";
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));
    }

    public RuleStepLogic(Transition transition) {
        this.transition = transition;
        stepRules = new ArrayList<>();
    }

    // terminal step
    public void createTerminalSteps() {
        StepRule firstStep = new StepRule();
        firstStep.setLeftSide(new SpecialNonTerminal("_", "_", "_"));
        firstStep.setTerminal(new MySymbol("_"));
        firstStep.setTransition(new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString()));
        firstStep.setHelpingComment("Keď nepridávame žiadne nové symboly do zásobníka, vytvoríme terminálne pravidlo.");
        stepRules.add(firstStep);

        StepRule secondStep = new StepRule();
        secondStep.setLeftSide(new SpecialNonTerminal(new MySymbol(transition.getCurrentState(), Color.RED), new MySymbol("_"), "_"));
        secondStep.setTerminal(new MySymbol("_"));
        secondStep.setTransition(new Transition(new MySymbol(transition.getCurrentState().getName(), Color.RED), transition.getInputSymbolToRead(),
                transition.getSymbolToPop(), transition.getNextState(), transition.getSymbolsToPushAsString()));
        secondStep.setHelpingComment("Pridávame začiatočný stav automatu.");
        stepRules.add(secondStep);

        StepRule thirdStep = new StepRule();
        thirdStep.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), new MySymbol(transition.getSymbolToPop(), Color.RED), "_"));
        thirdStep.setTerminal(new MySymbol("_"));
        thirdStep.setTransition(new Transition(transition.getCurrentState(), transition.getInputSymbolToRead(), new MySymbol(transition.getSymbolToPop(), Color.RED),
                transition.getNextState(), transition.getSymbolsToPushAsString()));
        thirdStep.setHelpingComment("Pridávame symbol zásobníka, ktorý automat vyžiera.");
        stepRules.add(thirdStep);

        StepRule fourthStep = new StepRule();
        fourthStep.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), new MySymbol(transition.getNextState(), Color.RED)));
        fourthStep.setTerminal(new MySymbol("_"));
        fourthStep.setTransition(new Transition(transition.getCurrentState(), transition.getInputSymbolToRead(), transition.getSymbolToPop(),
                new MySymbol(transition.getNextState().getName(), Color.RED), transition.getSymbolsToPushAsString()));
        fourthStep.setHelpingComment("Pridávame stav, do ktorého prechádzame.");
        stepRules.add(fourthStep);

        StepRule fifthStep = new StepRule();
        fifthStep.setLeftSide(new SpecialNonTerminal(transition.getCurrentState(), transition.getSymbolToPop(), transition.getNextState()));
        fifthStep.setTerminal(new MySymbol(transition.getInputSymbolToRead(), Color.RED));
        fifthStep.setTransition(new Transition(transition.getCurrentState(), new MySymbol(transition.getInputSymbolToRead().getName(), Color.RED),
                transition.getSymbolToPop(), transition.getNextState(), transition.getSymbolsToPushAsString()));
        String helpingComment = "";
        if (transition.getInputSymbolToRead().getName().equals("ε")) {
            helpingComment = "Pridávame prázdny symbol epsilon.";
        } else {
            helpingComment = "Pridávame terminálny symbol, ktorý čítame z pásky.";
        }
        fifthStep.setHelpingComment(helpingComment);
        stepRules.add(fifthStep);
    }

    // starting step
    public void createStartingSteps(String startingSymbol, String startingStackSymbol, String startingStateSymbol, String stateSymbol) {
        StepRule firstStep = new StepRule(transition);
        firstStep.setLeftSide(new NonTerminal("_"));
        firstStep.setRightSide(new ArrayList<>());
        firstStep.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        firstStep.setHelpingComment("Počet pravidiel závisí od počtu stavov zásobníkového automatu.");
        stepRules.add(firstStep);

        StepRule secondStep = new StepRule(transition);
        secondStep.setLeftSide(new NonTerminal(startingSymbol, Color.RED));
        secondStep.setRightSide(new ArrayList<>());
        secondStep.getRightSide().add(new SpecialNonTerminal("_", "_", "_"));
        secondStep.setHelpingComment("Pridávame začiatočný symbol S.");
        stepRules.add(secondStep);

        StepRule thirdStep = new StepRule(transition);
        thirdStep.setLeftSide(new NonTerminal(startingSymbol));
        thirdStep.setRightSide(new ArrayList<>());
        thirdStep.getRightSide().add(new SpecialNonTerminal(new MySymbol(startingStateSymbol, Color.RED), new MySymbol("_"), "_"));
        thirdStep.setHelpingComment("Pridávame začiatočný stav automatu.");
        stepRules.add(thirdStep);

        StepRule fourthStep = new StepRule(transition);
        fourthStep.setLeftSide(new NonTerminal(startingSymbol));
        fourthStep.setRightSide(new ArrayList<>());
        fourthStep.getRightSide().add(new SpecialNonTerminal(new MySymbol(startingStateSymbol), new MySymbol(startingStackSymbol, Color.RED), "_"));
        fourthStep.setHelpingComment("Pridávame začiatočný symbol zásobníka.");
        stepRules.add(fourthStep);

        StepRule fifthStep = new StepRule(transition);
        fifthStep.setLeftSide(new NonTerminal(startingSymbol));
        fifthStep.setRightSide(new ArrayList<>());
        fifthStep.getRightSide().add(new SpecialNonTerminal(new MySymbol(startingStateSymbol), new MySymbol(startingStackSymbol), new MySymbol(stateSymbol, Color.RED)));
        fifthStep.setHelpingComment("Postupne pridávame všetky možné stavy zásobníkového automatu.");
        stepRules.add(fifthStep);

    }

    // normal step
    public void addLeftSideStep(SpecialNonTerminal leftSide, String symbol) {
        // transition step

        String helpingComment = "";
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        if (symbol.equals("current")) {
            newTransition.setCurrentState(new MySymbol(leftSide.getStateSymbolFrom().getName(), Color.RED));
            helpingComment = "Do ľavej strany pravidla pridávame nový symbol súčastného stavu.";
        } else if (symbol.equals("pop")) {
            newTransition.setSymbolToPop(new MySymbol(leftSide.getStackSymbol().getName(), Color.RED));
            helpingComment = "Do ľavej strany pravidla pridávame symbol ktorý vyžierame zo zásobníka.";
        }

        // rule step
        templateRule.resetFontColor();
        templateRule.setLeftSide(leftSide);
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));
    }

    public void addTerminal(MySymbol terminal) {
        // transition step
        MySymbol myTerminal = new MySymbol(terminal.getName(), Color.RED);
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        newTransition.setInputSymbolToRead(terminal);

        // rule step
        templateRule.resetFontColor();
        templateRule.setTerminal(myTerminal);
        String helpingComment = "Na začiatok pravej strany pridáme terminálny symbol, ktorý čítame z pásky.";
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));

    }


    public void addRightSideStackSymbols(List<MySymbol> stackSymbols, Color color) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        List<MySymbol> newStackSymbols = new ArrayList<>();
        for (MySymbol stackSymbol : stackSymbols) {
            MySymbol newStackSymbol = new MySymbol(stackSymbol.getName(), color);
            newStackSymbols.add(newStackSymbol);
        }
        newTransition.setSymbolsToPush(newStackSymbols);

        // rule step
        templateRule.resetFontColor();
        for (int i = 0; i < stackSymbols.size(); i++) {
            MySymbol stackSymbol = stackSymbols.get(i);
            MySymbol newStackSymbol = new MySymbol(stackSymbol.getName(), color);
            NonTerminal nonTerminal = templateRule.getRightSide().get(i);
            if (nonTerminal instanceof  SpecialNonTerminal spt) {
                spt.setStackSymbol(newStackSymbol);
            }

        }
        String helpingComment = "Počet Neterminálnych symbolov na pravej strane pravidla, závisí od počtu nových symbolov na zásobníku.";
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));

    }

    public void addFirstRightSideStep(MySymbol name) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString());
        newTransition.setNextState(new MySymbol(name.getName(), Color.RED));

        // rule step
        templateRule.resetFontColor();
        NonTerminal firstNonTerminal = templateRule.getRightSide().get(0);
        if (firstNonTerminal instanceof  SpecialNonTerminal spt) {
            spt.setStateSymbolTo(name);
        }
        NonTerminal rightSideNonTerminal = templateRule.getRightSide().get(templateRule.getRightSide().size() - 1);
        NonTerminal leftSide = templateRule.getLeftSide();
        if (rightSideNonTerminal instanceof SpecialNonTerminal spt && leftSide instanceof SpecialNonTerminal spt2) {
            spt.setStateSymbolTo(spt2.getStateSymbolTo());
        }

        String helpingComment = "Na začiatok pravej strany pravidla pridáme symbol stavu do ktorého prechádzame.";
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));

    }

    public void addAllPossibilities(SpecialNonTerminal leftSide, List<MySymbol> tableOptions) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString());

        // rule step
        templateRule.resetFontColor();
        templateRule.setLeftSide(leftSide);
        for (MySymbol tableOption : tableOptions) {
            if (templateRule.copyRightSide().get(tableOption.getIndex()) instanceof SpecialNonTerminal spt) {
                spt.setStateSymbolTo(tableOption);
            }
        }
        String helpingComment = "Tu pridáme všetky možnosti ako môžeme prechádzať stavmi.";
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));

    }

    public void addPossibilitiesAnotherSide(List<MySymbol> tableOptions) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(),
                transition.getSymbolToPop().getName(), transition.getNextState().getName(), transition.getSymbolsToPushAsString());

        // rule step
        templateRule.resetFontColor();
        for (MySymbol tableOption : tableOptions) {
            tableOption.setColor(Color.RED);
            if (templateRule.getRightSide().get(tableOption.getIndex() + 1) instanceof  SpecialNonTerminal spt) {
                spt.setStateSymbolFrom(tableOption);
            }
        }
        String helpingComment = "Zkopírujeme koncoví stav predošlého neterminálu na začiatok ďalšieho neterminálu.";
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));

    }

    public void addLastRightStep(SpecialNonTerminal leftSide) {
        // transition step
        Transition newTransition = new Transition(transition.getCurrentState().getName(), transition.getInputSymbolToRead().getName(), transition.getSymbolToPop().getName(),
                transition.getNextState().getName(), transition.getSymbolsToPushAsString());

        // rule step
        templateRule.resetFontColor();
        leftSide.getStateSymbolTo().setColor(Color.RED);
        templateRule.setLeftSide(leftSide);
        if (templateRule.getRightSide().get(templateRule.getRightSide().size() - 1) instanceof  SpecialNonTerminal spt) {
            spt.setStateSymbolTo(leftSide.getStateSymbolTo());
        }
        String helpingComment = "Na koniec pravej strany pravidla pridáme symbol stavu, ktorý sa nachádza na poslednom mieste v neterminále naľavo.";
        stepRules.add(new StepRule(templateRule.copyLeftSide(), templateRule.copyTerminal(), templateRule.copyRightSide(), newTransition, helpingComment));
    }


}
