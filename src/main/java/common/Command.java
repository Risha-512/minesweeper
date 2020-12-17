package common;

public interface Command<T> {
    void execute(T t);
}
