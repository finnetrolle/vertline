package vertline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Класс, инкапсулирующий несколько последовательно выполняемых @Action
 * @param <I> - тип данных на входе в @Pipeline
 * @param <O> - тип данных на выходе из @Pipeline
 */
public class Pipeline<I, O> implements Action<I, O> {

    private List<Action> actions = new ArrayList<>();

    /**
     * Фабричный метод, создающий @Pipeline и задающий ей типы на входе и на выходе
     * @param incomingClazz - класс входных данных
     * @param outgoingClazz - класс выходных данных
     * @param <IN> - тип входных данных
     * @param <OUT> - тип выходных данных
     * @return
     */
    public static <IN, OUT> Pipeline<IN, OUT> define(Class<IN> incomingClazz, Class<OUT> outgoingClazz) {
        return new Pipeline<>();
    }

    /**
     * Добавление в @Pipeline следующего @Action
     * @param action - @Action
     * @param <TO> - тип данных, который будет возвращать добавляемый @Action
     * @return билдер @Pipeline
     */
    public <TO> PipelineBuilder<TO, I, O> emit(Action<I, TO> action) {
        this.addAction(action);
        return new PipelineBuilder<>(this);
    }

    /**
     * Добавление в @Pipeline финального действия. Отличается от добавления следующего действия тем,
     * что финальное действие должно всегда возвращать тип, который возвращает сам @Pipeline
     * @param action - финальный @Action с возвращаемым типом равным возвращаемому типу @Pipeline
     * @return @Pipeline
     */
    public Pipeline<I, O> finish(Action<I, O> action) {
        this.addAction(action);
        return this;
    }

    void addAction(Action action) {
        this.actions.add(action);
    }

    /**
     * Выполнение всех действий, загруженных в @Pipeline
     * @param in - входные данные
     * @param context - объект контекста @Context
     * @return выходной объект
     */
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
