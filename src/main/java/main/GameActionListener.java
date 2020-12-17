package main;

import common.MainCellType;
import model.GameActionObserver;
import view.ButtonType;
import view.View;

public class GameActionListener implements GameActionObserver {
    private final View view;

    public GameActionListener(View view) {
        this.view = view;
    }

    @Override
    public void updateOpenedCell(int x, int y, MainCellType cellType) {
        view.setFieldCellIcon(x, y, ButtonType.getTypeByMainCellType(cellType));
    }

    @Override
    public void updateOpenedNumber(int x, int y, int number) {
        view.setFieldCellIcon(x, y, ButtonType.getTypeByNumber(number));
    }

    @Override
    public void updateFlaggedBombsCount(int flaggedBombsCount) {
        view.updateFlaggedBombsCount(flaggedBombsCount);
    }
}
