package ru.finnetrolle.vertline.pipeline;

import java.util.List;

public class SplitBuilder<IN, SINGLE_OUT, PI, PO> {

    private final Pipeline<PI, PO> pipeline;

    private final List<Action<IN, SINGLE_OUT>> flows;

    SplitBuilder(List<Action<IN, SINGLE_OUT>> flows, Pipeline<PI, PO> pipeline) {
        this.flows = flows;
        this.pipeline = pipeline;
    }

//    public <OUT> Pipeline<PI, PO> joinWith(Joiner<SINGLE_OUT, OUT> joiner) {
//        SplitJoin<IN, SINGLE_OUT, OUT> splitJoin = new SplitJoin<>(joiner, flows);
//        this.pipeline.addAction(splitJoin);
//        return this.pipeline;
//    }

    public <OUT> ActionBuilder<OUT, PI, PO> joinWith(Joiner<SINGLE_OUT, OUT> joiner) {
        SplitJoin<IN, SINGLE_OUT, OUT> splitJoin = new SplitJoin<>(joiner, flows);
        this.pipeline.addAction(splitJoin);
        return new ActionBuilder<>(this.pipeline);
    }
}
