package ru.finnetrolle.vertline.pipeline;

import java.util.stream.Collectors;

public class ForEach<IN, LIST_IN, LIST_OUT, OUT> implements Action<IN, OUT> {

    private final Splitter<IN, LIST_IN> splitter;
    private final Action<LIST_IN, LIST_OUT> action;
    private final Joiner<LIST_OUT, OUT> joiner;

    ForEach(Splitter<IN, LIST_IN> splitter, Action<LIST_IN, LIST_OUT> action, Joiner<LIST_OUT, OUT> joiner) {
        this.splitter = splitter;
        this.action = action;
        this.joiner = joiner;
    }

    public static <I,LI> ForEachBuilder<I,LI> splittedWith(Splitter<I, LI> splitter) {
        return new ForEachBuilder<>(splitter);
    }

    @Override
    public OUT execute(IN in, Context context) {
        return joiner.join(splitter.split(in, context).stream()
                        .map(e -> action.execute(e, context))
                        .collect(Collectors.toList()), context);
    }

    public static class ForEachBuilder<IN, LIST_IN> {

        private final Splitter<IN, LIST_IN> splitter;

        public ForEachBuilder(Splitter<IN, LIST_IN> splitter) {
            this.splitter = splitter;
        }

        public <LIST_OUT> ForEachActionBuilder<IN, LIST_IN, LIST_OUT> processEachWith(Action<LIST_IN, LIST_OUT> action) {
            return new ForEachActionBuilder<>(splitter, action);
        }

    }

    public static class ForEachActionBuilder<IN, LIST_IN, LIST_OUT> {

        private final Splitter<IN, LIST_IN> splitter;
        private final Action<LIST_IN, LIST_OUT> action;

        public ForEachActionBuilder(Splitter<IN, LIST_IN> splitter, Action<LIST_IN, LIST_OUT> action) {
            this.splitter = splitter;
            this.action = action;
        }

        public <OUT> ForEach<IN, LIST_IN, LIST_OUT, OUT> thenJoinWith(Joiner<LIST_OUT, OUT> joiner) {
            return new ForEach<>(splitter, action, joiner);
        }
    }

}
