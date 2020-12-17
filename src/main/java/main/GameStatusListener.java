package main;

import common.GameDifficulty;
import model.GameStatusObserver;
import scores.HighScoresManager;
import timer.GameTimer;
import view.View;

public class GameStatusListener implements GameStatusObserver {
    private final View view;
    private final GameTimer gameTimer;
    private final HighScoresManager scoresManager;

    public GameStatusListener(View view, GameTimer gameTimer, HighScoresManager scoresManager) {
        this.view = view;
        this.gameTimer = gameTimer;
        this.scoresManager = scoresManager;
    }

    @Override
    public void onGameStart() {
        gameTimer.initializeTimer();
    }

    @Override
    public void onGameWon(GameDifficulty gameDifficulty) {
        gameTimer.stopTimer();
        view.updateFlaggedBombsCount(0);
        view.disableFieldButtons();
        view.setWinningIcon();

        int score = gameTimer.getTimeInSeconds();
        if (scoresManager.isInAHighScore(score, gameDifficulty)) {
            String playerName = view.askPlayerName();
            if (playerName != null) {
                scoresManager.addScore(score, playerName, gameDifficulty);
            }
        }
    }

    @Override
    public void onGameLost() {
        gameTimer.stopTimer();
        view.disableFieldButtons();
        view.setLosingIcon();
    }
}
