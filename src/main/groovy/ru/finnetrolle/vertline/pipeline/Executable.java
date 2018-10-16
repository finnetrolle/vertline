package ru.finnetrolle.vertline.pipeline;

@FunctionalInterface
public interface Executable<I,O> {

    O execute(I in, Context context);

}
