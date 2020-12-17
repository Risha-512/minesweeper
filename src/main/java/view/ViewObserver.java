package view;

import common.GameDifficulty;

public interface ViewObserver {
    void onButtonPressedAtCoordinates(int x, int y, ClickType clickType);

    void onResetButtonPressed();

    void onExitButtonPressed();

    void onNewGameButtonPressed();

    void createGame(GameDifficulty gameDifficulty);

    void createGame(int height, int width, int minesCount);

    void onShowHighScoresButtonPressed();
}
