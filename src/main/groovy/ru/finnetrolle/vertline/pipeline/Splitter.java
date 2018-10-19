package ru.finnetrolle.vertline.pipeline;

import java.util.List;

@FunctionalInterface
public interface Splitter<IN, LIST_OUT> {

    List<LIST_OUT> split(IN in, Context ctx);

}
