package com.example.bakalar.canvas;

import com.example.bakalar.canvas.node.MyNode;
import com.example.bakalar.canvas.node.NodeTransition;
import com.example.bakalar.character.TerminalSymbol;

import java.util.List;
import java.util.Stack;

public class BoardLogic {
    private Stack<TerminalSymbol> stack;
    private List<TerminalSymbol> inputAlphabet;
    private Board board;
    private String begSymbol;
    private int currentState;
    private MyNode currentNode;


    public BoardLogic(Board board) {
        this.board = board;
    }

    public void start(String begSymbol, String inputAlphabet) {
        populateInputAlphabet(inputAlphabet);
        this.begSymbol = begSymbol;
        stack.push(new TerminalSymbol(begSymbol.charAt(0)));
        currentState = 0;
        currentNode = board.getStartNode();
        board.updateStack(stack);
    }

    public void step() {
        TerminalSymbol input = inputAlphabet.get(currentState);
        TerminalSymbol stackTop = stack.peek();
        List<NodeTransition> nodeTransitions = currentNode.getTransitions();
        TerminalSymbol read = new TerminalSymbol(nodeTransitions.get(0).getRead().charAt(0));
        TerminalSymbol pop = new TerminalSymbol(nodeTransitions.get(0).getPop().charAt(0));
        TerminalSymbol push = new TerminalSymbol(nodeTransitions.get(0).getPush().charAt(0));
        if (input.equals(read) && stackTop.equals(pop)) {
            stack.pop();
            stack.push(push);
//            currentNode = board.getNode(nodeTransitions.get(0).getTo());
        }


        board.updateStack(stack);
        currentState++;
    }

    private void populateInputAlphabet(String inputAlphabet) {
        for (int i = 0; i < inputAlphabet.length(); i++) {
            this.inputAlphabet.add(new TerminalSymbol(inputAlphabet.charAt(i)));
        }
    }


}
