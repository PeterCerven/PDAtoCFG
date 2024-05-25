package com.example.bakalar.canvas;

import com.example.bakalar.logic.Board;
import javafx.scene.Group;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public abstract class MyObject extends Group implements Serializable, Erasable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected Long ID;


    public MyObject(Long ID) {
        this.ID = ID;
    }

    @Override
    public abstract void erase(Board board);
}
