package com.example.bakalar.logic.transitions;

import com.example.bakalar.logic.conversion.CustomText;
import com.example.bakalar.logic.conversion.TransitionType;
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
public class Transition {
    private MySymbol currentState;
    private MySymbol inputSymbolToRead;
    private MySymbol symbolToPop;
    private MySymbol nextState;
    private List<MySymbol> symbolsToPush;
    private TransitionType transitionType;

    public Transition(String currentState, String inputSymbolToRead, String symbolToPop, String nextState, String symbolsToPush) {
        this.currentState = new MySymbol(currentState);
        this.inputSymbolToRead = new MySymbol(inputSymbolToRead);
        this.symbolToPop = new MySymbol(symbolToPop);
        this.nextState = new MySymbol(nextState);
        this.symbolsToPush = convertStringToList(symbolsToPush);
        this.transitionType = TransitionType.NORMAL;
        if (this.symbolsToPush.size() == 1 && this.symbolsToPush.get(0).getName().equals("ε")) {
            this.transitionType = TransitionType.TERMINAL;
        }
    }

    public Transition(String currentState, TransitionType transitionType) {
        this.currentState = new MySymbol(currentState);
        this.transitionType = transitionType;
    }

    private List<MySymbol> convertStringToList(String symbolsToPush) {
        return symbolsToPush.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .map(MySymbol::new)
                .collect(Collectors.toList());
    }

    public String getSymbolsToPushAsString() {
        return symbolsToPush.stream().map(MySymbol::getName).collect(Collectors.joining());
    }

    public List<Text> createTextFromStep() {
        List<Text> texts = new ArrayList<>();
        texts.add(new CustomText("δ("));
        texts.add(new CustomText(currentState));
        texts.add(new CustomText(", "));
        texts.add(new CustomText(inputSymbolToRead));
        texts.add(new CustomText(", "));
        texts.add(new CustomText(symbolToPop));
        texts.add(new CustomText(") -> ("));
        texts.add(new CustomText(nextState));
        texts.add(new CustomText(", "));
        for (MySymbol symbol : symbolsToPush) {
            texts.add(new CustomText(symbol));
        }
        texts.add(new CustomText(")"));
        return texts;
    }

    @Override
    public String toString() {
        return "δ(" + currentState + ", " + inputSymbolToRead + ", " + symbolToPop + ") -> " +
                "(" + nextState + ", " + (symbolsToPush == null ? "" : symbolsToPush.stream().map(MySymbol::getName).collect(Collectors.joining())) + ")";
    }
}
