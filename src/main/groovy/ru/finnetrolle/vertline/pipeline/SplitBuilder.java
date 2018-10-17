package ru.finnetrolle.vertline.pipeline;

import java.util.ArrayList;
import java.util.List;

public class SplitBuilder<IN, SINGLE_OUT, PI, PO> {

    private final Pipeline<PI, PO> pipeline;

    private final List<Action<IN, SINGLE_OUT>> flows;

    SplitBuilder(List<Action<IN, SINGLE_OUT>> flows, Pipeline<PI, PO> pipeline) {
        this.flows = flows;
        this.pipeline = pipeline;
    }

    public static <T, D> ArrayOfActionsBuilder<T, D> to(Action<T,D> action) {
        return new ArrayOfActionsBuilder<>(action);
    }

//    public <OUT> Pipeline<PI, PO> joinWith(Joiner<SINGLE_OUT, OUT> joiner) {
//        SplitJoin<IN, SINGLE_OUT, OUT> splitJoin = new SplitJoin<>(joiner, flows);
//        this.pipeline.addAction(splitJoin);
//        return this.pipeline;
//    }

//    SplitBuilder(Action<IN, SINGLE_OUT> firstAction, Pipeline<PI, PO> pipeline) {
//        this.pipeline = pipeline;
//        this.flows = new ArrayList<>();
//        this.flows.add(firstAction);
//    }

//    public SplitBuilder<IN, SINGLE_OUT, PI, PO> andTo(Action<IN, SINGLE_OUT> anotherAction) {
//        this.flows.add(anotherAction);
//        return this;
//    }

    public <OUT> ActionBuilder<OUT, PI, PO> joinWith(Joiner<SINGLE_OUT, OUT> joiner) {
        SplitJoin<IN, SINGLE_OUT, OUT> splitJoin = new SplitJoin<>(joiner, flows);
        this.pipeline.addAction(splitJoin);
        return new ActionBuilder<>(this.pipeline);
    }
}
