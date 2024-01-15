package com.example.bakalar.canvas.conversion;

import com.example.bakalar.canvas.transitions.Transition;
import com.example.bakalar.character.MySymbol;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ConvertedTransitions {
    private MySymbol leftSide;
    private List<MySymbol> rightSide;

    public ConvertedTransitions() {
    }

    @Override
    public String toString() {
        return leftSide + " -> " + rightSide.stream().map(MySymbol::getName).collect(Collectors.joining());
    }
}
