package timer;

import common.Command;

public class GameTimer {
    private final GameTimerModel gameTimerModel;
    private boolean isTimerStarted;

    public GameTimer(Command<Integer> commandOnTick) {
        this.gameTimerModel = new GameTimerModel(1, commandOnTick);
    }

    public void initializeTimer() {
        if (isTimerStarted) {
            return;
        }
        gameTimerModel.startTimer();
        isTimerStarted = true;
    }

    public void stopTimer() {
        gameTimerModel.stopTimer();
        isTimerStarted = false;
    }

    public int getTimeInSeconds() {
        return gameTimerModel.getTimeInSeconds();
    }
}
