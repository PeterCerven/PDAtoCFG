package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.NonTerminal;
import com.example.bakalar.logic.utility.NonTerminal;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StepRule {
    private NonTerminal leftSide;
    private MySymbol terminal;
    private List<NonTerminal> rightSide;
    private Transition transition;
    private String helpingComment;

    public StepRule(NonTerminal leftSide, MySymbol terminal, List<NonTerminal> rightSide, Transition transition, String helpingComment) {
        this.leftSide = leftSide;
        this.terminal = terminal;
        this.rightSide = rightSide;
        this.transition = transition;
        this.helpingComment = helpingComment;
    }

    public StepRule() {
        this.rightSide = new ArrayList<>();
    }

    public StepRule(Transition transition) {
        this.transition = transition;
        this.rightSide = new ArrayList<>();
    }

    public List<Text> createTextFromStep() {
        int fontSize = 22;
        List<Text> texts = new ArrayList<>();
        texts.addAll(leftSide.createText(fontSize));
        texts.add(new CustomText(" -> ", fontSize));
        if (terminal != null) {
            texts.add(new CustomText(terminal, fontSize));
        }
        for (NonTerminal NonTerminal : rightSide) {
            texts.addAll(NonTerminal.createText(fontSize));
        }
        return texts;
    }

}
