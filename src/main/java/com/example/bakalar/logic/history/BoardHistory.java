package com.example.bakalar.logic.history;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BoardHistory {
    private UUID startNodeId;
    private int nodeCounter;
    private int idNodeCounter;

    public BoardHistory(UUID startNodeId, int nodeCounter, int idNodeCounter) {
        this.startNodeId = startNodeId;
        this.nodeCounter = nodeCounter;
        this.idNodeCounter = idNodeCounter;
    }
}
