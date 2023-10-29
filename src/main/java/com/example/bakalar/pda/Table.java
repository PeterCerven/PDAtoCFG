package com.example.bakalar.pda;

public class Table {
    private int row;
    private String state;
    private String input;
    private String stack;
    private String stateAfter;

    public Table(int row, String state, String input, String stack, String stateAfter) {
        this.row = row;
        this.state = state;
        this.input = input;
        this.stack = stack;
        this.stateAfter = stateAfter;
    }

}
