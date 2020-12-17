package main;

import common.GameDifficulty;
import model.Model;
import scores.HighScoresManager;
import timer.GameTimer;
import view.ClickType;
import view.View;
import view.ViewObserver;

public class Presenter implements ViewObserver {
    private final Model model;
    private final View view;
    private final GameTimer gameTimer;
    private final HighScoresManager scoresManager;

    public Presenter() {
        this.view = new View(GameDifficulty.EASY);
        this.view.addObserver(this);

        this.gameTimer = new GameTimer(view::updateTime);

        this.scoresManager = new HighScoresManager();
        this.scoresManager.addObserver(new HighScoresErrorsListener(view));

        this.model = new Model(GameDifficulty.EASY);
        this.model.addActionObserver(new GameActionListener(view));
        this.model.addStatusObserver(new GameStatusListener(view, gameTimer, scoresManager));
    }

    @Override
    public void onButtonPressedAtCoordinates(int x, int y, ClickType clickType) {
        switch (clickType) {
            case LEFT_MOUSE -> model.openCell(x, y);
            case RIGHT_MOUSE -> model.flagCell(x, y);
            case MIDDLE_MOUSE -> model.openAllCellsAround(x, y);
        }
    }

    @Override
    public void onResetButtonPressed() {
        gameTimer.stopTimer();
        model.reset();
        view.reset();
        view.updateFlaggedBombsCount(model.getMinesCount());
    }

    @Override
    public void onExitButtonPressed() {
        scoresManager.saveScores();
        view.closeWindow();
    }

    @Override
    public void onNewGameButtonPressed() {
        view.openNewGameSettingsWindow();
    }

    @Override
    public void createGame(GameDifficulty gameDifficulty) {
        gameTimer.stopTimer();
        model.initializeModel(gameDifficulty);
        view.initializeView(gameDifficulty);
    }

    @Override
    public void createGame(int height, int width, int minesPercentage) {
        gameTimer.stopTimer();
        model.initializeModel(height, width, (double) minesPercentage / 100);
        view.initializeView(height, width, model.getMinesCount());
    }

    @Override
    public void onShowHighScoresButtonPressed() {
        view.showHighScores(scoresManager.getHighScoresListMap());
    }
}
