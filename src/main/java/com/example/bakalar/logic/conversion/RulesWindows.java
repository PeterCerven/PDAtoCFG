package com.example.bakalar.logic.conversion;

import com.example.bakalar.logic.transitions.Transition;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RulesWindows {
    private List<CFGRule> rules;
    private WindowType windowType;
    private Transition transition;

    public RulesWindows(List<CFGRule> rules, Transition transition, WindowType windowType) {
        this.rules = rules;
        this.windowType = windowType;
        this.transition = transition;
    }

    public RulesWindows(WindowType windowType) {
        this.windowType = windowType;
        this.rules = new ArrayList<>();
    }
}
