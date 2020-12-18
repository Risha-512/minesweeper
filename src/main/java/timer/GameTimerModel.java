package timer;

import common.Command;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class GameTimerModel {
    private static final long NANO_SECOND = 1000000000;
    private final long period;
    private final AtomicInteger timeInSeconds = new AtomicInteger(0);
    private final Command<Integer> commandOnTick;

    private ExecutorService executorService;
    private boolean isStarted;

    protected GameTimerModel(int periodInSeconds, Command<Integer> commandOnTick) {
        this.commandOnTick = commandOnTick;
        this.period = NANO_SECOND * periodInSeconds;
    }

    protected void startTimer() {
        timeInSeconds.set(0);

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            long startTime = System.nanoTime();
            isStarted = true;
            while (isStarted) {
                if (startTime + period * (timeInSeconds.get() + 1) > System.nanoTime()) {
                    continue;
                }
                commandOnTick.execute(timeInSeconds.incrementAndGet());
            }
        });
    }

    protected void stopTimer() {
        isStarted = false;
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    protected int getTimeInSeconds() {
        return timeInSeconds.get();
    }
}
