package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CFGRule {
    private SpecialNonTerminal leftSide;
    private MySymbol mySymbolLeftSide;
    private MySymbol terminal;
    private List<SpecialNonTerminal> rightSide;
    private List<CFGRule> steps;
    private List<Transition> stepsTransitions;
    private Transition transition;
    private List<String> helpingComments;

    public CFGRule() {
        this.steps = new ArrayList<>();
        this.rightSide = new ArrayList<>();
        this.stepsTransitions = new ArrayList<>();
        this.helpingComments = new ArrayList<>();
    }

    public CFGRule(SpecialNonTerminal leftSide, MySymbol terminal, List<SpecialNonTerminal> rightSide, Transition transition) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
        this.stepsTransitions = new ArrayList<>();
        this.helpingComments = new ArrayList<>();
    }

    public CFGRule(MySymbol mySymbolLeftSide, MySymbol terminal, List<SpecialNonTerminal> rightSide, Transition transition) {
        this.mySymbolLeftSide = mySymbolLeftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.steps = new ArrayList<>();
        this.transition = transition;
        this.stepsTransitions = new ArrayList<>();
    }

    public SpecialNonTerminal copyLeftSide() {
        return new SpecialNonTerminal(leftSide.getStateSymbolFrom(), leftSide.getStateSymbolFrom().getColor(),
                leftSide.getStackSymbol(), leftSide.getStackSymbol().getColor(),
                leftSide.getStateSymbolTo(), leftSide.getStateSymbolTo().getColor());
    }

    public List<SpecialNonTerminal> copyRightSide() {
        List<SpecialNonTerminal> copy = new ArrayList<>();
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            copy.add(new SpecialNonTerminal(
                    specialNonTerminal.getStateSymbolFrom(), specialNonTerminal.getStateSymbolFrom().getColor(),
                    specialNonTerminal.getStackSymbol(), specialNonTerminal.getStackSymbol().getColor(),
                    specialNonTerminal.getStateSymbolTo(), specialNonTerminal.getStateSymbolTo().getColor()));
        }
        return copy;
    }

    public MySymbol copyTerminal() {
        return new MySymbol(terminal.getName(), terminal.getColor());
    }

    public void resetFontColor() {
        leftSide.getStateSymbolFrom().setColor(MySymbol.DEFAULT_COLOR);
        leftSide.getStackSymbol().setColor(MySymbol.DEFAULT_COLOR);
        leftSide.getStateSymbolTo().setColor(MySymbol.DEFAULT_COLOR);
        if (terminal != null) {
            terminal.setColor(MySymbol.DEFAULT_COLOR);
        }
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            specialNonTerminal.getStateSymbolFrom().setColor(MySymbol.DEFAULT_COLOR);
            specialNonTerminal.getStackSymbol().setColor(MySymbol.DEFAULT_COLOR);
            specialNonTerminal.getStateSymbolTo().setColor(MySymbol.DEFAULT_COLOR);
        }
    }

    public List<Text> createTextFromStep() {
        int fontSize = 22;
        List<Text> texts = new ArrayList<>();
        if (mySymbolLeftSide == null) {
            createSpecialSymbol(texts, leftSide, fontSize);
        } else {
            texts.add(new CustomText(mySymbolLeftSide, fontSize));
        }
        texts.add(new CustomText(" -> ", fontSize));
        if (terminal != null) {
            texts.add(new CustomText(terminal, fontSize));
        }
        for (SpecialNonTerminal specialNonTerminal : rightSide) {
            createSpecialSymbol(texts, specialNonTerminal, fontSize);
        }
        return texts;
    }

    private void createSpecialSymbol(List<Text> texts, SpecialNonTerminal specialNonTerminal, int fontSize) {
        texts.add(new CustomText("[", fontSize));
        texts.add(new CustomText(specialNonTerminal.getStateSymbolFrom(), fontSize));
        texts.add(new CustomText(", ", fontSize));
        texts.add(new CustomText(specialNonTerminal.getStackSymbol(), fontSize));
        texts.add(new CustomText(", ", fontSize));
        texts.add(new CustomText(specialNonTerminal.getStateSymbolTo(), fontSize));
        texts.add(new CustomText("]", fontSize));
    }


    @Override
    public String toString() {
        if (mySymbolLeftSide == null) {
            return leftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
        } else {
            return mySymbolLeftSide + " -> " + (terminal == null ? "" : terminal) + rightSide.stream().map(SpecialNonTerminal::toString).collect(Collectors.joining());
        }
    }
}
