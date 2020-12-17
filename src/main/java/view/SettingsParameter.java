package view;

import common.GameDifficulty;

public enum SettingsParameter {
    HEIGHT("Height", GameDifficulty.EASY.getHeight(), GameDifficulty.HARD.getHeight()),
    WIDTH("Width", GameDifficulty.EASY.getWidth(), GameDifficulty.HARD.getWidth()),
    MINES_COUNT("Mines percentage", 1, 70);

    private final String parameterName;
    private final int minValue;
    private final int maxValue;

    SettingsParameter(String parameterName, int minValue, int maxValue) {
        this.parameterName = parameterName;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }
}
