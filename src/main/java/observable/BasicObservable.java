package observable;

import common.Command;

public class BasicObservable<T> extends Observable<T> {
    @Override
    public void notifyAllObservers(Command<T> observerCommand) {
        super.notifyAllObservers(observerCommand);
    }
}
