package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.MySymbol;
import com.example.bakalar.logic.utility.SpecialNonTerminal;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StepRule {
    private SpecialNonTerminal leftSide;
    private MySymbol mySymbolLeftSide;
    private MySymbol terminal;
    private List<SpecialNonTerminal> rightSide;
    private Transition transition;
    private String helpingComment;

    public StepRule(SpecialNonTerminal leftSide, MySymbol terminal, List<SpecialNonTerminal> rightSide, Transition transition, String helpingComment) {
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
}
