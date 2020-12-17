package timer;

import common.Command;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GameTimerModel {
    private static final int ONE_SECOND = 1000;
    private final AtomicInteger timeInSeconds = new AtomicInteger(0);
    private final Timer timer;

    protected GameTimerModel(Command<Integer> commandOnTick) {
        timer = new Timer(ONE_SECOND, action -> commandOnTick.execute(timeInSeconds.incrementAndGet()));
    }

    protected void startTimer() {
        timeInSeconds.set(0);
        timer.start();
    }

    protected void stopTimer() {
        timer.stop();
    }

    protected int getTimeInSeconds() {
        return timeInSeconds.get();
    }
}
