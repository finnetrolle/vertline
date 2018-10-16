package ru.finnetrolle.vertline.pipeline;

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
}
