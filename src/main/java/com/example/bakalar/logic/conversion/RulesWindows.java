package com.example.bakalar.logic.conversion;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RulesWindows {
    private List<CFGRule> rules;
    private WindowType windowType;

    public RulesWindows(List<CFGRule> rules, WindowType windowType) {
        this.rules = rules;
        this.windowType = windowType;
    }
}
