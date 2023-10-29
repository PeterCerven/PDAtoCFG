package tests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bakalar.cfg.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


public class GrammarTest {
    private CNF cnf;


    @ParameterizedTest
    @DisplayName("Reduction of CFG")
    @MethodSource({"setOfRules", "afterReduction"})
    void reductionOfCFGTest(Set<Rule> rules, Set<Rule> afterReduction) {

        assertTrue(true);
    }

    @ParameterizedTest
    @DisplayName("Unit production removal")
    @MethodSource({"setOfRules", "afterUnitProductionRemoval"})
    void unitProductionRemovalTest(Set<Rule> rules, Set<Rule> afterUnitProductionRemoval) {
        assertTrue(true);
    }

    @ParameterizedTest
    @DisplayName("Null production removal")
    @MethodSource({"setOfRules", "afterNullProductionRemoval"})
    void nullProductionRemovalTest(Set<Rule> rules, Set<Rule> afterNullProductionRemoval) {
        assertTrue(true);
    }

    @ParameterizedTest
    @DisplayName("Start symbol change")
    @MethodSource({"setOfRules", "afterStartSymbolChange"})
    void startSymbolChangeTest(Set<Rule> rules, Set<Rule> afterStartSymbolChange) {
        assertTrue(true);
    }

    @ParameterizedTest
    @DisplayName("Max two symbols in right side")
    @MethodSource({"setOfRules", "afterMaxTwoSymbols"})
    void maxTwoSymbolsOnTheRightSideTest(Set<Rule> rules, Set<Rule> afterMaxTwoSymbols) {
        assertTrue(true);
    }

    @ParameterizedTest
    @DisplayName("Chomsky normal form")
    @MethodSource({"setOfRules", "afterChomskyNormalForm"})
    void chomskyNormalFormTest(Set<Rule> rules, Set<Rule> afterChomskyNormalForm) {
        assertTrue(true);
    }

    @ParameterizedTest
    @DisplayName("Greibach normal form")
    @MethodSource({"setOfRules", "afterGreibachNormalForm"})
    void GreibachNormalFormTest(Set<Rule> rules, Set<Rule> afterGreibachNormalForm) {
        assertTrue(true);
    }


    private static Stream<Arguments> setOfRules() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

    private static Stream<Arguments> afterReduction() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

    private static Stream<Arguments> afterUnitProductionRemoval() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

    private static Stream<Arguments> afterNullProductionRemoval() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }


    private static Stream<Arguments> afterStartSymbolChange() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

    private static Stream<Arguments> afterMaxTwoSymbols() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

    private static Stream<Arguments> afterChomskyNormalForm() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

    private static Stream<Arguments> afterGreibachNormalForm() {
        return Stream.of(
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "AC|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "A"));
                        add(new Rule('A', "a"));
                        add(new Rule('C', "c|BC"));
                    }
                }, 'S'),
                Arguments.of(new HashSet<Rule>() {
                    {
                        add(new Rule('S', "C|B"));
                        add(new Rule('A', "a"));
                        add(new Rule('E', "aA|e"));
                    }
                }, 'S')
        );
    }

   


}
