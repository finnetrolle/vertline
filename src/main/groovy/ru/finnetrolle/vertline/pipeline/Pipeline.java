package ru.finnetrolle.vertline.pipeline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pipeline<I,O> implements Executable<I,O> {

    private List<Executable> actions = new ArrayList<>();

    public <TO> ActionBuilder<TO, I, O> emit(Executable<I, TO> executable) {
        this.addAction(new Action<>(executable));
        return new ActionBuilder<>(this);
    }

    public Pipeline<I, O> finish(Executable<I, O> executable) {
        Action<I, O> action = new Action<>(executable);
        this.addAction(action);
        return this;
    }

    public static <IN,OUT> Pipeline<IN, OUT> define(Class<IN> incomingClazz, Class<OUT> outgoingClazz) {
        return new Pipeline<>();
    }

    void addAction(Action action) {
        this.actions.add(action);
    }

    @Override
    public O execute(I in, Context context) {
        if (actions.isEmpty()) {
            throw new RuntimeException("Empty pipeline");
        }
        Iterator<Executable> iterator = actions.iterator();
        Object result = iterator.next().execute(in, context);
        while (iterator.hasNext()) {
            result = iterator.next().execute(result, context);
        }
        return (O)result;
    }

}
