package observable;

import common.Command;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {
    private final List<T> observers = new ArrayList<>();

    public void addObserver(T observer) {
        observers.add(observer);
    }

    public void removeObserver(T observer) {
        observers.remove(observer);
    }

    protected void notifyAllObservers(Command<T> observerCommand) {
        observers.forEach(observerCommand::execute);
    }
}
