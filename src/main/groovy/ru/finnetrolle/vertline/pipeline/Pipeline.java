package ru.finnetrolle.vertline.pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pipeline<I, O> implements Action<I, O> {

    private List<Action> actions = new ArrayList<>();

    public static <IN, OUT> Pipeline<IN, OUT> define(Class<IN> incomingClazz, Class<OUT> outgoingClazz) {
        return new Pipeline<>();
    }

//    public <LIST_TO> SplitBuilder<I, LIST_TO, I, O> split(Action<I, LIST_TO>... flows) {
//        return new SplitBuilder<>(Arrays.asList(flows), this);
//    }

    public <TO> ActionBuilder<TO, I, O> emit(Action<I, TO> action) {
        this.addAction(action);
        return new ActionBuilder<>(this);
    }

//    public <LIST_TO> SplitBuilder<I, LIST_TO, I, O> splitTo(Action<I, LIST_TO> firstAction) {
//        return new SplitBuilder<>(firstAction, this);
//    }

    public <LIST_TO> SplitBuilder<I, LIST_TO, I, O> split(ArrayOfActionsBuilder<I, LIST_TO> arrayOfActionsBuilder) {
        return new SplitBuilder<>(arrayOfActionsBuilder.actions, this);
    }

    public Pipeline<I, O> finish(Action<I, O> action) {
        this.addAction(action);
        return this;
    }

    void addAction(Action action) {
        this.actions.add(action);
    }

    @Override
    public O execute(I in, Context context) {
        if (actions.isEmpty()) {
            throw new RuntimeException("Empty pipeline");
        }
        Iterator<Action> iterator = actions.iterator();
        Object result = iterator.next().execute(in, context);
        while (iterator.hasNext()) {
            result = iterator.next().execute(result, context);
        }
        return (O) result;
    }

}
