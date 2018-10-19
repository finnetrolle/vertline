package ru.finnetrolle.vertline.pipeline;

@FunctionalInterface
public interface Action<I,O> {

    O execute(I in, Context context);

    static <IN, OUT> Pipeline<IN, OUT> pipeline(Class<IN> in, Class<OUT> out) {
        return Pipeline.define(in, out);
    }

    static <IN, OUT> SplitJoin.SplitJoinCarcassBuilder<IN, OUT> split(Class<IN> in, Class<OUT> out) {
        return SplitJoin.define(in, out);
    }

}
