package model;

import common.*;
import observable.BasicObservable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Model {
    private static final double MAX_MINES_PERCENTAGE = 0.7;

    private final BasicObservable<GameActionObserver> actionObservable = new BasicObservable<>();
    private final BasicObservable<GameStatusObserver> statusObservable = new BasicObservable<>();

    private int height;
    private int width;
    private int minesCount;
    private int flagsCount;
    private Cell[][] field;

    private boolean isGameStarted;
    private GameDifficulty gameDifficulty;

    public Model(int height, int width, int minesCount) {
        initializeModel(height, width, minesCount);
    }

    public Model(int height, int width, double minesPercentage) {
        this(height, width, calculateMinesCount(height, width, minesPercentage));
    }

    public Model(GameDifficulty gameDifficulty) {
        this(gameDifficulty.getHeight(), gameDifficulty.getWidth(), gameDifficulty.getMinesCount());
        this.gameDifficulty = gameDifficulty;
    }

    public void initializeModel(int height, int width, int minesCount) {
        this.gameDifficulty = null;
        initializeValues(height, width, minesCount);
    }

    public void initializeModel(int height, int width, double minesPercentage) {
        initializeModel(height, width, calculateMinesCount(height, width, minesPercentage));
    }

    public void initializeModel(GameDifficulty gameDifficulty) {
        initializeModel(gameDifficulty.getHeight(), gameDifficulty.getWidth(), gameDifficulty.getMinesCount());
        this.gameDifficulty = gameDifficulty;
    }

    public void reset() {
        this.flagsCount = 0;
        this.isGameStarted = false;
        this.field = new Cell[height][width];
        initializeField();
    }

    public void openCell(int x, int y) {
        if (!isGameStarted) {
            startGame(x, y);
        }
        if (!isCellValid(x, y) || field[x][y].isFlagged()) {
            return;
        }

        if (field[x][y].isMine()) {
            openMineCell();
            return;
        } else if (field[x][y].isEmpty()) {
            openEmptyCellsSpace(x, y);
        } else {
            openNumberCell(x, y);
        }

        if (isGameWon()) {
            doGameWonAction();
        }
    }

    public void flagCell(int x, int y) {
        if (!isGameStarted) {
            startGame(x, y);
        }
        if (!field[x][y].isClosed()) {
            return;
        }
        field[x][y].flag();

        if (field[x][y].isFlagged()) {
            actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateOpenedCell(x, y, MainCellType.FLAG));
            flagsCount++;
        } else {
            actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateOpenedCell(x, y, MainCellType.CLOSED));
            flagsCount--;
        }

        actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateFlaggedBombsCount(minesCount - flagsCount));
    }

    public void openAllCellsAround(int x, int y) {
        if (!isCellValid(x, y) || field[x][y].isClosed() || !field[x][y].isNumber() || countFlagsAround(x, y) != field[x][y].getNumber()) {
            return;
        }
        doActionForCellsAround(x, y, (xIndex, yIndex) -> {
            if (isCellValid(xIndex, yIndex) && field[xIndex][yIndex].isClosed()) {
                openCell(xIndex, yIndex);
            }
        });
    }

    public void addActionObserver(GameActionObserver observer) {
        actionObservable.addObserver(observer);
    }

    public void addStatusObserver(GameStatusObserver observer) {
        statusObservable.addObserver(observer);
    }

    public int getMinesCount() {
        return minesCount;
    }

    private static int calculateMinesCount(int height, int width, double minesPercentage) {
        minesPercentage = Math.min(minesPercentage, MAX_MINES_PERCENTAGE);
        return Math.max(1, (int) (height * width * minesPercentage));
    }

    private void initializeValues(int height, int width, int minesCount) {
        if (height <= 0 || width <= 0 || minesCount <= 0) {
            throw new IllegalArgumentException("Illegal arguments! All arguments should be positive");
        }
        this.height = height;
        this.width = width;
        this.minesCount = minesCount;
        reset();
    }

    private void initializeField() {
        doActionForEachCell((x, y) -> field[x][y] = new Cell());
    }

    private void fillField(int x, int y) {
        List<Cell> cellsList = new ArrayList<>(height * width);
        for (int i = 0; i < height; i++) {
            cellsList.addAll(Arrays.asList(field[i]));
        }
        cellsList.remove(width * x + y);

        Collections.shuffle(cellsList);
        for (int i = 0; i < minesCount; i++) {
            cellsList.get(i).setCellTypeToMine();
        }

        doActionForEachCell((i, j) -> {
            if (field[i][j].isMine()) {
                incrementNumbersAroundBomb(i, j);
            }
        });
    }

    private void incrementNumbersAroundBomb(int x, int y) {
        if (!isCellValid(x, y) || !field[x][y].isMine()) {
            return;
        }
        doActionForCellsAround(x, y, (xIndex, yIndex) -> {
            if (isCellValid(xIndex, yIndex) && !field[xIndex][yIndex].isMine()) {
                field[xIndex][yIndex].setCellTypeToNumber();
                field[xIndex][yIndex].incrementNumber();
            }
        });
    }

    private void startGame(int x, int y) {
        fillField(x, y);
        isGameStarted = true;
        statusObservable.notifyAllObservers(GameStatusObserver::onGameStart);
    }

    private void openMineCell() {
        doActionForEachCell((x, y) -> {
            if (field[x][y].isMine()) {
                field[x][y].open();
                actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateOpenedCell(x, y, MainCellType.MINE));
            }
        });

        statusObservable.notifyAllObservers(GameStatusObserver::onGameLost);
        actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateFlaggedBombsCount(minesCount - flagsCount));
    }

    private void openNumberCell(int x, int y) {
        field[x][y].open();
        actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateOpenedNumber(x, y, field[x][y].getNumber()));
    }

    private boolean isCellValid(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width;
    }

    private boolean isGameWon() {
        if (minesCount < flagsCount) {
            return false;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (field[i][j].isNumber() && field[i][j].isClosed()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void doGameWonAction() {
        doActionForEachCell((x, y) -> {
            if (field[x][y].isMine() && !field[x][y].isFlagged()) {
                actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateOpenedCell(x, y, MainCellType.FLAG));
            }
        });
        statusObservable.notifyAllObservers((GameStatusObserver observer) -> observer.onGameWon(gameDifficulty));
    }

    private void openEmptyCellsSpace(int x, int y) {
        Queue<FieldPoint> cellsQueue = new LinkedList<>();
        cellsQueue.add(new FieldPoint(x, y));

        while (!cellsQueue.isEmpty()) {
            FieldPoint cell = cellsQueue.remove();
            x = cell.getX();
            y = cell.getY();
            if (!openCellInEmptySpace(x, y)) {
                continue;
            }
            cellsQueue.add(new FieldPoint(x + 1, y));
            cellsQueue.add(new FieldPoint(x - 1, y));
            cellsQueue.add(new FieldPoint(x, y + 1));
            cellsQueue.add(new FieldPoint(x, y - 1));
        }
    }

    private boolean openCellInEmptySpace(int x, int y) {
        if (!isCellInEmptySpace(x, y)) {
            return false;
        }
        field[x][y].open();
        actionObservable.notifyAllObservers((GameActionObserver observer) -> observer.updateOpenedNumber(x, y, field[x][y].getNumber()));
        return true;
    }

    private boolean hasEmptyCellNear(int x, int y) {
        AtomicBoolean hasEmptyCellNear = new AtomicBoolean(false);
        doActionForCellsAround(x, y, (xIndex, yIndex) -> {
            if (isCellEmptyAndNotFlagged(xIndex, yIndex)) {
                hasEmptyCellNear.set(true);
            }
        });
        return hasEmptyCellNear.get();
    }

    private boolean isCellEmptyAndNotFlagged(int x, int y) {
        return isCellValid(x, y) && field[x][y].isEmpty() && !field[x][y].isFlagged() && !field[x][y].isClosed();
    }

    private boolean isCellInEmptySpace(int x, int y) {
        return isCellValid(x, y)
                && field[x][y].isClosed()
                && !field[x][y].isFlagged()
                && (field[x][y].isEmpty()
                || hasEmptyCellNear(x, y));
    }

    private int countFlagsAround(int x, int y) {
        AtomicInteger flagsCount = new AtomicInteger();

        doActionForCellsAround(x, y, (xIndex, yIndex) -> {
            if (isCellValid(xIndex, yIndex) && field[xIndex][yIndex].isFlagged()) {
                flagsCount.getAndIncrement();
            }
        });
        return flagsCount.get();
    }

    private void doActionForCellsAround(int x, int y, Callback callback) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                callback.call(x + i, y + j);
            }
        }
    }

    private void doActionForEachCell(Callback callback) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                callback.call(i, j);
            }
        }
    }
}
