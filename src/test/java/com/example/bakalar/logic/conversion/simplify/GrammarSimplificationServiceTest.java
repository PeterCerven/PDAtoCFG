package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GrammarSimplificationServiceTest {
    private final GrammarSimplificationService grammarSimplificationService = new GrammarSimplificationService();

    private static Stream<Arguments> provideGrammarComponentsForTesting() {
        return Stream.of(
                arguments(
                        inputGrammar(),
                        expectedGrammar()
                )
        );
    }

    private static SpecialNonTerminal createSNT(String stateFrom, String stack, String stateTo) {
        return SpecialNonTerminal.specialNonTerminalBuilder()
                .stateFrom(new MySymbol(stateFrom))
                .stack(new MySymbol(stack))
                .stateTo(new MySymbol(stateTo))
                .build();
    }

    private static NonTerminal createNT(String name) {
        return new NonTerminal(name);
    }

    private static GrammarComponents inputGrammar() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"),null,
                                List.of(
                                createSNT("A", "B", "C"),
                                createSNT("D", "E", "F"))),

                        createRule(createSNT("A", "B", "C"),"a",
                                List.of(
                                createSNT("D", "E", "F"),
                                createSNT("J", "K", "L"))),

                        createRule(createSNT("M", "N", "O"),"b",
                                List.of(
                                createSNT("P", "Q", "R"),
                                createSNT("V", "W", "X"))),

                        createRule(createSNT("Y", "Z", "A"),"c",
                                List.of(
                                createSNT("B", "C", "D"),
                                createSNT("H", "I", "J")))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static GrammarComponents expectedGrammar() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"),null,
                                List.of(
                                createNT("C"),
                                createNT("E"))),

                        createRule(createNT("C"),"a",
                                List.of(
                                createNT("E"),
                                createNT("H"))),

                        createRule(createNT("I"),"b",
                                List.of(
                                createNT("A"),
                                createNT("F"))),

                        createRule(createNT("B"),"c",
                                List.of(
                                createNT("D"),
                                createNT("G")))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static CFGRule createRule(NonTerminal left, String terminal, List<NonTerminal> right) {
        return CFGRule.builder()
                .leftSide(left)
                .terminal(terminal == null ? null : new MySymbol(terminal))
                .rightSide(right)
                .build();
    }


    @ParameterizedTest
    @DisplayName("Given converted Grammar from PDA with SpecialNonTerminals," +
            " when convert to NonTerminals(Capital letter names for NonTerminal Symbols)," +
            " then return NonTerminals")
    @MethodSource("provideGrammarComponentsForTesting")
    void changeSpecialTerminalsToNonTerminals(GrammarComponents inputGrammar, GrammarComponents expectedGrammar) {
        GrammarComponents newGC = grammarSimplificationService.changeSpecialTerminalsToNonTerminals(inputGrammar);
        assertThat(newGC)
                .usingRecursiveComparison()
                .isEqualTo(expectedGrammar);
    }

    @Test
    void reductionOfCFG() {
    }

    @Test
    void removalOfUnitProductions() {
    }

    @Test
    void removalOfNullProductions() {
    }
}