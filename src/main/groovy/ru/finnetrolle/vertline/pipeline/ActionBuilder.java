package ru.finnetrolle.vertline.pipeline;

import java.util.Arrays;

public class ActionBuilder<TO, PI, PO> {

    private final Pipeline<PI,PO> pipeline;

    ActionBuilder(Pipeline<PI, PO> pipeline) {
        this.pipeline = pipeline;
    }

    public <NEW_TO> ActionBuilder<NEW_TO, PI, PO> emit(Action<TO, NEW_TO> action) {
        this.pipeline.addAction(action);
        return new ActionBuilder<>(this.pipeline);
    }

    public Pipeline<PI, PO> finish(Action<TO, PO> action) {
        this.pipeline.addAction(action);
        return pipeline;
    }

    public <LIST_TO> SplitBuilder<TO, LIST_TO, PI, PO> split(Action<TO, LIST_TO>... flows) {
        return new SplitBuilder<>(Arrays.asList(flows), this.pipeline);
    }
}
