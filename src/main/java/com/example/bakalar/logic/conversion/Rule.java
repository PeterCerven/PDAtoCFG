package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.character.MySymbol;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Rule {

    private MySymbol leftSide;
    private List<MySymbol> rightSide;

    public Rule(MySymbol leftSide, MySymbol readSymbol, List<MySymbol> rightSide) {
        this.leftSide = leftSide;
        rightSide.add(0, readSymbol);
        this.rightSide = rightSide;
    }

    @Override
    public String toString() {
        return leftSide + " -> " + rightSide.stream().map(MySymbol::getName).collect(Collectors.joining());
    }
}
