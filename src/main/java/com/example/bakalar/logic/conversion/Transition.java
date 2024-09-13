package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.conversion.window.WindowType;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bakalar.logic.MainLogic.EPSILON;

@Getter
@Setter
public class Transition {
    private MySymbol currentState;
    private MySymbol inputSymbolToRead;
    private MySymbol symbolToPop;
    private MySymbol nextState;
    private List<MySymbol> symbolsToPush;
    private WindowType windowType;

    public Transition(String currentState, String inputSymbolToRead, String symbolToPop, String nextState, String symbolsToPush) {
        this.currentState = new MySymbol(currentState);
        this.inputSymbolToRead = new MySymbol(inputSymbolToRead);
        this.symbolToPop = new MySymbol(symbolToPop);
        this.nextState = new MySymbol(nextState);
        this.symbolsToPush = convertStringToList(symbolsToPush);
        this.windowType = WindowType.NORMAL;
        if (this.symbolsToPush.size() == 1 && this.symbolsToPush.get(0).getName().equals(EPSILON)) {
            this.windowType = WindowType.TERMINAL;
        }
    }

    public Transition(String currentState, WindowType windowType) {
        this.currentState = new MySymbol(currentState);
        this.windowType = windowType;
        this.symbolsToPush = new ArrayList<>();

    }

    public Transition(MySymbol mySymbol, MySymbol inputSymbolToRead, MySymbol symbolToPop, MySymbol nextState, String symbolsToPushAsString) {
        this.currentState = mySymbol;
        this.inputSymbolToRead = inputSymbolToRead;
        this.symbolToPop = symbolToPop;
        this.nextState = nextState;
        this.symbolsToPush = convertStringToList(symbolsToPushAsString);
        this.windowType = WindowType.NORMAL;
        if (this.symbolsToPush.size() == 1 && this.symbolsToPush.get(0).getName().equals(EPSILON)) {
            this.windowType = WindowType.TERMINAL;
        }
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

    public Transition deepCopy() {
        return new Transition(currentState.getName(), inputSymbolToRead.getName(), symbolToPop.getName(), nextState.getName(), getSymbolsToPushAsString());
    }

    public List<Text> createTextFromStep() {
        int size = 25;
        List<Text> texts = new ArrayList<>();
        texts.add(new CustomText("δ(", size));
        texts.add(new CustomText(currentState, size));
        texts.add(new CustomText(", ", size));
        texts.add(new CustomText(inputSymbolToRead, size));
        texts.add(new CustomText(", ", size));
        texts.add(new CustomText(symbolToPop, size));
        texts.add(new CustomText(") = (", size));
        texts.add(new CustomText(nextState, size));
        texts.add(new CustomText(", ", size));
        for (MySymbol symbol : symbolsToPush) {
            texts.add(new CustomText(symbol, size));
        }
        texts.add(new CustomText(")", size));
        return texts;
    }

    @Override
    public String toString() {
        return "δ(" + currentState + ", " + inputSymbolToRead + ", " + symbolToPop + ") = " +
                "(" + nextState + ", " + (symbolsToPush == null ? "" : symbolsToPush.stream().map(MySymbol::getName).collect(Collectors.joining())) + ")";
    }
}
