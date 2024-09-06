package com.example.bakalar.logic.conversion.simplify;

import com.example.bakalar.logic.conversion.CFGRule;
import com.example.bakalar.logic.conversion.MySymbol;
import com.example.bakalar.logic.conversion.NonTerminal;
import com.example.bakalar.logic.conversion.SpecialNonTerminal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.example.bakalar.logic.MainLogic.EPSILON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GrammarSimplificationServiceTest {
    private final GrammarSimplificationService grammarSimplificationService = new GrammarSimplificationService();

    // Helper functions
    private static CFGRule createRule(NonTerminal left, String terminal, List<NonTerminal> right) {
        return CFGRule.builder()
                .leftSide(left)
                .terminal(terminal == null ? null : new MySymbol(terminal))
                .rightSide(right)
                .build();
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

    // Test data
    private static Stream<Arguments> provideTestDataForGrammarForChangingToNonTerminalFromSpecialNonTerminal() {
        return Stream.of(
                arguments(
                        inputSpecialNonTerminalGrammar(),
                        expectedNonTerminalGrammar()
                )
        );
    }

    private static Stream<Arguments> provideTestDataForGrammarForReduction() {
        return Stream.of(
                arguments(
                        inputNonReducedGrammar(),
                        expectedReducedGrammar()
                )
        );
    }

    private static Stream<Arguments> provideTestDataForGrammarForRemovalOfUnitProduction() {
        return Stream.of(
                arguments(
                        inputGrammarWithUnitProductions(),
                        expectedGrammarWithoutUnitProductions()
                )
        );
    }

    private static Stream<Arguments> provideTestDataForGrammarForRemovalOfNullProductions() {
        return Stream.of(
                arguments(
                        inputGrammarWithNullProductions(),
                        expectedGrammarWithoutNullProductions()
                )
        );
    }

    // changeSpecialTerminalsToNonTerminals test data
    private static GrammarComponents inputSpecialNonTerminalGrammar() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createSNT("A", "B", "C"),
                                        createSNT("D", "E", "F"))),

                        createRule(createSNT("A", "B", "C"), "a",
                                List.of(
                                        createSNT("D", "E", "F"),
                                        createSNT("J", "K", "L"))),

                        createRule(createSNT("M", "N", "O"), "b",
                                List.of(
                                        createSNT("P", "Q", "R"),
                                        createSNT("V", "W", "X"))),

                        createRule(createSNT("Y", "Z", "A"), "c",
                                List.of(
                                        createSNT("B", "C", "D"),
                                        createSNT("H", "I", "J")))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static GrammarComponents expectedNonTerminalGrammar() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("C"),
                                        createNT("E"))),

                        createRule(createNT("C"), "a",
                                List.of(
                                        createNT("E"),
                                        createNT("H"))),

                        createRule(createNT("I"), "b",
                                List.of(
                                        createNT("A"),
                                        createNT("F"))),

                        createRule(createNT("B"), "c",
                                List.of(
                                        createNT("D"),
                                        createNT("G")))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    // reductionOfCFG test data
    private static GrammarComponents inputNonReducedGrammar() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("A"),
                                        createNT("C"))),
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("B"))),
                        createRule(createNT("A"), "a",
                                List.of()),
                        createRule(createNT("C"), "c",
                                List.of()),
                        createRule(createNT("C"), null,
                                List.of(
                                        createNT("B"),
                                        createNT("C"))),
                        createRule(createNT("E"), "a",
                                List.of(
                                        createNT("A"))),
                        createRule(createNT("E"), "e",
                                List.of())
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static GrammarComponents expectedReducedGrammar() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("C"),
                                        createNT("E"))),

                        createRule(createNT("C"), "a",
                                List.of(
                                        createNT("E"),
                                        createNT("H"))),

                        createRule(createNT("I"), "b",
                                List.of(
                                        createNT("A"),
                                        createNT("F"))),

                        createRule(createNT("B"), "c",
                                List.of(
                                        createNT("D"),
                                        createNT("G")))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static GrammarComponents inputGrammarWithUnitProductions() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("X"),
                                        createNT("Y")
                                )),
                        createRule(createNT("X"), "a",
                                List.of(
                                )),
                        createRule(createNT("Y"), null,
                                List.of(createNT("Z")
                                )),
                        createRule(createNT("y"), "b",
                                List.of(
                                )),
                        createRule(createNT("Z"), null,
                                List.of(
                                        createNT("M")
                                )),
                        createRule(createNT("M"), null,
                                List.of(
                                        createNT("N")
                                )),
                        createRule(createNT("N"), "a",
                                List.of(
                                ))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static GrammarComponents expectedGrammarWithoutUnitProductions() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("C"),
                                        createNT("E")
                                )),

                        createRule(createNT("C"), "a",
                                List.of(
                                        createNT("E"),
                                        createNT("H")
                                )),

                        createRule(createNT("I"), "b",
                                List.of(
                                        createNT("A"),
                                        createNT("F")
                                )),

                        createRule(createNT("B"), "c",
                                List.of(
                                        createNT("D"),
                                        createNT("G")
                                ))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    // removalOfNullProductions test data
    private static GrammarComponents inputGrammarWithNullProductions() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("A"),
                                        createNT("B"),
                                        createNT("A"),
                                        createNT("C")
                                )),
                        createRule(createNT("A"), "a",
                                List.of(
                                        createNT("A")
                                )),
                        createRule(createNT("A"), EPSILON,
                                List.of(
                                )),
                        createRule(createNT("B"), "b",
                                List.of(
                                        createNT("B")
                                )),
                        createRule(createNT("B"), EPSILON,
                                List.of(
                                )),
                        createRule(createNT("C"), "c",
                                List.of(
                                ))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    private static GrammarComponents expectedGrammarWithoutNullProductions() {
        return GrammarComponents.builder()
                .rules(List.of(
                        createRule(createNT("S"), null,
                                List.of(
                                        createNT("C"),
                                        createNT("E"))),

                        createRule(createNT("C"), "a",
                                List.of(
                                        createNT("E"),
                                        createNT("H"))),

                        createRule(createNT("I"), "b",
                                List.of(
                                        createNT("A"),
                                        createNT("F"))),

                        createRule(createNT("B"), "c",
                                List.of(
                                        createNT("D"),
                                        createNT("G")))
                ))
                .startingSymbol(new NonTerminal("S"))
                .build();
    }

    // removalOfUnitProductions test data

    @ParameterizedTest
    @DisplayName("Given converted Grammar from PDA with SpecialNonTerminals," +
            " When convert to NonTerminals(Capital letter names for NonTerminal Symbols)," +
            " then return NonTerminals")
    @MethodSource("provideTestDataForGrammarForChangingToNonTerminalFromSpecialNonTerminal")
    void changeSpecialTerminalsToNonTerminals(GrammarComponents inputGrammar, GrammarComponents expectedGrammar) {
        GrammarComponents newGC = grammarSimplificationService.changeSpecialTerminalsToNonTerminals(inputGrammar);
        assertThat(newGC)
                .usingRecursiveComparison()
                .isEqualTo(expectedGrammar);
    }

    @ParameterizedTest
    @DisplayName("Given converted Grammar from PDA," +
            " When reducing function is used," +
            " then return grammar without not needed rules")
    @MethodSource("provideTestDataForGrammarForReduction")
    void reductionOfCFG(GrammarComponents inputGrammar, GrammarComponents expectedGrammar) {
        GrammarComponents newGC = grammarSimplificationService.reductionOfCFG(inputGrammar);
        assertThat(newGC)
                .usingRecursiveComparison()
                .isEqualTo(expectedGrammar);
    }

    @ParameterizedTest
    @DisplayName("Given grammar with unit productions," +
            " When removal of unit production function is used," +
            " Then return grammar without unit productions")
    @MethodSource("provideTestDataForGrammarForRemovalOfUnitProduction")
    void removalOfUnitProductions(GrammarComponents inputGrammar, GrammarComponents expectedGrammar) {
        GrammarComponents newGC = grammarSimplificationService.removalOfUnitProductions(inputGrammar);
        assertThat(newGC)
                .usingRecursiveComparison()
                .isEqualTo(expectedGrammar);
    }

    @ParameterizedTest
    @DisplayName("Given grammar with null productions," +
            " When removal of null production is used," +
            " Then return grammar without null productions")
    @MethodSource("provideTestDataForGrammarForRemovalOfNullProductions")
    void removalOfNullProductions(GrammarComponents inputGrammar, GrammarComponents expectedGrammar) {
        GrammarComponents newGC = grammarSimplificationService.removalOfNullProductions(inputGrammar);
        assertThat(newGC)
                .usingRecursiveComparison()
                .isEqualTo(expectedGrammar);
    }

}
