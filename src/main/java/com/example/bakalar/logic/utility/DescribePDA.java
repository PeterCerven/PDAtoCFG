package com.example.bakalar.logic.utility;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.arrow.TransitionInputs;
import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.logic.transitions.Transition;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.bakalar.logic.Board.*;

public class DescribePDA {
    private final TextField describeStates;
    private final TextField describeAlphabet;
    private TextField describeStackAlphabet;
    private TextField describeStartingState;
    private TextField describeStartingStackSymbol;
    private TextField describeEndStates;
    private VBox transFunctions;
    private TextField inputFieldAlphabet;

    public DescribePDA(List<TextField> describeFields, VBox transFunctions, TextField inputFieldAlphabet) {
        this.describeStates = describeFields.get(0);
        this.describeAlphabet = describeFields.get(1);
        this.describeStackAlphabet = describeFields.get(2);
        this.describeEndStates = describeFields.get(3);
        this.transFunctions = transFunctions;
        this.inputFieldAlphabet = inputFieldAlphabet;
    }

    public void updateAllDescribePDA(List<MyNode> nodes, List<Arrow> arrows, List<Transition> transitions) {
        updateDescribeStates(nodes);
        updateDescribeAlphabet();
        updateDescribeStackAlphabet(arrows);
        updateDescribeEndStates(nodes);
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

    public void updateDescribeAlphabet() {
        StringBuilder text = new StringBuilder(SIGMA + " = {");
        Set<Character> alphabet = new HashSet<>();
        for (Character character : inputFieldAlphabet.getText().toCharArray()) {
            alphabet.add(character);
        }
        for (Character character : alphabet) {
            text.append(character).append(", ");
        }
        if (!inputFieldAlphabet.getText().isEmpty()) {
            text.delete(text.length() - 2, text.length());
        }
        text.append("}");
        this.describeAlphabet.setText(text.toString());
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
        for (Transition transition : transitions) {
            TextField textField = new TextField(transition.toString());
            textField.setEditable(false);
            this.transFunctions.getChildren().add(textField);
        }
    }
}
