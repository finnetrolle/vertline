package ru.finnetrolle.vertline.pipeline;

import java.util.ArrayList;
import java.util.List;

public class ArrayOfActionsBuilder<IN, SINGLE_OUT> {

    final List<Action<IN, SINGLE_OUT>> actions = new ArrayList<>();

    public ArrayOfActionsBuilder(Action<IN, SINGLE_OUT> action) {
        actions.add(action);
    }

    public ArrayOfActionsBuilder<IN, SINGLE_OUT> to(Action<IN, SINGLE_OUT> action) {
        actions.add(action);
        return this;
    }

}
