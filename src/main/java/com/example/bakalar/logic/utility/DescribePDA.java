package com.example.bakalar.logic.utility;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.transitions.Transition;
import com.example.bakalar.logic.utility.sorters.TransitionSorter;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.bakalar.logic.Board.*;

public class DescribePDA {
    private final TextField describeStates;
    private final TextField describeAlphabet;
    private final TextField describeStackAlphabet;
    private final TextField describeEndStates;
    private final VBox transFunctions;


    public DescribePDA(List<TextField> describeFields, VBox transFunctions) {
        this.describeStates = describeFields.get(0);
        this.describeAlphabet = describeFields.get(1);
        this.describeStackAlphabet = describeFields.get(2);
        this.describeEndStates = describeFields.get(3);
        this.transFunctions = transFunctions;

    }

    public void updateAllDescribePDA(List<MyNode> nodes, List<Arrow> arrows, List<Transition> transitions) {
        updateDescribeStates(nodes);
        updateDescribeStackAlphabet(arrows);
        updateDescribeEndStates(nodes);
        updateDescribeAlphabet(transitions);
        updateDescribeTransFunctions(transitions);
    }

    public void updateDescribeStates(List<MyNode> nodes) {
        StringBuilder text = new StringBuilder("K = {");
        for (MyNode node : nodes) {
            text.append(node.getName()).append(", ");
        }
        if (!nodes.isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeStates.setText(text.toString());
    }

    public void updateDescribeStackAlphabet(List<Arrow> arrows) {
        StringBuilder text = new StringBuilder(GAMMA_CAPITAL + " = {");
        Set<String> stackAlphabet = getStrings(arrows);
        for (String symbol : stackAlphabet) {
            text.append(symbol).append(", ");
        }
        if (!stackAlphabet.isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeStackAlphabet.setText(text.toString());
    }

    private void updateDescribeAlphabet(List<Transition> transitions) {
        StringBuilder text = new StringBuilder("Σ = {");
        String symbols = transitions.stream()
                .map(t -> t.getInputSymbolToRead().getName())
                .filter(s -> !s.equals(EPSILON))
                .distinct()
                .collect(Collectors.joining(", "));
        text.append(symbols).append("}");
        this.describeAlphabet.setText(text.toString());
    }


    private Set<String> getStrings(List<Arrow> arrows) {
        Set<String> stackAlphabet = new HashSet<>();
        stackAlphabet.add(STARTING_Z);
        for (Arrow arrow : arrows) {
            for (TransitionInputs transitionInputs : arrow.getTransitions()) {
                String pushString = transitionInputs.getPush();
                String popString = transitionInputs.getPop();
                if (!popString.equals(EPSILON))
                    stackAlphabet.add(popString);
                for (int i = 0; i < transitionInputs.getPush().length(); i++) {
                    String substring = pushString.substring(i, i + 1);
                    if (!substring.equals(EPSILON))
                        stackAlphabet.add(substring);
                }
            }
        }
        return stackAlphabet;
    }


    public void updateDescribeEndStates(List<MyNode> nodes) {
        StringBuilder text = new StringBuilder("F = {");
        boolean hasEndStates = false;
        for (MyNode node : nodes) {
            if (node.isEnding()) {
                text.append(node.getName()).append(", ");
                hasEndStates = true;
            }
        }
        if (hasEndStates) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeEndStates.setText(text.toString());
    }

    public void updateDescribeTransFunctions(List<Transition> transitions) {
        this.transFunctions.getChildren().clear();
        transitions.stream()
                .sorted(new TransitionSorter())
                .map(Transition::toString)
                .map(TextField::new)
                .peek(tf -> tf.setFont(new Font(18)))
                .peek(tf -> tf.setEditable(false))
                .forEach(this.transFunctions.getChildren()::add);
    }
}
