package com.example.bakalar.logic;

import com.example.bakalar.canvas.arrow.Arrow;
import com.example.bakalar.canvas.node.MyNode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class BoardHistory {
    private List<MyNode> nodes;
    private List<Arrow> arrows;

    public BoardHistory(List<MyNode> nodes) {
        List<MyNode> cloneNodes = new ArrayList<>();
        HashMap<Object, Object> clonedObjects = new HashMap<>();
        for (MyNode node : nodes) {
            try {
                cloneNodes.add(node.clone(clonedObjects));
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        Set<Arrow> uniqueArrows = new HashSet<>();

        for (MyNode node : cloneNodes) {
            uniqueArrows.addAll(node.getArrowsTo());
            uniqueArrows.addAll(node.getArrowsFrom());
        }
        List<Arrow> clonedArrows = new ArrayList<>(uniqueArrows);
        this.nodes = new ArrayList<>(cloneNodes);
        this.arrows = new ArrayList<>(clonedArrows);
    }
}
