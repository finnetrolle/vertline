package ru.finnetrolle.vertline.pipeline;

import java.util.ArrayList;
import java.util.List;

public class SplitJoin<IN, SINGLE_RESULT, OUT> implements Action<IN, OUT> {

    private final Joiner<SINGLE_RESULT, OUT> joiner;
    private final List<Action<IN, SINGLE_RESULT>> flows;

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


}
