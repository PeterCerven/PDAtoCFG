package com.example.bakalar.canvas.conversion;

import com.example.bakalar.canvas.Board;
import com.example.bakalar.canvas.transitions.Transition;
import com.example.bakalar.character.MySymbol;

import java.util.ArrayList;
import java.util.List;

import static com.example.bakalar.canvas.Board.*;

public class ConversionLogic {
    private final Board board;
    public ConversionLogic(Board board) {
        this.board = board;
    }

    public void convertPDA() {
        List<Transition> transitions = board.getNodesTransitions();
        createFirstRule();
        for (Transition transition : transitions) {
            Rule rule = new Rule(transition.getSymbolToPop(), transition.getInputSymbolToRead(), transition.getSymbolsToPush());
            board.addRule(rule);
        }
        board.addConversions();
    }

    private void createFirstRule() {
        MySymbol startNodeSymbol = new MySymbol(STARTING_S);
        MySymbol startS = new MySymbol(STARTING_S);
        MySymbol startZ = new MySymbol(STARTING_Z);
        MySymbol epsilon = new MySymbol(EPSILON);
        List<MySymbol> rightSide = new ArrayList<>();
        rightSide.add(startZ);
        rightSide.add(startS);
        Rule rule = new Rule(startNodeSymbol, epsilon, rightSide);

        board.addRule(rule);
    }



}
