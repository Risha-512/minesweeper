package model;

import common.MainCellType;

public interface GameActionObserver {
    void updateOpenedCell(int x, int y, MainCellType cellType);

    void updateOpenedNumber(int x, int y, int number);

    void updateFlaggedBombsCount(int flaggedBombsCount);
}
