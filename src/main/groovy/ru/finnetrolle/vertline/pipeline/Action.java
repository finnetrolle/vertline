package ru.finnetrolle.vertline.pipeline;

public class Action<I, O> implements Executable<I,O> {

    private final Executable<I,O> executable;

    public Action(Executable<I,O> executable) {
        this.executable = executable;
    }

    @Override
    public O execute(I in, Context context) {
        return executable.execute(in, context);
    }
}
