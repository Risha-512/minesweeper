package common;

public enum GameDifficulty {
    EASY(9, 9, 10, "Easy"),
    MEDIUM(16, 16, 40, "Medium"),
    HARD(16, 30, 99, "Hard");

    private final int height;
    private final int width;
    private final int minesCount;
    private final String name;

    GameDifficulty(int height, int width, int minesCount, String name) {
        this.height = height;
        this.width = width;
        this.minesCount = minesCount;
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public String getName() {
        return name;
    }

}
