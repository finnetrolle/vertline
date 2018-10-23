package vertline;

import java.util.ArrayList;
import java.util.List;

/**
 * Вид @Action, который принимает объект и несколько задаваемых цепочку @Pipeline или @Action.
 * Каждая цепочка выполняется для входного объекта и в конце объединяется в один выходной возвращаемый объект
 * @param <IN> - тип входных данных
 * @param <SINGLE_RESULT> - тип выходных данных для каждой цепочки @Pipeline или @Action
 * @param <OUT> - тип выходного объекта
 */
public class SplitJoin<IN, SINGLE_RESULT, OUT> implements Action<IN, OUT> {

    private final Joiner<SINGLE_RESULT, OUT> joiner;
    private final List<Action<IN, SINGLE_RESULT>> flows;

    /**
     * Фабричный метод для формирование конструкции @SplitJoin
     * @param incomingClazz - класс входящих данных
     * @param outgoingClazz - класс выходящих данных
     * @param <IN> - тип входящих данных
     * @param <OUT> - тип выходящих данных
     * @return билдер @SplitJoin
     */
    public static <IN, OUT> SplitJoinCarcassBuilder<IN, OUT> define(Class<IN> incomingClazz, Class<OUT> outgoingClazz) {
        return new SplitJoinCarcassBuilder<>();
    }

    SplitJoin(Joiner<SINGLE_RESULT, OUT> joiner, List<Action<IN, SINGLE_RESULT>> flows) {
        this.joiner = joiner;
        this.flows = flows;
    }

    /**
     * Выполнение всех действий, загруженных в @SplitJoin
     * @param in - входные данные
     * @param context - объект контекста @Context
     * @return выходной объект
     */
    @Override
    public OUT execute(IN in, Context context) {
        List<SINGLE_RESULT> results = new ArrayList<>();
        for (Action<IN, SINGLE_RESULT> flow : flows) {
            SINGLE_RESULT result = flow.execute(in, context);
            results.add(result);
        }
        return joiner.join(results, context);
    }

    /**
     * Промежуточный билдер
     */
    public static class SplitJoinCarcassBuilder<PI, PO> {

        /**
         * Добавление цепочки выполнения
         * @param action - действие @Action или @Pipeline
         * @param <SINGLE_RESULT> - тип результата выполнения действия
         * @return билдер, позволяющий добавить еще действий или определить @Joiner
         */
        public <SINGLE_RESULT> SplitJoinBuilder<PI, SINGLE_RESULT, PO> flow(Action<PI, SINGLE_RESULT> action) {
            List<Action<PI, SINGLE_RESULT>> actions = new ArrayList<>();
            actions.add(action);
            return new SplitJoinBuilder<>(actions);
        }

    }

    /**
     * Промежуточный билдер
     */
    public static class SplitJoinBuilder<IN, SINGLE_RESULT, OUT> {

        private final List<Action<IN, SINGLE_RESULT>> actions;

        SplitJoinBuilder(List<Action<IN, SINGLE_RESULT>> actions) {
            this.actions = actions;
        }

        /**
         * Добавление цепочки выполнения
         * @param action - действие @Action или @Pipeline
         * @return этот же билдер
         */
        public SplitJoinBuilder<IN, SINGLE_RESULT, OUT> flow(Action<IN, SINGLE_RESULT> action) {
            this.actions.add(action);
            return this;
        }

        /**
         * Определение метода, который соединит все результаты цепочек в один объект
         * @param joiner - метод для соеднинения результатов
         * @return объект @SplitJoin
         */
        public SplitJoin<IN, SINGLE_RESULT, OUT> thenJoin(Joiner<SINGLE_RESULT, OUT> joiner) {
            return new SplitJoin<>(joiner, actions);
        }
    }


}
