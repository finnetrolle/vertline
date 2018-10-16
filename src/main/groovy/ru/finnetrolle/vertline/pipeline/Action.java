package ru.finnetrolle.vertline.pipeline;

@FunctionalInterface
public interface Action<I,O> {

    O execute(I in, Context context);

}
