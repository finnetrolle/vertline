package ru.finnetrolle.vertline.pipeline;

public class ActionBuilder<TO, PI, PO> {

    private final Pipeline<PI,PO> pipeline;

    ActionBuilder(Pipeline<PI, PO> pipeline) {
        this.pipeline = pipeline;
    }

    public <NEW_TO> ActionBuilder<NEW_TO, PI, PO> emit(Executable<TO, NEW_TO> executable) {
        Action<TO, NEW_TO> action = new Action<>(executable);
        this.pipeline.addAction(action);
        return new ActionBuilder<>(this.pipeline);
    }

    public Pipeline<PI, PO> finish(Executable<TO, PO> executable) {
        Action<TO, PO> action = new Action<>(executable);
        this.pipeline.addAction(action);
        return pipeline;
    }
}
