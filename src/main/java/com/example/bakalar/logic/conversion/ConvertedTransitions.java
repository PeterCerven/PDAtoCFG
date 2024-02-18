package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.utility.MySymbol;
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
