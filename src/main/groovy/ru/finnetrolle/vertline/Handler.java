package ru.finnetrolle.vertline;

public interface Handler {

    void setPredecessor(Handler predecessor);
    void setSuccessor(Handler successor);

    void handle(Command command);

}
