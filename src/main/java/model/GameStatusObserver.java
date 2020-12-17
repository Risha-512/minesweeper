package model;

import common.GameDifficulty;

public interface GameStatusObserver {
    void onGameStart();

    void onGameWon(GameDifficulty gameDifficulty);

    void onGameLost();
}
