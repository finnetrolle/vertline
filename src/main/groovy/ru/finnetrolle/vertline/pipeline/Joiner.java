package ru.finnetrolle.vertline.pipeline;

import java.util.List;

@FunctionalInterface
public interface Joiner<LIST_T, OUT> {
    OUT join(List<LIST_T> results, Context context);
}
