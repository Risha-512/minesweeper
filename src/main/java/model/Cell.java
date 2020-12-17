package model;

import common.MainCellType;

class Cell {
    private int number;
    private boolean isClosed = true;
    private boolean isFlagged = false;
    private MainCellType type = MainCellType.NUMBER;

    protected void open() {
        isClosed = false;
    }

    protected void flag() {
        isFlagged = !isFlagged;
    }

    protected void setCellTypeToMine() {
        type = MainCellType.MINE;
    }

    protected void setCellTypeToNumber() {
        type = MainCellType.NUMBER;
    }

    protected void incrementNumber() {
        if (type == MainCellType.NUMBER) {
            number++;
        }
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isEmpty() {
        return type == MainCellType.NUMBER && number == 0;
    }

    public boolean isMine() {
        return type == MainCellType.MINE;
    }

    public boolean isNumber() {
        return type == MainCellType.NUMBER;
    }

    public int getNumber() {
        return number;
    }

    public MainCellType getType() {
        return type;
    }
}
