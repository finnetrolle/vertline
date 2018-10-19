package ru.finnetrolle.vertline.pipeline;

import java.util.ArrayList;
import java.util.List;

public class SplitJoin<IN, SINGLE_RESULT, OUT> implements Action<IN, OUT> {

    private final Joiner<SINGLE_RESULT, OUT> joiner;
    private final List<Action<IN, SINGLE_RESULT>> flows;

    public static <IN, OUT> SplitJoinCarcassBuilder<IN, OUT> define(Class<IN> incomingClazz, Class<OUT> outgoingClazz) {
        return new SplitJoinCarcassBuilder<>();
    }

    SplitJoin(Joiner<SINGLE_RESULT, OUT> joiner, List<Action<IN, SINGLE_RESULT>> flows) {
        this.joiner = joiner;
        this.flows = flows;
    }

    @Override
    public OUT execute(IN in, Context context) {
        List<SINGLE_RESULT> results = new ArrayList<>();
        for (Action<IN, SINGLE_RESULT> flow : flows) {
            SINGLE_RESULT result = flow.execute(in, context);
            results.add(result);
        }
        return joiner.join(results, context);
    }

    public static class SplitJoinCarcassBuilder<PI, PO> {

        public <SINGLE_RESULT> SplitJoinBuilder<PI, SINGLE_RESULT, PO> flow(Action<PI, SINGLE_RESULT> action) {
            List<Action<PI, SINGLE_RESULT>> actions = new ArrayList<>();
            actions.add(action);
            return new SplitJoinBuilder<>(actions);
        }

    }

    public static class SplitJoinBuilder<IN, SINGLE_RESULT, OUT> {

        private final List<Action<IN, SINGLE_RESULT>> actions;

        public SplitJoinBuilder(List<Action<IN, SINGLE_RESULT>> actions) {
            this.actions = actions;
        }

        public SplitJoinBuilder<IN, SINGLE_RESULT, OUT> flow(Action<IN, SINGLE_RESULT> action) {
            this.actions.add(action);
            return this;
        }

        public SplitJoin<IN, SINGLE_RESULT, OUT> thenJoin(Joiner<SINGLE_RESULT, OUT> joiner) {
            return new SplitJoin<>(joiner, actions);
        }
    }


}
